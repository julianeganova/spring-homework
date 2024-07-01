package com.bed.budget.service;

import com.bed.budget.dto.*;
import com.bed.budget.mapper.BudgetMapper;
import com.bed.budget.model.*;
import com.bed.budget.repository.BudgetRepository;
import com.bed.budget.repository.ExpenseRepository;
import com.bed.budget.repository.ExpenseTypeRepository;
import com.bed.budget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetMapper budgetMapper;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
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

    private Optional<User> getCurrentUser() {
        return userRepository.findById(activeBudgetIndex);
    }

    public NewUserResponse createFamilyBudget(NewUserRequest request) {
        User user = new User(request.getName());
        userRepository.save(user);
        int budgetIndex = user.getId();
        activeBudgetIndex = budgetIndex;
        return budgetMapper.linkToNewFamilyBudgetResponse(SUCCESS_RESPONSE, budgetIndex);
    }

    public UserListResponse getList() {
        List<User> list = userRepository.findAll();
        List<String> result = new ArrayList<>();
        for (User u : list) {
            result.add(u.getName());
        }
        return budgetMapper.linkToFamilyBudgetListResponse(result);
    }

    public CreateMonthsBudgetResponse createMonthsBudget(CreateMonthsBudgetRequest request) {
        Optional<User> currentUser = getCurrentUser();
        Date monthOfBudget = getDateFromMonth(request.getMonth());
        MonthsBudget planBudget = new MonthsBudget(currentUser.get(), monthOfBudget, MonthsBudget.PLAN);
        MonthsBudget factBudget = new MonthsBudget(currentUser.get(), monthOfBudget, MonthsBudget.FACT);
        budgetRepository.save(planBudget);
        budgetRepository.save(factBudget);
        return budgetMapper.linkToCreateMonthsBudgetResponse(SUCCESS_RESPONSE);
    }

    public MonthlyBudgetReportResponse monthlyBudgetReport(String month) {
        Date monthOfBudget = getDateFromMonth(month);
        Optional<User> currentUser = userRepository.findById(activeBudgetIndex);
        List<ExpenseType> expenseTypes = expenseTypeRepository.findByUser(currentUser.get());
        List<String> report = new ArrayList<>();
        for (ExpenseType type : expenseTypes) {
            double sumPlan = 0;
            double sumFact = 0;
            MonthsBudget planBudget = budgetRepository.findByUserAndMonthAndType(currentUser.get(), monthOfBudget, MonthsBudget.PLAN);
            for (Expense expense : expenseRepository.findByBudget(planBudget)) {
                if (expense.getExpenseType().equals(type)) {
                    sumPlan += expense.getSum();
                }
            }
            MonthsBudget factBudget = budgetRepository.findByUserAndMonthAndType(currentUser.get(), monthOfBudget, MonthsBudget.FACT);
            for (Expense expense : expenseRepository.findByBudget(factBudget)) {
                if (expense.getExpenseType().equals(type)) {
                    sumFact += expense.getSum();
                }
            }
            report.add(String.format("%s: план %s, факт %s", type.getName(), sumPlan, sumFact));
        }
        return budgetMapper.linkToMonthlyBudgetReportResponse(report);
    }
    public AddExpenseResponse addExpense (AddExpenseRequest request) {
        Optional<User> currentUser = userRepository.findById(activeBudgetIndex);

        ExpenseType expenseType = expenseTypeRepository.findByUserAndName(currentUser.get(), request.getType());

        if (expenseType == null) {
            expenseType = new ExpenseType(request.getType(), currentUser.get());
            expenseTypeRepository.save(expenseType);
        }
        Date monthOfBudget = getDateFromMonth(request.getMonth());
        MonthsBudget budget = budgetRepository.findByUserAndMonthAndType(currentUser.get(), monthOfBudget, request.getBudgetType());
        Expense expense = new Expense(expenseType, request.getSum(), request.getDate(), request.getComment(), budget);

        expenseRepository.save(expense);
        return budgetMapper.linkToAddExpenseResponse(SUCCESS_RESPONSE);
    }

}
