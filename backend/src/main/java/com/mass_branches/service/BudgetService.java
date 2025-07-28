package com.mass_branches.service;

import com.mass_branches.dto.request.BudgetItemPostRequest;
import com.mass_branches.dto.request.BudgetPostRequest;
import com.mass_branches.dto.request.BudgetPutRequest;
import com.mass_branches.dto.request.StagePostRequest;
import com.mass_branches.dto.response.*;
import com.mass_branches.exception.BadRequestException;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.*;
import com.mass_branches.repository.BudgetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private final BudgetRepository repository;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final BudgetItemService budgetItemService;
    private final StageService stageService;

    public BudgetPostResponse create(User user, BudgetPostRequest postRequest) {
        Customer customer = postRequest.customerId() != null ? customerService.findByUserAndIdOrThrowsNotFoundException(user, postRequest.customerId())
                : null;

        Budget budgetToSave = Budget.builder()
                .customer(customer)
                .description(postRequest.description())
                .proposalNumber(postRequest.proposalNumber())
                .bdi(postRequest.bdi() != null ? postRequest.bdi() : BigDecimal.ZERO)
                .totalValue(BigDecimal.ZERO)
                .totalWithBdi(BigDecimal.ZERO)
                .status(postRequest.status())
                .user(user)
                .active(true)
                .build();

        Budget savedBudget = repository.save(budgetToSave);

        return BudgetPostResponse.by(savedBudget);
    }

    @Transactional
    public void update(String id, BudgetPutRequest request, User user) {
        if (!id.equals(request.id()))
            throw new BadRequestException("The url id (%s) is different from the request body id(%s)".formatted(id, request.id()));

        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id) : findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(user, id);

        Customer customer = request.customerId() != null ? customerService.findByUserAndIdOrThrowsNotFoundException(budget.getUser(), request.customerId()) : null;
        BigDecimal bdi = request.bdi() != null ? request.bdi() : BigDecimal.ZERO;

        if (!bdi.equals(budget.getBdi())) {
            budget.setBdi(bdi);

            budgetItemService.updateBudgetItemsTotalValueByBudget(budget, bdi);
        }

        budget.setDescription(request.description());
        budget.setProposalNumber(request.proposalNumber());
        budget.setCustomer(customer);
        budget.setStatus(request.status());

        recalculateTotals(budget);
    }

    public List<BudgetGetResponse> listAll(Optional<String> description, Optional<BudgetStatus> status) {
        Sort sort = Sort.by("updatedAt").descending();

        boolean fetchByName = description.isPresent();
        boolean fetchByStatus = status.isPresent();
        List<Budget> budgets =
                fetchByName && fetchByStatus ? repository.findAllByDescriptionContainingAndStatus(description.get(), status.get(), sort)
                        : fetchByName ? repository.findAllByDescriptionContaining(description.get(), sort)
                        : fetchByStatus ? repository.findAllByStatus(status.get(), sort)
                        : repository.findAll(sort);

        return budgets.stream()
                .map(BudgetGetResponse::by)
                .toList();
    }

    public List<BudgetGetResponse> listAllMy(User requestingUser, Optional<String> description, Optional<BudgetStatus> status) {
        Sort sort = Sort.by("updatedAt").descending();

        boolean fetchByStatus = status.isPresent();
        boolean fetchByName = description.isPresent();
        List<Budget> budgets =
                fetchByName && fetchByStatus ? repository.findAllByDescriptionContainingAndStatusAndActiveIsTrue(description.get(), status.get(), sort)
                        : fetchByName ? repository.findAllByDescriptionContainingAndUserAndActiveIsTrue(description.get(), requestingUser, sort)
                        : fetchByStatus ? repository.findAllByStatusAndActiveIsTrue(status.get(), sort)
                        : repository.findAllByUserAndActiveIsTrue(requestingUser, sort);

        return budgets.stream()
                .map(BudgetGetResponse::by)
                .toList();
    }

    public BudgetGetResponse findById(User requestingUser, String id) throws InterruptedException {
        Budget response = requestingUser.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(requestingUser, id);

        return BudgetGetResponse.by(response);
    }

    public Budget findByIdOrThrowsNotFoundException(String id) {
        return repository.findById(id)
                .orElseThrow(() -> throwsBudgetIdNotFoundException(id));
    }

    public Budget findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(User user, String id) {
        return repository.findByUserAndIdAndActiveIsTrue(user, id)
                .orElseThrow(() -> throwsBudgetIdNotFoundException(id));
    }

    public Budget findByIdAndActiveIsTrueOrThrowsNotFoundException(String id) {
        return repository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> throwsBudgetIdNotFoundException(id));
    }

    public NotFoundException throwsBudgetIdNotFoundException(String id) {
        return new NotFoundException("Budget with id '%s' not found".formatted(id));
    }

    @Transactional
    public BudgetItemPostResponse addItem(User user, String id, BudgetItemPostRequest postRequest) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(user, id);

        if (!user.isAdmin() && !budget.getUser().equals(user)) throw throwsBudgetIdNotFoundException(id);

        Long itemId = postRequest.itemId();
        Item item = itemService.findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(itemId, budget.getUser());

        Long stageId = postRequest.stageId();
        boolean stageIdIsPresent = stageId != null;

        Stage stage = stageIdIsPresent ? stageService.findByIdOrThrowsNotFoundException(stageId) : null;

        BudgetItem savedBudgetItem = budgetItemService.create(budget, item, postRequest, stage);

        recalculateTotals(budget);
        if (stageIdIsPresent) stageService.recalculateTotalValue(stage);
        return BudgetItemPostResponse.by(savedBudgetItem);
    }

    public StagePostResponse addStage(User user, String id, StagePostRequest postRequest) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(user, id);

        if (!user.isAdmin() && !budget.getUser().equals(user)) throw throwsBudgetIdNotFoundException(id);

        Stage savedStage = stageService.create(postRequest, budget);

        return StagePostResponse.by(savedStage);
    }

    private void recalculateTotals(Budget budget) {
        BigDecimal totalValue = budgetItemService.totalValueOfItemsByBudgetId(budget.getId());
        BigDecimal totalWithBdi = budgetItemService.totalWithBdiOfItemsByBudgetId(budget.getId());

        budget.setTotalValue(totalValue);
        budget.setTotalWithBdi(totalWithBdi);

        repository.save(budget);
    }

    public List<BudgetElementGetResponse> listAllElementsByBudgetId(User user, String id) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(user, id);

        return listAllElementsByBudget(budget);
    }

    public List<BudgetElementGetResponse> listAllElementsByBudget(Budget budget) {
        List<BudgetItem> budgetItems = budgetItemService.findAllByBudget(budget);
        List<Stage> stages = stageService.findAllByBudget(budget);

        List<BudgetElementGetResponse> response = new ArrayList<>();

        budgetItems.forEach(budgetItem ->
                response.add(BudgetElementGetResponse.by(budgetItem))
        );

        stages.forEach(stage ->
                response.add(BudgetElementGetResponse.by(stage))
        );

        response.sort(Comparator
                .comparingInt((BudgetElementGetResponse el) -> Integer.parseInt(el.order().split("\\.")[0]))
                .thenComparingInt(el -> Integer.parseInt(el.order().split("\\.")[1]))
        );

        return response;
    }

    @Transactional
    public void removeItem(User user, String id, Long budgetItemId) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(user, id);

        BudgetItem budgetItem = budgetItemService.findByIdOrThrowsNotFoundException(budgetItemId);
        budgetItemService.remove(budget, budgetItem);

        recalculateTotals(budget);

        if (budgetItem.getStage() != null) stageService.recalculateTotalValue(budgetItem.getStage());
    }

    @Transactional
    public void removeStage(User user, String id, Long stageId) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(user, id);

        stageService.remove(budget, stageId);
    }

    public Integer numberOfBudgets(User user) {
        return repository.countBudgetsByUserAndActiveIsTrue(user);
    }

    public void delete(String id, User user) {
        Budget budget = findByIdAndActiveIsTrueOrThrowsNotFoundException(id);

        if (!user.isAdmin() && !budget.getUser().equals(user)) throw throwsBudgetIdNotFoundException(id);

        budget.setActive(false);

        repository.save(budget);
    }

    public byte[] exportBudget(String id, User user) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdAndActiveIsTrueOrThrowsNotFoundException(user, id);

        List<BudgetElementGetResponse> budgetElements = listAllElementsByBudget(budget);

        return generateBudgetFile(budget, budgetElements);
    }

    private byte[] generateBudgetFile(Budget budget, List<BudgetElementGetResponse> elements) {
        try (
                InputStream templateFile = getClass().getResourceAsStream("/templates/budget-template.xlsx");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()

        ) {
            if (templateFile == null) throw new IOException();

            Workbook workbook = new XSSFWorkbook(templateFile);
            Sheet sheet = workbook.getSheetAt(0);

            setBudgetDatas(budget, sheet);

            Row stageRowTemplate = sheet.getRow(14);
            CellStyle orderCellStyle = stageRowTemplate.getCell(0).getCellStyle();
            CellStyle stageCellStyle = stageRowTemplate.getCell(1).getCellStyle();
            CellStyle stageMonetaryCellStyle = stageRowTemplate.getCell(5).getCellStyle();

            Row itemRowTemplate = sheet.getRow(15);
            CellStyle itemCellStyle = itemRowTemplate.getCell(1).getCellStyle();
            CellStyle itemMonetaryCellStyle = itemRowTemplate.getCell(5).getCellStyle();

            short lineHeight = stageRowTemplate.getHeight();

            int initialElementLine = 14;
            AtomicInteger currentRowIndex = new AtomicInteger(initialElementLine);

            int numberOfElementsToBeAdded = elements.size();
            if (numberOfElementsToBeAdded > 3) {
                int numberOfElementsLinesAlreadyCreated = 3;
                int lastElementLine = 37;
                sheet.shiftRows(initialElementLine, lastElementLine, numberOfElementsToBeAdded - numberOfElementsLinesAlreadyCreated);
            }

            elements.forEach(element -> {
                Row row = sheet.createRow(currentRowIndex.getAndIncrement());
                row.setHeight(lineHeight);

                Cell orderCell = row.createCell(0);
                Cell nameCell = row.createCell(1);
                Cell unitCell = row.createCell(2);
                Cell quantityCell = row.createCell(3);
                Cell unitPriceCell = row.createCell(4);
                Cell totalValueCell = row.createCell(5);

                orderCell.setCellValue(element.order());
                orderCell.setCellStyle(orderCellStyle);

                switch (element.type()) {
                    case ITEM -> {
                        nameCell.setCellValue(element.name());
                        nameCell.setCellStyle(itemCellStyle);

                        unitCell.setCellValue(element.unitMeasurement());
                        unitCell.setCellStyle(itemCellStyle);

                        quantityCell.setCellValue(element.quantity().doubleValue());
                        quantityCell.setCellStyle(itemCellStyle);

                        unitPriceCell.setCellValue(element.unitPrice().doubleValue());
                        unitPriceCell.setCellStyle(itemMonetaryCellStyle);

                        totalValueCell.setCellValue(element.totalValue().doubleValue());
                        totalValueCell.setCellStyle(itemMonetaryCellStyle);
                    }

                    case STAGE -> {
                        nameCell.setCellValue(element.name());
                        nameCell.setCellStyle(stageCellStyle);

                        totalValueCell.setCellValue(element.totalValue().doubleValue());
                        totalValueCell.setCellStyle(stageMonetaryCellStyle);

                        unitCell.setCellStyle(stageCellStyle);
                        quantityCell.setCellStyle(stageCellStyle);
                        unitPriceCell.setCellStyle(stageCellStyle);
                    }
                }
            });

            workbook.write(outputStream);

            workbook.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setBudgetDatas(Budget budget, Sheet sheet) {
        Cell proposalNumberCell = sheet.getRow(0).getCell(1);
        proposalNumberCell.setCellValue(budget.getProposalNumber());

        if (budget.getCustomer() != null) {
            Cell customerCell = sheet.getRow(10).getCell(0);
            customerCell.setCellValue("Cliente: %s".formatted(budget.getCustomer().getName()));
        }

        Row bdiRow = sheet.getRow(17);

        Cell bdiCell = bdiRow.getCell(0);
        bdiCell.setCellValue("BDI (" + budget.getBdi() + "%):");

        Cell bdiValueCell = bdiRow.getCell(5);
        bdiValueCell.setCellValue(budget.getTotalWithBdi().min(budget.getTotalValue()).doubleValue());

        Cell proposalValue = sheet.getRow(18).getCell(5);
        proposalValue.setCellValue(budget.getTotalWithBdi().doubleValue());
    }

    public List<BudgetGetResponse> listAllByCustomerId(User user, String id) {
        Customer customer = customerService.findByUserAndIdOrThrowsNotFoundException(user, id);

        Sort sort = Sort.by("updatedAt").descending();

        List<Budget> budgets = repository.findAllByCustomerAndActiveIsTrue(customer, sort);

        return budgets.stream().map(BudgetGetResponse::by).toList();
    }
}
