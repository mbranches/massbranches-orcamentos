package com.mass_branches.repository;

import com.mass_branches.model.Budget;
import com.mass_branches.model.BudgetStatus;
import com.mass_branches.model.Customer;
import com.mass_branches.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {
    List<Budget> findAllByUserAndActiveIsTrue(User user, Sort sort);

    Optional<Budget> findByUserAndIdAndActiveIsTrue(User user, String id);

    Optional<Budget> findByIdAndActiveIsTrue(String id);

    Integer countBudgetsByUserAndActiveIsTrue(User user);

    List<Budget> findAllByDescriptionContaining(String description, Sort sort);

    List<Budget> findAllByDescriptionContainingAndUserAndActiveIsTrue(String description, User user, Sort sort);

    List<Budget> findAllByDescriptionContainingAndStatusAndActiveIsTrue(String description, BudgetStatus status, Sort sort);

    List<Budget> findAllByStatusAndActiveIsTrue(BudgetStatus budgetStatus, Sort sort);

    List<Budget> findAllByDescriptionContainingAndStatus(String s, BudgetStatus budgetStatus, Sort sort);

    List<Budget> findAllByStatus(BudgetStatus status, Sort sort);

    long countBudgetsByCustomerAndActiveIsTrue(Customer customer);

    @Query("SELECT COALESCE(SUM(b.totalWithBdi), 0) FROM Budget b WHERE b.customer = :customer AND b.active = true")
    BigDecimal sumTotalWithBdiByCustomerAndActiveIsTrue(@Param("customer") Customer customer);
}
