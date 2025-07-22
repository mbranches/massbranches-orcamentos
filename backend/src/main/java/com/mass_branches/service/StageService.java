package com.mass_branches.service;

import com.mass_branches.dto.request.StagePostRequest;
import com.mass_branches.exception.BadRequestException;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Budget;
import com.mass_branches.model.BudgetItem;
import com.mass_branches.model.Stage;
import com.mass_branches.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StageService {
    private final StageRepository repository;
    private final BudgetItemService budgetItemService;

    public Stage create(StagePostRequest postRequest, Budget budget) {
        Stage stage = Stage.builder().budget(budget).orderIndex(postRequest.order()).name(postRequest.name()).totalValue(BigDecimal.ZERO).build();

        return repository.save(stage);
    }

    public List<Stage> findAllByBudget(Budget budget) {
        return repository.findAllByBudget(budget);
    }

    public void remove(Budget budget, Long id) {
        Stage stage = findByIdOrThrowsNotFoundException(id);

        if (!stage.getBudget().equals(budget)) throw new BadRequestException("The stage does not belongs to the given budget");

        repository.delete(stage);
    }

    public Stage findByIdOrThrowsNotFoundException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stage with id '%s' not found".formatted(id)));
    }

    public void recalculateTotalValue(Stage stage) {
        List<BudgetItem> budgetItems = budgetItemService.findAllByStage(stage);

        BigDecimal newTotalValue = budgetItems.stream()
                .map(BudgetItem::getTotalValue)
                .reduce(
                        BigDecimal.ZERO,
                        (totalValue, ac) -> ac.add(totalValue)
                );

        stage.setTotalValue(newTotalValue);

        repository.save(stage);
    }
}
