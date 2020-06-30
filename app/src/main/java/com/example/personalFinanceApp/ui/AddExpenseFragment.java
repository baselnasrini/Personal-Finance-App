package com.example.personalFinanceApp.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.personalFinanceApp.Expense;
import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.controllers.AddExpenseController;

import java.util.Calendar;

public class AddExpenseFragment extends Fragment {
    private EditText title;
    private EditText amount;
    private EditText etDate;
    private Spinner categorySpinner;
    private Expense.Category category;
    private Button addBtn, scanBtn;

    private AddExpenseController controller;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_add, container, false);
        initializeComponents(view);
        registerListeners();

        setRetainInstance(true);

        return view;
    }

    private void initializeComponents(View view) {
        title = view.findViewById(R.id.expense_txtTitle);
        amount = view.findViewById(R.id.expense_txtAmount);
        etDate = view.findViewById(R.id.expense_txtDate);
        categorySpinner = view.findViewById(R.id.spin_category);
        addBtn = view.findViewById(R.id.btn_expense_add);
        scanBtn = view.findViewById(R.id.btn_expense_scan);
    }

    private void registerListeners() {
        categorySpinner.setAdapter(new ArrayAdapter<Expense.Category>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, Expense.Category.values()));
        addBtn.setOnClickListener(new AddNewExpenseListener());

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Expense.Category selected = (Expense.Category) parent
                        .getItemAtPosition(position);
                category = selected;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.scanQR();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month= month+1;
                        String date = dayOfMonth+"/"+ month + "/" + year;
                        etDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public Double getAmount(){
        String temp = amount.getText().toString();
        double value = 0;
        if (!"".equals(temp)){
            value=Double.parseDouble(temp);
        }

        return value;
    }

    public Expense.Category getCategory(){
        return category;
    }

    public String getDate(){
        return etDate.getText().toString();
    }

    public void setController(AddExpenseController controller){
        this.controller = controller;
    }

    public void setTitle(String asd) {
        title.setText(asd);

    }

    private class AddNewExpenseListener implements View.OnClickListener {
        public void onClick(View v) {
            controller.addExpense();
        }
    }

}
