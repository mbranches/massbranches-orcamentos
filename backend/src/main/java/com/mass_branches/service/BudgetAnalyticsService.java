package com.mass_branches.service;

import com.mass_branches.model.BudgetStatus;
import com.mass_branches.model.User;
import com.mass_branches.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
public class BudgetAnalyticsService {

    private final BudgetRepository budgetRepository;

    public BigDecimal getConversionRate(User user) {
        BigDecimal numberOfBudgets = BigDecimal.valueOf(budgetRepository.countBudgetsByUserAndActiveIsTrue(user));
        BigDecimal numberOfApprovedBudgets = BigDecimal.valueOf(budgetRepository.countBudgetsByUserAndStatusAndActiveIsTrue(user, BudgetStatus.APROVADO));

        return numberOfBudgets.equals(BigDecimal.ZERO) ? BigDecimal.ZERO :
                numberOfApprovedBudgets.multiply(BigDecimal.valueOf(100)).divide(numberOfBudgets, 2, RoundingMode.HALF_UP);
    }
}
