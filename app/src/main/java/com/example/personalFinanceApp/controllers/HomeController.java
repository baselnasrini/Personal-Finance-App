package com.example.personalFinanceApp.controllers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.personalFinanceApp.MainActivity;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.example.personalFinanceApp.ui.HomeFragment;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HomeController {
    private HomeFragment homeFragment;
    private MainActivity activity;
    private DatabaseHelper databaseHelper;

    public HomeController(MainActivity activity){
        this.homeFragment = new HomeFragment();
        this.activity = activity;

        this.databaseHelper = new DatabaseHelper(activity);
        homeFragment.setController(this);
        setTexts();
    }

    public HomeFragment getFragment (){
        return this.homeFragment;
    }

    public void setTexts() {
        ArrayList<Integer> result = databaseHelper.getSummary();
        SharedPreferences sharedPreferences= this.activity.getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        String summary = "Total incomes: " + result.get(0) + "\nTotal Expenses: " + (result.get(1) * -1) +"\nTotal Amount: " + (result.get(0) - result.get(1));
        homeFragment.setText("Welcome " + username , summary);
    }

    public PieData setChartData() {
        ArrayList<Integer> result = databaseHelper.getSummary();
        PieData data = null;

        if(result.get(0) > 0 || result.get(1) > 0){
            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new BarEntry(result.get(0), 0));
            entries.add(new BarEntry(result.get(1), 1));

            PieDataSet pieDataSet = new PieDataSet(entries, "");
            pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
            pieDataSet.setValueTextSize(15);

            ArrayList<String> labels = new ArrayList<>();
            labels.add("Incomes");
            labels.add("Expenses");

            data = new PieData(labels, pieDataSet);
        }
        return data;
    }
}
