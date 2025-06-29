package com.mass_branches.repository;

import com.mass_branches.model.Budget;
import com.mass_branches.model.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {
    List<Stage> findAllByBudget(Budget budget);
}
