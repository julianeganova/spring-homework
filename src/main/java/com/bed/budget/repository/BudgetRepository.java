package com.bed.budget.repository;

import com.bed.budget.exception.BudgetAlreadyExistsException;
import com.bed.budget.exception.NoCurrentBudgetException;
import com.bed.budget.model.Expense;
import com.bed.budget.model.ExpenseType;
import com.bed.budget.model.User;
import com.bed.budget.model.MonthsBudget;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class BudgetRepository {
    private final List<User> budgets = new ArrayList<>();

    private final SessionFactory sessionFactory;
    private int activeBudgetIndex = 1;
    public int save(User user) {
        //budgets.add(user);
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
        activeBudgetIndex = user.getId();
        return user.getId();
    }

    public User getCurrentUser() {
        if (activeBudgetIndex == -1) {
            throw new NoCurrentBudgetException("Не выбран аккаунт для ведения бюджета");
        }
        return getById(activeBudgetIndex);
    }
    public User getById(int index) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, index);
        return user;
    }

    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        List<User> responseList = session.createQuery("select u from User u", User.class)
                .getResultList();
        return responseList;
    }

    public void createNewMonthsBudget(User user, Date month) {
        if (getBudget(user, month, MonthsBudget.PLAN) != null) {
            throw new BudgetAlreadyExistsException("Бюджет на месяц уже открыт");
        }
        MonthsBudget planBudget = new MonthsBudget(user, month, MonthsBudget.PLAN);
        MonthsBudget factBudget = new MonthsBudget(user, month, MonthsBudget.FACT);
        Session session = sessionFactory.getCurrentSession();
        session.persist(planBudget);
        session.persist(factBudget);
    }
    public ExpenseType addExpenseType(User user, String expenseTypeName) { //todo
        Session session = sessionFactory.getCurrentSession();
        ExpenseType expenseType = new ExpenseType(expenseTypeName, user);
        session.persist(expenseType);
        return expenseType;
    }
    public void addExpense(Expense expense) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(expense);
    }

    public ExpenseType getExpenseType(User user, String name) {
        Session session = sessionFactory.getCurrentSession();
        Optional<ExpenseType> expenseType = session.createQuery("select e from ExpenseType e where name = :name and user = :id", ExpenseType.class)
                .setParameter("name", name)
                .setParameter("id", user)
                .getResultList().stream().findFirst();
        return expenseType.isPresent() ? expenseType.get() : null;
    }

    public List<ExpenseType> getExpenseTypes(User user) {
        Session session = sessionFactory.getCurrentSession();
        List<ExpenseType> resultList = session.createQuery("select e from ExpenseType e where user = :user", ExpenseType.class)
                .setParameter("user", user)
                .getResultList();
        return  resultList;
    }

    public MonthsBudget getBudget(User user, Date month, int type) {
        Session session = sessionFactory.getCurrentSession();
        Optional<MonthsBudget> budget = session.createQuery("select b from MonthsBudget b where user = :user and type = :type and month = :month", MonthsBudget.class)
                .setParameter("user", user)
                .setParameter("type", type)
                .setParameter("month", month)
                .getResultList().stream().findFirst();
        return budget.isPresent() ? budget.get() : null;
    }

    public List<Expense> getExpenses(MonthsBudget budget) {
        Session session = sessionFactory.getCurrentSession();
        List<Expense> resultList = session.createQuery("select e from Expense e where budget = :budget", Expense.class)
                .setParameter("budget", budget)
                .getResultList();
        return resultList;
    }
}
