package com.example.personalFinanceApp.controllers;

import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.personalFinanceApp.CaptureActivityPortrait;
import com.example.personalFinanceApp.Expense;
import com.example.personalFinanceApp.MainActivity;
import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.example.personalFinanceApp.ui.AddQrFragment;
import com.google.zxing.integration.android.IntentIntegrator;

public class AddQrController {
    private MainActivity activity;
    private AddQrFragment fragment;
    private DatabaseHelper databaseHelper;

    public AddQrController(MainActivity activity){
        this.activity = activity;
        this.fragment = new AddQrFragment();
        fragment.setController(this);
        databaseHelper = new DatabaseHelper(this.activity);

    }

    public void addQR() {
        String title = fragment.getTitle().trim();
        Double amount = fragment.getAmount();
        Expense.Category category = fragment.getCategory();

        if(fragment.getIdValue().length() > 0 && title.length() > 0 && amount > 0){
            this.databaseHelper.addQR(fragment.getIdValue(), title, amount, category.name());
            FragmentManager fragmentManager = ((MainActivity)this.fragment.getContext()).getSupportFragmentManager();
            fragmentManager.popBackStack();
            Toast.makeText(this.activity, "QR values added to database successfully", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(this.activity, "Please scan the QR code again or provide the required values", Toast.LENGTH_LONG).show();
        }
    }

    public void showFragment(){
        FragmentManager fragmentManager = this.activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, this.fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void scanAgain() {
        IntentIntegrator qrScan = new IntentIntegrator(((MainActivity)this.fragment.getContext()));
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        qrScan.setPrompt("Scan a QR Code to add a new QR Code value.");
        qrScan.setCaptureActivity(CaptureActivityPortrait.class);
        qrScan.setOrientationLocked(false);
        Intent intent = qrScan.createScanIntent();
        (((MainActivity)this.fragment.getContext())).startActivityForResult(intent, MainActivity.ADD_NEW_QR);
    }
}
