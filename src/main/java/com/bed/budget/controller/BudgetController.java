package com.bed.budget.controller;

import com.bed.budget.dto.*;
import com.bed.budget.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("budget")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping("new-family")
    public NewUserResponse createFamilyBudget(@RequestBody NewUserRequest request) {
        return budgetService.createFamilyBudget(request);
    }

    @GetMapping("get-families")
    public UserListResponse getFamilies() {
        return budgetService.getList();
    }

    @PostMapping("new-months-budget")
    public CreateMonthsBudgetResponse createMonthsBudget(@RequestBody CreateMonthsBudgetRequest request) {
        return budgetService.createMonthsBudget(request);
    }

    @GetMapping("budgets/{months}")
    public MonthlyBudgetReportResponse getMonthlyReport(@PathVariable("months") String month) {
        return budgetService.monthlyBudgetReport(month);
    }

    @PostMapping("add-expense")
    public AddExpenseResponse addExpense(@RequestBody AddExpenseRequest request) {
        return budgetService.addExpense(request);
    }

}
