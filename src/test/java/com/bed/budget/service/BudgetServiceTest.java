package com.bed.budget.service;

import com.bed.budget.dto.MonthlyBudgetReportResponse;
import com.bed.budget.dto.NewFamilyBudgetRequest;
import com.bed.budget.dto.NewFamilyBudgetResponse;
import com.bed.budget.mapper.BudgetMapper;
import com.bed.budget.model.Expense;
import com.bed.budget.model.FamilyBudget;
import com.bed.budget.model.MonthsBudget;
import com.bed.budget.repository.BudgetRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        BudgetService.class,
        BudgetMapper.class
})
class BudgetServiceTest {
    @Autowired
    private BudgetService budgetService;

    @MockBean
    private BudgetRepository budgetRepository;

    @Test
    void newFamilyBudget() {
        //Подготовка входных данных
        NewFamilyBudgetRequest request = new NewFamilyBudgetRequest();
        request.setName("Тестовые");

        when(budgetRepository.save(any())).thenReturn(0);

        //Подготовка ожидаемого результата
        NewFamilyBudgetResponse expectedResponse = new NewFamilyBudgetResponse();
        expectedResponse.setId(0);
        expectedResponse.setResponse("Успешно");

        //Запуск теста

        NewFamilyBudgetResponse actualResponse = budgetService.createFamilyBudget(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @SneakyThrows
    void monthlyReport() {
        //Подготовка входных данных
        String month = "01-2024";

        FamilyBudget family = new FamilyBudget("Тестовые");
        MonthsBudget monthBudget = new MonthsBudget(0);
        MonthsBudget monthBudgetFact = new MonthsBudget(1);

        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("mm-yyyy");
        Date date = dateFormat.parse(month);;
        System.out.println(date);
        Expense expense = new Expense("food", 100, date, "");
        monthBudget.getExpenses().add(expense);
        family.getExpenseTypes().add("food");
        family.getPlanMonthsBudgetList().put(date, monthBudget);
        family.getMonthsBudgetList().put(date, monthBudgetFact);

        when(budgetRepository.getCurrentBudget()).thenReturn(family);

        //Подготовка ожидаемого результата
        List<String> expectedResultList = new ArrayList<>();
        String result = "food: план 100.0, факт 0.0";
        expectedResultList.add(result);


        //Начало теста
        MonthlyBudgetReportResponse actualResponse = budgetService.monthlyBudgetReport(month);
        assertEquals(expectedResultList, actualResponse.getExpenses());
    }

}
