package com.mass_branches.converter;

import com.mass_branches.model.BudgetStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BudgetStatusConverter implements AttributeConverter<BudgetStatus, String> {
    @Override
    public String convertToDatabaseColumn(BudgetStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public BudgetStatus convertToEntityAttribute(String dbData) {
        return BudgetStatus.fromString(dbData);
    }
}
