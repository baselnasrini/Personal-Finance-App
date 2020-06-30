package com.example.personalFinanceApp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.personalFinanceApp.Expense;
import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.controllers.AddQrController;

public class AddQrFragment extends Fragment {
    private AddQrController controller;
    private TextView id;
    private EditText title, amount;
    private Spinner categorySpinner;
    private Expense.Category category;
    private Button saveBtn, againBtn;


    public AddQrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_qr, container, false);
        initializeComponents(view);
        registerListeners();

        setRetainInstance(true);
        return view;
    }

    private void initializeComponents(View view) {
        id = view.findViewById(R.id.txt_add_qr_id);
        title = view.findViewById(R.id.txt_add_qr_title);
        amount = view.findViewById(R.id.txt_add_qr_amount);
        categorySpinner = view.findViewById(R.id.txt_add_qr_category);
        saveBtn = view.findViewById(R.id.btn_add_qr_ok);
        againBtn = view.findViewById(R.id.btn_qr_scan_again);


    }

    private void registerListeners() {
        categorySpinner.setAdapter(new ArrayAdapter<Expense.Category>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, Expense.Category.values()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                category = (Expense.Category) parent
                        .getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.scanAgain();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.addQR();
            }
        });
    }

    public String getIdValue(){
        return id.getText().toString();
    }

    public Expense.Category getCategory(){
        return category;
    }

    public Double getAmount() {
        String temp = amount.getText().toString();
        double value = 0;
        if (!"".equals(temp)){
            value=Double.parseDouble(temp);
        }
        return value;
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public void setController(AddQrController addQrController) {
        this.controller = addQrController;
    }

}