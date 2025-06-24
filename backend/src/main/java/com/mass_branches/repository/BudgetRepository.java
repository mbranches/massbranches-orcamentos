package com.mass_branches.repository;

import com.mass_branches.model.Budget;
import com.mass_branches.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {
    List<Budget> findAllByUser(User user, Sort sort);

    Optional<Budget> findByUserAndId(User user, String id);
}
