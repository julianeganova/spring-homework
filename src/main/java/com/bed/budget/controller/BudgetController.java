package com.bed.budget.controller;

import com.bed.budget.dto.*;
import com.bed.budget.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigTreeConfigDataLocationResolver;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("budget")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping("newFamily")
    public NewFamilyBudgetResponse createFamilyBudget(@RequestBody NewFamilyBudgetRequest request) {
        return budgetService.createFamilyBudget(request);
    }

    @GetMapping("getFamilies")
    public FamilyBudgetListResponse getFamilies() {
        return budgetService.getList();
    }

    @PostMapping("newMonthsBudget")
    public CreateMonthsBudgetResponse createMonthsBudget(@RequestBody CreateMonthsBudgetRequest request) {
        return budgetService.createMonthsBudget(request);
    }

    @GetMapping("budgets/{months}")
    public MonthlyBudgetReportResponse getMonthlyReport(@PathVariable("months") String month) {
        return budgetService.monthlyBudgetReport(month);
    }

    @PostMapping("addExpense")
    public AddExpenseResponse addExpense(@RequestBody AddExpenseRequest request) {
        return budgetService.addExpense(request);
    }

}