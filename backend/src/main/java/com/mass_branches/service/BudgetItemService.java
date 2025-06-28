package com.mass_branches.service;

import com.mass_branches.dto.request.BudgetItemPostRequest;
import com.mass_branches.dto.response.BudgetItemPostResponse;
import com.mass_branches.model.Budget;
import com.mass_branches.model.BudgetItem;
import com.mass_branches.model.Item;
import com.mass_branches.model.User;
import com.mass_branches.repository.BudgetItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
public class BudgetItemService {
    private final BudgetItemRepository repository;
    private final BudgetService budgetService;
    private final ItemService itemService;

    public BudgetItemPostResponse create(User user, String budgetId, BudgetItemPostRequest postRequest) {
        Budget budget = budgetService.findByUserAndIdOrThrowsNotFoundException(user, budgetId);
        BigDecimal bdi = budget.getBdi();

        if (!budget.getUser().equals(user)) throw budgetService.throwsBudgetIdNotFoundException(budgetId);

        Item item = itemService.findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(postRequest.itemId(), user);

        BigDecimal unitPrice = postRequest.unitPrice();
        BigDecimal quantity = postRequest.quantity();
        BigDecimal totalValue = unitPrice.multiply(quantity);
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

        BudgetItem savedBudgetItem = repository.save(budgetItemToSave);

        return BudgetItemPostResponse.by(savedBudgetItem);
    }
}
