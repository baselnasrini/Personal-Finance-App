package com.example.personalFinanceApp.controllers;

import android.content.Intent;
import android.widget.Toast;

import com.example.personalFinanceApp.CaptureActivityPortrait;
import com.example.personalFinanceApp.Expense;
import com.example.personalFinanceApp.MainActivity;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.example.personalFinanceApp.ui.AddExpenseFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpenseController {
    private AddExpenseFragment addExpenseFragment;
    private ExpenseController parentController;
    private Expense newExpense;
    private DatabaseHelper databaseHelper;

    public AddExpenseController(ExpenseController parentController){
        this.addExpenseFragment = new AddExpenseFragment();
        this.parentController = parentController;
        this.databaseHelper = new DatabaseHelper(parentController.getFragment().getContext());
        addExpenseFragment.setController(this);
    }


    public AddExpenseFragment getFragment(){
        return this.addExpenseFragment;
    }

    public void addExpense() {
        String title = addExpenseFragment.getTitle().trim();
        Double amount = addExpenseFragment.getAmount();
        Expense.Category category = addExpenseFragment.getCategory();
        String dateStr = addExpenseFragment.getDate();


        if(title.length() > 0 && amount > 0 && dateStr.length() > 0){

            Date date = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                date = format.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newExpense = new Expense(1, title, date, amount, category);
            this.databaseHelper.addExpense(newExpense);
            this.parentController.goBack();
        } else{
            Toast.makeText(this.addExpenseFragment.getContext(), "Please provide the required values", Toast.LENGTH_SHORT).show();
        }
    }

    public void scanQR(){
        IntentIntegrator qrScan = new IntentIntegrator(this.getFragment().getActivity());
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        qrScan.setPrompt("Scan a QR Code to get values from database");
        qrScan.setCaptureActivity(CaptureActivityPortrait.class);
        qrScan.setOrientationLocked(false);
        Intent intent = qrScan.createScanIntent();
        (((MainActivity)this.addExpenseFragment.getContext())).startActivityForResult(intent, MainActivity.SCAN_QR);
    }
}
