package com.bed.budget.repository;

import com.bed.budget.dto.FamilyBudgetListResponse;
import com.bed.budget.exception.BudgetAlreadyExistsException;
import com.bed.budget.exception.NoCurrentBudgetException;
import com.bed.budget.model.Expense;
import com.bed.budget.model.FamilyBudget;
import com.bed.budget.model.MonthsBudget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class BudgetRepository {
    private final List<FamilyBudget> budgets = new ArrayList<>();
    private int activeBudgetIndex = -1;
    public int save(FamilyBudget familyBudget) {
        budgets.add(familyBudget);
        activeBudgetIndex = budgets.indexOf(familyBudget);
        return budgets.indexOf(familyBudget);
    }

    public FamilyBudget getCurrentBudget() {
        if (activeBudgetIndex == -1) {
            throw new NoCurrentBudgetException("Не выбран аккаунт для ведения бюджета");
        }
        return getById(activeBudgetIndex);
    }
    public FamilyBudget getById(int index) {
        return budgets.get(index);
    }

    public List<String> getAllNames() {
        List<String> responseList = new ArrayList<>();
        for (FamilyBudget budget : budgets) {
            responseList.add(budget.getName());
        }
        return responseList;
    }

    public void createNewMonthsBudget(FamilyBudget familyBudget, Date month) {
        if (familyBudget.getMonthsBudgetList().containsKey(month)) {
            throw new BudgetAlreadyExistsException("Бюджет на месяц уже открыт");
        }
        familyBudget.getPlanMonthsBudgetList().put(month, new MonthsBudget(MonthsBudget.PLAN));
        familyBudget.getMonthsBudgetList().put(month, new MonthsBudget(MonthsBudget.FACT));
    }
    public void addExpenseType(FamilyBudget familyBudget, String expenseType) {
        familyBudget.getExpenseTypes().add(expenseType);
    }
    public void addExpense(FamilyBudget familyBudget, Expense expense, Date month, int budgetType) {

        MonthsBudget budget;
        if (budgetType == MonthsBudget.PLAN) {
            budget = familyBudget.getPlanMonthsBudgetList().get(month);
        } else {
            budget = familyBudget.getMonthsBudgetList().get(month);
        }
        budget.getExpenses().add(expense);
    }
}
