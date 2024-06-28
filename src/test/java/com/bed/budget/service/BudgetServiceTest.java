//package com.bed.budget.service;
//
//import com.bed.budget.dto.MonthlyBudgetReportResponse;
//import com.bed.budget.dto.NewUserRequest;
//import com.bed.budget.dto.NewUserResponse;
//import com.bed.budget.mapper.BudgetMapper;
//import com.bed.budget.model.Expense;
//import com.bed.budget.model.ExpenseType;
//import com.bed.budget.model.User;
//import com.bed.budget.model.MonthsBudget;
//import com.bed.budget.repository.UserRepository;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {
//        BudgetService.class,
//        BudgetMapper.class
//})
//class BudgetServiceTest {
//    @Autowired
//    private BudgetService budgetService;
//
//    @MockBean
//    private UserRepository budgetRepository;
//
//    @Test
//    void newFamilyBudget() {
//        //Подготовка входных данных
//        NewUserRequest request = new NewUserRequest();
//        request.setName("Тестовые");
//
//        when(budgetRepository.save(any())).thenReturn(0);
//
//        //Подготовка ожидаемого результата
//        NewUserResponse expectedResponse = new NewUserResponse();
//        expectedResponse.setId(0);
//        expectedResponse.setResponse("Успешно");
//
//        //Запуск теста
//
//        NewUserResponse actualResponse = budgetService.createFamilyBudget(request);
//
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    @SneakyThrows
//    void monthlyReport() {
//        //Подготовка входных данных
//        String month = "01-2024";
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat();
//        dateFormat.applyPattern("MM-yyyy");
//        Date date = dateFormat.parse(month);
//
//        User family = new User("Тестовые");
//        List<ExpenseType> test = new ArrayList<>();
//        test.add(new ExpenseType("food", family));
//        //family.setExpenseTypes(test);
//        MonthsBudget plan = new MonthsBudget(family, date, MonthsBudget.PLAN);
//        MonthsBudget fact = new MonthsBudget(family, date, MonthsBudget.FACT);
//        Expense expense = new Expense(test.get(0), 100, date, "", plan);
//        List<Expense> expenses = new ArrayList<>();
//        expenses.add(expense);
//        //plan.setExpenses(expenses);
//        when(budgetRepository.getCurrentUser()).thenReturn(family);
//        when(budgetRepository.getExpenseTypes(family)).thenReturn(test);
//        when(budgetRepository.getBudget(family, date, MonthsBudget.PLAN)).thenReturn(plan);
//        when(budgetRepository.getBudget(family, date, MonthsBudget.FACT)).thenReturn(fact);
//        when(budgetRepository.getExpenses(plan)).thenReturn(expenses);
//
//        //Подготовка ожидаемого результата
//        List<String> expectedResultList = new ArrayList<>();
//        String result = "food: план 100.0, факт 0.0";
//        expectedResultList.add(result);
//
//
//        //Начало теста
//        MonthlyBudgetReportResponse actualResponse = budgetService.monthlyBudgetReport(month);
//        assertEquals(expectedResultList, actualResponse.getExpenses());
//    }
//
//}
