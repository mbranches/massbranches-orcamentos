package com.mass_branches.controller;

import com.mass_branches.dto.response.ConversionRateByCustomerType;
import com.mass_branches.dto.response.CustomerByCustomerRank;
import com.mass_branches.model.User;
import com.mass_branches.service.BudgetAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets/analytics")
@RestController
public class BudgetAnalyticsController {
    private final BudgetAnalyticsService service;

    @GetMapping("/conversion-rate")
    public ResponseEntity<BigDecimal> getBudgetConversionRate(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        BigDecimal response = service.getConversionRate(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalBudgeted(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        BigDecimal response = service.getTotalBudgeted(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-approved")
    public ResponseEntity<BigDecimal> getTotalApproved(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        BigDecimal response = service.getTotalApproved(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/type/conversion-rate")
    public ResponseEntity<List<ConversionRateByCustomerType>> getConversionRateByCustomerType(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<ConversionRateByCustomerType> response = service.getConversionRateByCustomerType(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/top")
    public ResponseEntity<List<CustomerByCustomerRank>> getCustomerRank(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<CustomerByCustomerRank> response = service.getCustomerRank(user);

        return ResponseEntity.ok(response);
    }
}
