package com.bed.budget.service;

import com.bed.budget.dto.*;
import com.bed.budget.mapper.BudgetMapper;
import com.bed.budget.model.Expense;
import com.bed.budget.model.ExpenseType;
import com.bed.budget.model.User;
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
    //todo: добавить информацию о доходах в бюджет, CRUD типов расходов, скопировать бюджет (план)
    @SneakyThrows
    private Date getDateFromMonth(String month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("MM-yyyy");
        Date monthOfBudget = dateFormat.parse(month);
        return monthOfBudget;
    }

    public NewUserResponse createFamilyBudget(NewUserRequest request) {
        User user = new User(request.getName());
        int budgetIndex = budgetRepository.save(user);
        return budgetMapper.linkToNewFamilyBudgetResponse(SUCCESS_RESPONSE, budgetIndex);
    }

    public UserListResponse getList() {
        List<User> list = budgetRepository.getAllUsers();
        List<String> result = new ArrayList<>();
        for (User u : list) {
            result.add(u.getName());
        }
        return budgetMapper.linkToFamilyBudgetListResponse(result);
    }

    public CreateMonthsBudgetResponse createMonthsBudget(CreateMonthsBudgetRequest request) {
        User currentBudget = budgetRepository.getCurrentUser();
        Date monthOfBudget = getDateFromMonth(request.getMonth());
        budgetRepository.createNewMonthsBudget(currentBudget, monthOfBudget);
        return budgetMapper.linkToCreateMonthsBudgetResponse(SUCCESS_RESPONSE);
    }

    public MonthlyBudgetReportResponse monthlyBudgetReport(String month) {
        Date monthOfBudget = getDateFromMonth(month);
        User currentUser = budgetRepository.getCurrentUser();
        List<ExpenseType> expenseTypes = budgetRepository.getExpenseTypes(currentUser);
        List<String> report = new ArrayList<>();
        for (ExpenseType type : expenseTypes) {
            double sumPlan = 0;
            double sumFact = 0;
            MonthsBudget planBudget = budgetRepository.getBudget(currentUser, monthOfBudget, MonthsBudget.PLAN);
            for (Expense expense : budgetRepository.getExpenses(planBudget)) {
                if (expense.getExpenseType().equals(type)) {
                    sumPlan += expense.getSum();
                }
            }
            MonthsBudget factBudget = budgetRepository.getBudget(currentUser, monthOfBudget, MonthsBudget.FACT);
            for (Expense expense : budgetRepository.getExpenses(factBudget)) {
                if (expense.getExpenseType().equals(type)) {
                    sumFact += expense.getSum();
                }
            }
            report.add(String.format("%s: план %s, факт %s", type.getName(), sumPlan, sumFact));
        }
        return budgetMapper.linkToMonthlyBudgetReportResponse(report);
    }
    public AddExpenseResponse addExpense (AddExpenseRequest request) {
        User currentUser = budgetRepository.getCurrentUser();
        ExpenseType expenseType = budgetRepository.getExpenseType(currentUser, request.getType());
        if (expenseType == null) {
            expenseType = budgetRepository.addExpenseType(currentUser, request.getType());
        }
        Date monthOfBudget = getDateFromMonth(request.getMonth());
        MonthsBudget budget = budgetRepository.getBudget(currentUser, monthOfBudget, request.getBudgetType());
        Expense expense = new Expense(expenseType, request.getSum(), request.getDate(), request.getComment(), budget);

        budgetRepository.addExpense(expense);
        return budgetMapper.linkToAddExpenseResponse(SUCCESS_RESPONSE);
    }

}
