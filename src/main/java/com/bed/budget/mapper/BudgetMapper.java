package com.bed.budget.mapper;

import com.bed.budget.dto.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
@Component
@NoArgsConstructor
public class BudgetMapper {
    public NewUserResponse linkToNewFamilyBudgetResponse(String response, int id) {
        NewUserResponse result = new NewUserResponse();
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

    public UserListResponse linkToFamilyBudgetListResponse(List<String> response) {
        List<String> result = response;
        return new UserListResponse(result);
    }

    public MonthlyBudgetReportResponse linkToMonthlyBudgetReportResponse(List<String> response) {
        List<String> result = response;
        return new MonthlyBudgetReportResponse(result);
    }
}
