package com.bed.budget.dto;

import com.bed.budget.model.FamilyBudget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyBudgetListResponse {
    private List<String> response = new ArrayList<>();

}
