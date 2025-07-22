package com.mass_branches.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum BudgetStatus {
    APROVADO("aprovado"),
    EM_ANALISE("em análise"),
    RECUSADO("recusado"),
    EM_ANDAMENTO("em andamento");

    private final String status;

    BudgetStatus(String status) {
        this.status = status;
    }

    public static BudgetStatus fromString(String status) {
        Optional<BudgetStatus> optionalStatus = Arrays.stream(BudgetStatus.values())
                .filter(s -> s.getStatus().equalsIgnoreCase(status))
                .findFirst();

        return optionalStatus
                .orElseThrow(() -> new IllegalArgumentException("Status inválido: " + status));
    }
}
