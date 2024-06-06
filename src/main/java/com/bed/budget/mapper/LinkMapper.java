package com.bed.budget.mapper;

import com.bed.budget.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LinkMapper {
    NewFamilyBudgetResponse linkToNewFamilyBudgetResponse(String response, int id);
    CreateMonthsBudgetResponse linkToCreateMonthsBudgetResponse(String result);
    AddExpenseResponse linkToAddExpenseResponse(String result);

    default FamilyBudgetListResponse linkToFamilyBudgetListResponse(List<String> response) {
        List<String> result = response;
        return new FamilyBudgetListResponse(result);
    }

    default MonthlyBudgetReportResponse linkToMonthlyBudgetReportResponse(List<String> response) {
        List<String> result = response;
        return new MonthlyBudgetReportResponse(result);
    }
}
