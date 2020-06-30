package com.example.personalFinanceApp.controllers;

import android.util.Log;

import com.example.personalFinanceApp.Income;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.example.personalFinanceApp.ui.IncomeDetailFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class IncomeDetailController {
    private IncomeDetailFragment fragment;
    private IncomeController parentController;
    private Income currentIncome;
    private DatabaseHelper databaseHelper;

    public IncomeDetailController(IncomeController parentController, Income income){
        this.fragment = new IncomeDetailFragment();
        this.parentController = parentController;
        this.databaseHelper = new DatabaseHelper(parentController.getFragment().getContext());
        this.currentIncome = income;
        fragment.setController(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Double incomeDouble = currentIncome.getAmount();
        fragment.setIncomeData(currentIncome.getTitle(), "" + incomeDouble , dateFormat.format(currentIncome.getDate()), currentIncome.getCategory().toString());
    }

    public IncomeDetailFragment getFragment(){
        return this.fragment;
    }

    public Income getCurrentIncome() {
        return currentIncome;
    }

    public void deleteIncome(){
        databaseHelper.deleteIncome(currentIncome);
        this.parentController.goBack();
    }
}
