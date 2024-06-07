package com.bed.budget.mapper;

import com.bed.budget.dto.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
@Component
@NoArgsConstructor
public class LinkMapper {
    public NewFamilyBudgetResponse linkToNewFamilyBudgetResponse(String response, int id) {
        NewFamilyBudgetResponse result = new NewFamilyBudgetResponse();
        result.setResponse(response);
        result.setId(id);
        return result;
    };
    public CreateMonthsBudgetResponse linkToCreateMonthsBudgetResponse(String result) {
        CreateMonthsBudgetResponse response = new CreateMonthsBudgetResponse();
        response.setResult(result);
        return response;
    };
    public AddExpenseResponse linkToAddExpenseResponse(String result) {
        AddExpenseResponse response = new AddExpenseResponse();
        response.setResult(result);
        return response;
    };

    public FamilyBudgetListResponse linkToFamilyBudgetListResponse(List<String> response) {
        List<String> result = response;
        return new FamilyBudgetListResponse(result);
    }

    public MonthlyBudgetReportResponse linkToMonthlyBudgetReportResponse(List<String> response) {
        List<String> result = response;
        return new MonthlyBudgetReportResponse(result);
    }
}
