package com.mass_branches.repository;

import com.mass_branches.dto.response.CustomerByCustomerRank;
import com.mass_branches.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    List<Budget> findAllByCustomerAndActiveIsTrue(Customer customer, Sort sort);

    long countBudgetsByUserAndStatusAndActiveIsTrue(User user, BudgetStatus budgetStatus);

    long countBudgetsByUserAndCustomer_Type_NameAndActiveIsTrue(User user, CustomerTypeName customerTypeName);

    long countBudgetsByUserAndCustomer_Type_NameAndStatusAndActiveIsTrue(User user, CustomerTypeName customerTypeName, BudgetStatus status);

    @Query("""
        SELECT new com.mass_branches.dto.response.CustomerByCustomerRank(
                b.customer.id, b.customer.name, b.customer.type.name, COUNT(b)
            )
            FROM Budget b
            WHERE b.active = true
            GROUP BY b.customer.id, b.customer.name, b.customer.type
            ORDER BY count(b) DESC
    """)
    List<CustomerByCustomerRank> findTopCustomersByBudgetCountByUserAndActiveIsTrue(User user, Pageable page);

    @Query("""
        SELECT COALESCE(SUM(b.totalWithBdi), 0) FROM Budget b
            WHERE b.user = :user AND b.active = true
    """)
    BigDecimal sumTotalWithBdiByUserAndActiveIsTrue(User user);

    @Query("""
        SELECT COALESCE(SUM(b.totalWithBdi), 0) FROM Budget b
            WHERE b.user = :user AND b.status = :status AND b.active = true
    """)
    BigDecimal sumTotalWithBdiByUserAndStatusAndActiveIsTrue(@Param("user") User user, @Param("status") BudgetStatus status);
}
