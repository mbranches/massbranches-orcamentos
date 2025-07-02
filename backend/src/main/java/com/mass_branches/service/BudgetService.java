package com.mass_branches.service;

import com.mass_branches.dto.request.BudgetItemPostRequest;
import com.mass_branches.dto.request.BudgetPostRequest;
import com.mass_branches.dto.request.StagePostRequest;
import com.mass_branches.dto.response.*;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.*;
import com.mass_branches.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private final BudgetRepository repository;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final BudgetItemService budgetItemService;
    private final StageService stageService;

    public BudgetPostResponse create(User user, BudgetPostRequest postRequest) {
        Customer customer = customerService.findByUserAndIdOrThrowsNotFoundException(user, postRequest.customerId());

        Budget budgetToSave = Budget.builder()
                .customer(customer)
                .description(postRequest.description())
                .proposalNumber(postRequest.proposalNumber())
                .bdi(postRequest.bdi() != null ? postRequest.bdi() : BigDecimal.ZERO)
                .totalValue(BigDecimal.ZERO)
                .totalWithBdi(BigDecimal.ZERO)
                .user(user)
                .build();

        Budget savedBudget = repository.save(budgetToSave);

        return BudgetPostResponse.by(savedBudget);
    }

    public List<BudgetGetResponse> listAll(User requestingUser, Boolean personal) {
        Sort sort = Sort.by("updatedAt").descending();

        boolean shouldFetchAllBudgets = !Boolean.TRUE.equals(personal);

        List<Budget> response = requestingUser.isAdmin() && shouldFetchAllBudgets ? repository.findAll(sort)
                : repository.findAllByUser(requestingUser, sort);

        return response.stream()
                .map(BudgetGetResponse::by)
                .toList();
    }

    public BudgetGetResponse findById(User requestingUser, String id) {
        Budget response = requestingUser.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(requestingUser, id);

        return BudgetGetResponse.by(response);
    }

    public Budget findByIdOrThrowsNotFoundException(String id) {
        return repository.findById(id)
                .orElseThrow(() -> throwsBudgetIdNotFoundException(id));
    }

    public Budget findByUserAndIdOrThrowsNotFoundException(User user, String id) {
        return repository.findByUserAndId(user, id)
                .orElseThrow(() -> throwsBudgetIdNotFoundException(id));
    }

    public NotFoundException throwsBudgetIdNotFoundException(String id) {
        return new NotFoundException("Budget with id '%s' not found".formatted(id));
    }

    public BudgetItemPostResponse addItem(User user, String id, BudgetItemPostRequest postRequest) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(user, id);

        if (!user.isAdmin() && !budget.getUser().equals(user)) throw throwsBudgetIdNotFoundException(id);

        Long itemId = postRequest.itemId();
        Item item = user.isAdmin() ? itemService.findByIdOrThrowsNotFoundException(itemId)
                : itemService.findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(itemId, user);

        BudgetItem savedBudgetItem = budgetItemService.create(budget, item, postRequest);

        recalculateTotals(budget);

        return BudgetItemPostResponse.by(savedBudgetItem);
    }

    public StagePostResponse addStage(User user, String id, StagePostRequest postRequest) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(user, id);

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

    public List<BudgetElementGetResponse> listAllElements(User user, String id) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(user, id);

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

    public void removeItem(User user, String id, Long budgetItemId) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(user, id);

        budgetItemService.remove(budget, budgetItemId);

        recalculateTotals(budget);
    }

    public void removeStage(User user, String id, Long stageId) {
        Budget budget = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(user, id);

        stageService.remove(budget, stageId);
    }

    public Integer numberOfBudgets(User user) {
        return repository.countBudgetsByUser(user);
    }
}
