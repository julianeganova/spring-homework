package com.bed.budget.dto;

import com.bed.budget.model.FamilyBudget;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FamilyBudgetListResponse {
    private List<String> response = new ArrayList<>();

}
