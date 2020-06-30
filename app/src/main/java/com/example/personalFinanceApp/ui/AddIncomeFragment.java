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

import com.example.personalFinanceApp.Income;
import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.controllers.AddIncomeController;

import java.util.Calendar;

public class AddIncomeFragment extends Fragment {
    private EditText title;
    private EditText amount;
    private EditText etDate;
    private Spinner categorySpinner;
    private Income.Category category;
    private Button addBtn;
    private AddIncomeController controller;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_add, container, false);
        initializeComponents(view);
        registerListeners();

        setRetainInstance(true);

        return view;
    }

    private void initializeComponents(View view) {
        title = view.findViewById(R.id.income_row_txtTitle);
        amount = view.findViewById(R.id.txt_amount);
        etDate = view.findViewById(R.id.income_row_txtDate);
        categorySpinner = view.findViewById(R.id.spin_category);
        categorySpinner.setAdapter(new ArrayAdapter<Income.Category>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, Income.Category.values()));

        addBtn = view.findViewById(R.id.btn_income_add);
        addBtn.setOnClickListener(new AddNewIncomeListener());
    }

    private void registerListeners() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                category = (Income.Category) parent
                        .getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

    public Income.Category getCategory(){
        return category;
    }

    public String getDate(){
        return etDate.getText().toString();
    }

    public void setController(AddIncomeController controller){
        this.controller = controller;
    }

    private class AddNewIncomeListener implements View.OnClickListener {
        public void onClick(View v) {
            controller.addIncome();
        }
    }
}
