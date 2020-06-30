package com.example.personalFinanceApp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.controllers.ExpenseDetailController;

public class ExpenseDetailFragment extends Fragment {
    private TextView tvTitle, tvAmount, tvDate, tvCategory;
    private String strTitle, strAmount, strDate, staCategory;
    private Button btnDelete;
    private ExpenseDetailController controller;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_detail, container, false);
        initializeComponents(view);

        setRetainInstance(true);

        return view;
    }

    private void initializeComponents(View view) {
        tvTitle = view.findViewById(R.id.txt_detail_title);
        tvAmount = view.findViewById(R.id.txt_detail_amount);
        tvDate = view.findViewById(R.id.txt_detail_date);
        tvCategory = view.findViewById(R.id.txt_detail_category);
        btnDelete = view.findViewById(R.id.btn_detail_delete);
        tvTitle.setText(strTitle);
        tvAmount.setText(strAmount);
        tvDate.setText(strDate);
        tvCategory.setText(staCategory);

        btnDelete.setOnClickListener(new DeleteExpenseListener());

    }

    public void setExpenseData(String title, String amount, String date, String category){
        this.strTitle = title;
        this.strAmount = amount;
        this.strDate = date;
        this.staCategory = category;
    }

    public void setController(ExpenseDetailController controller){
        this.controller = controller;
    }

    private class DeleteExpenseListener implements View.OnClickListener {
        public void onClick(View v) {
            controller.deleteExpense();
        }
    }

}
