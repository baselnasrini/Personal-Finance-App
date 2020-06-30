package com.example.personalFinanceApp.controllers;

import android.app.admin.DelegatedAdminReceiver;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.personalFinanceApp.Income;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.example.personalFinanceApp.ui.AddIncomeFragment;
import com.example.personalFinanceApp.ui.IncomeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddIncomeController {
    private AddIncomeFragment addIncomeFragment;
    private IncomeController parentController;
    private Income newIncome;
    private DatabaseHelper databaseHelper;

    public AddIncomeController(IncomeController parentController){
        this.addIncomeFragment = new AddIncomeFragment();
        this.parentController = parentController;
        this.databaseHelper = new DatabaseHelper(parentController.getFragment().getContext());
        addIncomeFragment.setController(this);
    }


    public AddIncomeFragment getFragment(){
        return this.addIncomeFragment;
    }

    public void addIncome() {
        String title = addIncomeFragment.getTitle().trim();
        Double amount = addIncomeFragment.getAmount();
        Income.Category category = addIncomeFragment.getCategory();
        String dateStr = addIncomeFragment.getDate();

        if(title.length() > 0 && amount > 0 && dateStr.length() > 0){
            Date date = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                date = format.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            newIncome = new Income(1, title, date, amount, category);
            this.databaseHelper.addIncome(newIncome);
            this.parentController.goBack();
        } else {
            Toast.makeText(this.addIncomeFragment.getContext(), "Please provide the required values", Toast.LENGTH_SHORT).show();
        }
    }
}
