package com.mass_branches.service;

import com.mass_branches.dto.request.BudgetItemPostRequest;
import com.mass_branches.exception.BadRequestException;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Budget;
import com.mass_branches.model.BudgetItem;
import com.mass_branches.model.Item;
import com.mass_branches.repository.BudgetItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BudgetItemService {
    private final BudgetItemRepository repository;

    public BudgetItem create(Budget budget, Item item, BudgetItemPostRequest postRequest) {
        BigDecimal bdi = budget.getBdi();

        BigDecimal unitPrice = postRequest.unitPrice();
        BigDecimal quantity = postRequest.quantity();
        BigDecimal totalValue = unitPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
        BigDecimal percentage = bdi.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal totalWithBdi = totalValue.multiply(BigDecimal.ONE.add(percentage)).setScale(2, RoundingMode.HALF_UP);
        BudgetItem budgetItemToSave = BudgetItem.builder()
                .budget(budget)
                .item(item)
                .orderIndex(postRequest.order())
                .unitPrice(unitPrice)
                .quantity(quantity)
                .totalValue(totalValue)
                .totalWithBdi(totalWithBdi)
                .build();

        return repository.save(budgetItemToSave);
    }

    public BigDecimal totalValueOfItemsByBudgetId(String budgetId) {
        return repository.sumTotalValueByBudget_Id(budgetId);
    }

    public BigDecimal totalWithBdiOfItemsByBudgetId(String budgetId) {
        return repository.sumTotalWithBdiByBudget_Id(budgetId);
    }

    public List<BudgetItem> findAllByBudget(Budget budget) {
        return repository.findAllByBudget(budget);
    }

    public void remove(Budget budget, Long budgetItemId) {
        BudgetItem budgetItem = findByIdOrThrowsNotFoundException(budgetItemId);

        if (!budgetItem.getBudget().equals(budget)) throw new BadRequestException("Budget item does not belongs to the given budget");

        repository.delete(budgetItem);
    }

    public BudgetItem findByIdOrThrowsNotFoundException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Budget item with id '%s' not found".formatted(id)));
    }
}
