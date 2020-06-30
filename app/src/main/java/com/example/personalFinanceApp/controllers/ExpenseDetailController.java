package com.example.personalFinanceApp.controllers;

import com.example.personalFinanceApp.Expense;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.example.personalFinanceApp.ui.ExpenseDetailFragment;
import com.example.personalFinanceApp.ui.ExpenseFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpenseDetailController {
    private ExpenseDetailFragment fragment;
    private ExpenseController parentController;
    private Expense currentExpense;
    private DatabaseHelper databaseHelper;

    public ExpenseDetailController (ExpenseController parentController, Expense expense){
        this.fragment = new ExpenseDetailFragment();
        this.parentController = parentController;
        this.databaseHelper = new DatabaseHelper(parentController.getFragment().getContext());
        this.currentExpense = expense;
        fragment.setController(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        double expenseDouble = currentExpense.getAmount() * -1;
        fragment.setExpenseData(currentExpense.getTitle(), "" + expenseDouble, dateFormat.format(currentExpense.getDate()), currentExpense.getCategory().toString());
    }

    public ExpenseDetailFragment getFragment(){
        return this.fragment;
    }

    public Expense getCurrentExpense() {
        return currentExpense;
    }

    public void deleteExpense(){
        databaseHelper.deleteExpense(currentExpense);
        this.parentController.goBack();
    }

}
