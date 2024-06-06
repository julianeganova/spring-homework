package com.bed.budget.service;

import com.bed.budget.dto.*;
import com.bed.budget.exception.BudgetAlreadyExistsException;
import com.bed.budget.exception.NoCurrentBudgetException;
import com.bed.budget.mapper.LinkMapper;
import com.bed.budget.model.Expense;
import com.bed.budget.model.FamilyBudget;
import com.bed.budget.model.MonthsBudget;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final LinkMapper linkMapper;
    public static final String SUCCESS_RESPONSE = "Успешно";
    private final List<FamilyBudget> budgets = new ArrayList<>();
    private int activeBudgetIndex = -1;
    //todo: добавить информацию о доходах в бюджет, создание и хранение типов расходов, скопировать бюджет (план)
    @SneakyThrows
    private Date getDateFromMonth(String month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("mm-yyyy");
        Date monthOfBudget = dateFormat.parse(month);
        return monthOfBudget;
    }

    private FamilyBudget getCurrentBudget() {
        if (activeBudgetIndex == -1) {
            throw new NoCurrentBudgetException("Не выбран аккаунт для ведения бюджета");
        }
        return budgets.get(activeBudgetIndex);
    }

    public NewFamilyBudgetResponse createFamilyBudget(NewFamilyBudgetRequest request) {
        FamilyBudget familyBudget = new FamilyBudget(request.getName());
        budgets.add(familyBudget);
        activeBudgetIndex = budgets.indexOf(familyBudget);
        return linkMapper.linkToNewFamilyBudgetResponse(SUCCESS_RESPONSE, budgets.indexOf(familyBudget));
    }
    public FamilyBudgetListResponse getList() {
        List<String> responseList = new ArrayList<>();
        for (FamilyBudget budget : budgets) {
            responseList.add(budget.getName());
        }
        return linkMapper.linkToFamilyBudgetListResponse(responseList);
    }

    public CreateMonthsBudgetResponse createMonthsBudget(CreateMonthsBudgetRequest request) {
        FamilyBudget currentBudget = getCurrentBudget();
        Map<Date, MonthsBudget> monthsBudgets = new HashMap();
        Date monthOfBudget = getDateFromMonth(request.getMonth());
        if (currentBudget.getMonthsBudgetList().containsKey(monthOfBudget)) {
            throw new BudgetAlreadyExistsException("Бюджет на месяц уже открыт");
        }
        currentBudget.getPlanMonthsBudgetList().put(monthOfBudget, new MonthsBudget(MonthsBudget.PLAN));
        currentBudget.getMonthsBudgetList().put(monthOfBudget, new MonthsBudget(MonthsBudget.FACT));
        return linkMapper.linkToCreateMonthsBudgetResponse(SUCCESS_RESPONSE);
    }

    public MonthlyBudgetReportResponse monthlyBudgetReport(String month) {
        Date monthOfBudget = getDateFromMonth(month);
        FamilyBudget currentBudget = getCurrentBudget();
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
        return linkMapper.linkToMonthlyBudgetReportResponse(report);
    }
    public AddExpenseResponse addExpense (AddExpenseRequest request) {
        FamilyBudget currentBudget = getCurrentBudget();
        String expenseType = request.getType(); //todo отдельно заводить типы расходов и тут проверять тип
        if (!currentBudget.getExpenseTypes().contains((expenseType))) {
            currentBudget.getExpenseTypes().add(expenseType);
        }
        if (currentBudget.getExpenseTypes().contains(expenseType)) {
            double sumExpense = request.getSum();
            String comment = request.getComment();
            Date dateExpense = request.getDate();
            Expense expense = new Expense(expenseType, sumExpense, dateExpense, comment);
            Date monthOfBudget = getDateFromMonth(request.getMonth());
            MonthsBudget budget;
            if (request.getBudgetType() == MonthsBudget.PLAN) {
                budget = currentBudget.getPlanMonthsBudgetList().get(monthOfBudget);
            } else {
                budget = currentBudget.getMonthsBudgetList().get(monthOfBudget);
            }
            budget.getExpenses().add(expense);
        }
        return linkMapper.linkToAddExpenseResponse(SUCCESS_RESPONSE);
    }

}
