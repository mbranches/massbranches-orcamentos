package com.mass_branches.service;

import com.mass_branches.dto.response.ConversionRateByCustomerType;
import com.mass_branches.dto.response.CustomerByCustomerRank;
import com.mass_branches.model.BudgetStatus;
import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerTypeName;
import com.mass_branches.model.User;
import com.mass_branches.repository.BudgetRepository;
import com.mass_branches.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BudgetAnalyticsService {

    private final BudgetRepository budgetRepository;
    private final CustomerRepository customerRepository;

    public BigDecimal getConversionRate(User user) {
        BigDecimal numberOfBudgets = BigDecimal.valueOf(budgetRepository.countBudgetsByUserAndActiveIsTrue(user));
        BigDecimal numberOfApprovedBudgets = BigDecimal.valueOf(budgetRepository.countBudgetsByUserAndStatusAndActiveIsTrue(user, BudgetStatus.APROVADO));

        return numberOfBudgets.equals(BigDecimal.ZERO) ? BigDecimal.ZERO :
                numberOfApprovedBudgets.multiply(BigDecimal.valueOf(100)).divide(numberOfBudgets, 2, RoundingMode.HALF_UP);
    }

    public List<ConversionRateByCustomerType> getConversionRateByCustomerType(User user) {
        CustomerTypeName pessoaFisica = CustomerTypeName.PESSOA_FISICA;
        CustomerTypeName pessoaJuridica = CustomerTypeName.PESSOA_JURIDICA;

        BigDecimal numberOfBudgetsByPessoaFisica = BigDecimal.valueOf(budgetRepository.countBudgetsByUserAndCustomer_Type_NameAndActiveIsTrue(user, pessoaFisica));
        BigDecimal numberOfBudgetsByPessoaJuridica = BigDecimal.valueOf(budgetRepository.countBudgetsByUserAndCustomer_Type_NameAndActiveIsTrue(user, pessoaJuridica));

        BigDecimal numberOfApprovedBudgetsByPessoaFisica = BigDecimal.valueOf(budgetRepository.countBudgetsByUserAndCustomer_Type_NameAndStatusAndActiveIsTrue(user, pessoaFisica, BudgetStatus.APROVADO));
        BigDecimal numberOfApprovedBudgetsByPessoaJuridica = BigDecimal.valueOf(budgetRepository.countBudgetsByUserAndCustomer_Type_NameAndStatusAndActiveIsTrue(user, pessoaJuridica, BudgetStatus.APROVADO));

        BigDecimal conversionRateByPessoaFisica = numberOfBudgetsByPessoaFisica.equals(BigDecimal.ZERO) ? BigDecimal.ZERO :
                numberOfApprovedBudgetsByPessoaFisica.multiply(BigDecimal.valueOf(100)).divide(numberOfBudgetsByPessoaFisica, 2, RoundingMode.HALF_UP);
        BigDecimal conversionRateByPessoaJuridica = numberOfBudgetsByPessoaJuridica.equals(BigDecimal.ZERO) ? BigDecimal.ZERO :
                numberOfApprovedBudgetsByPessoaJuridica.multiply(BigDecimal.valueOf(100)).divide(numberOfBudgetsByPessoaJuridica, 2, RoundingMode.HALF_UP);

        return List.of(
                ConversionRateByCustomerType.by(pessoaFisica, conversionRateByPessoaFisica),
                ConversionRateByCustomerType.by(pessoaJuridica, conversionRateByPessoaJuridica)
        );
    }

    public List<CustomerByCustomerRank> getCustomerRank(User user) {
        Pageable pageable = PageRequest.of(0, 5);

        return budgetRepository.findTopCustomersByBudgetCountByUserAndActiveIsTrue(user, pageable);
    }

    public BigDecimal getTotalBudgeted(User user) {
        return budgetRepository.sumTotalWithBdiByUserAndActiveIsTrue(user);
    }

    public BigDecimal getTotalApproved(User user) {
        return budgetRepository.sumTotalWithBdiByUserAndStatusAndActiveIsTrue(user, BudgetStatus.APROVADO);
    }
}
