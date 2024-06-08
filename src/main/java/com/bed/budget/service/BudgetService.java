package com.bed.budget.service;

import com.bed.budget.dto.*;
import com.bed.budget.mapper.BudgetMapper;
import com.bed.budget.model.Expense;
import com.bed.budget.model.FamilyBudget;
import com.bed.budget.model.MonthsBudget;
import com.bed.budget.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetMapper budgetMapper;
    private final BudgetRepository budgetRepository;
    public static final String SUCCESS_RESPONSE = "Успешно";

    private int activeBudgetIndex = -1;
    //todo: добавить информацию о доходах в бюджет, создание и хранение типов расходов, скопировать бюджет (план)
    @SneakyThrows
    private Date getDateFromMonth(String month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("mm-yyyy");
        Date monthOfBudget = dateFormat.parse(month);
        return monthOfBudget;
    }

    public NewFamilyBudgetResponse createFamilyBudget(NewFamilyBudgetRequest request) {
        FamilyBudget familyBudget = new FamilyBudget(request.getName());
        int budgetIndex = budgetRepository.save(familyBudget);
        return budgetMapper.linkToNewFamilyBudgetResponse(SUCCESS_RESPONSE, budgetIndex);
    }

    public FamilyBudgetListResponse getList() {
        return budgetMapper.linkToFamilyBudgetListResponse(budgetRepository.getAllNames());
    }

    public CreateMonthsBudgetResponse createMonthsBudget(CreateMonthsBudgetRequest request) {
        FamilyBudget currentBudget = budgetRepository.getCurrentBudget();
        Date monthOfBudget = getDateFromMonth(request.getMonth());
        budgetRepository.createNewMonthsBudget(currentBudget, monthOfBudget);
        return budgetMapper.linkToCreateMonthsBudgetResponse(SUCCESS_RESPONSE);
    }

    public MonthlyBudgetReportResponse monthlyBudgetReport(String month) {
        Date monthOfBudget = getDateFromMonth(month);
        FamilyBudget currentBudget = budgetRepository.getCurrentBudget();
        Set<String> expenseTypes = currentBudget.getExpenseTypes();
        List<String> report = new ArrayList<>();
        for (String type : expenseTypes) {
            double sumPlan = 0;
            double sumFact = 0;
            MonthsBudget planBudget = currentBudget.getPlanMonthsBudgetList().get(monthOfBudget);
            for (Expense expense : planBudget.getExpenses()) {
                if (expense.getExpenseType().equals(type)) {
                    sumPlan += expense.getSum();
                }
            }
            MonthsBudget factBudget = currentBudget.getMonthsBudgetList().get(monthOfBudget);
            for (Expense expense : factBudget.getExpenses()) {
                if (expense.getExpenseType().equals(type)) {
                    sumFact += expense.getSum();
                }
            }
            report.add(String.format("%s: план %s, факт %s", type, sumPlan, sumFact));
        }
        return budgetMapper.linkToMonthlyBudgetReportResponse(report);
    }
    public AddExpenseResponse addExpense (AddExpenseRequest request) {
        FamilyBudget currentBudget = budgetRepository.getCurrentBudget();
        String expenseType = request.getType(); //todo отдельно заводить типы расходов и тут проверять тип
        if (!currentBudget.getExpenseTypes().contains(expenseType)) {
            budgetRepository.addExpenseType(currentBudget, expenseType);
        }
        Date monthOfBudget = getDateFromMonth(request.getMonth());
        Expense expense = new Expense(expenseType, request.getSum(), request.getDate(), request.getComment());

        budgetRepository.addExpense(currentBudget, expense, monthOfBudget, request.getBudgetType());
        return budgetMapper.linkToAddExpenseResponse(SUCCESS_RESPONSE);
    }

}
