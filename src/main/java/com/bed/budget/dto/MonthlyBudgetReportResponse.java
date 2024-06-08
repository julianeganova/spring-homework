package com.bed.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MonthlyBudgetReportResponse {
    private List<String> expenses;
}
