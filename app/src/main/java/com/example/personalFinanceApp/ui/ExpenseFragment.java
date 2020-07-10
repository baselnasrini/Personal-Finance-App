package com.example.personalFinanceApp.ui;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalFinanceApp.Expense;
import com.example.personalFinanceApp.IOnBackPressed;
import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.SharedViewModel;
import com.example.personalFinanceApp.controllers.ExpenseController;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ExpenseFragment extends Fragment implements IOnBackPressed {
    private ListView lvExpense;
    private FloatingActionButton newExpenseBtn, filterBtn, clearBtn;
    private ExpenseController controller;
    private BarChart barChart;
    private TextView filterTextView, noExpenseTv;
    private ConstraintLayout layout;
    private PopupWindow popupWindow;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Date date;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        initializeComponents(view);
        registerListeners();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setRetainInstance(true);
        return view;
    }

    private void initializeComponents(View view) {
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        layout = view.findViewById(R.id.expenseView);
        noExpenseTv= view.findViewById(R.id.no_expense_list);
        lvExpense = view.findViewById(R.id.lv_expense);
        newExpenseBtn = view.findViewById(R.id.btn_expense_add);
        filterBtn = view.findViewById(R.id.btn_expense_filter);
        filterTextView = view.findViewById(R.id.txt_filterExpense);
        barChart = view.findViewById(R.id.expenseChart);
        clearBtn= view.findViewById(R.id.float_btn_expense_clearFilter);
    }

    private void registerListeners() {

        lvExpense.setOnItemClickListener(new ListViewListener());

        sharedViewModel.getExpenseAdapter().observe(getViewLifecycleOwner(), new Observer<ArrayAdapter<Expense>>() {
            @Override
            public void onChanged(ArrayAdapter<Expense> expenseArrayAdapter) {
                lvExpense.setAdapter(expenseArrayAdapter);
                if(expenseArrayAdapter.getCount() == 0){
                    noExpenseTv.setVisibility(View.VISIBLE);
                } else{
                    noExpenseTv.setVisibility(View.GONE);
                }
            }
        });

        newExpenseBtn.setOnClickListener(new AddButtonListener());
        filterBtn.setOnClickListener(new FilterButtonListener());

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null)
                    controller.clearFilters();
            }
        });

        updateChart();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            barChart.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) lvExpense.getLayoutParams();
            lp.setMargins(0,370,0,0);
            lvExpense.setLayoutParams(lp);
        } else{
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) lvExpense.getLayoutParams();
            lp.setMargins(0,0,0,0);
            lvExpense.setLayoutParams(lp);
        }

        filterTextView.setVisibility(View.GONE);
        clearBtn.setVisibility(View.GONE);

        sharedViewModel.getIsExpenseFilterFrom().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean){
                    updateChart();
                    barChart.setVisibility(View.GONE);
                    filterTextView.setVisibility(View.VISIBLE);
                    clearBtn.setVisibility(View.VISIBLE);
                    filterTextView.setText(controller.getFilterText());
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) lvExpense.getLayoutParams();
                    lp.setMargins(0,80,0,0);
                    lvExpense.setLayoutParams(lp);
                }
                else if (!controller.getFilterTo()){
                    updateChart();
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        barChart.setVisibility(View.VISIBLE);
                        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) lvExpense.getLayoutParams();
                        lp.setMargins(0, 330, 0, 0);
                        lvExpense.setLayoutParams(lp);
                    } else{
                        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) lvExpense.getLayoutParams();
                        lp.setMargins(0,0,0,0);
                        lvExpense.setLayoutParams(lp);
                    }

                    filterTextView.setVisibility(View.GONE);
                    clearBtn.setVisibility(View.GONE);
                }
            }
        });

        sharedViewModel.getIsExpenseFilterTo().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean){
                    updateChart();
                    barChart.setVisibility(View.GONE);
                    filterTextView.setVisibility(View.VISIBLE);
                    clearBtn.setVisibility(View.VISIBLE);
                    filterTextView.setText(controller.getFilterText());
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) lvExpense.getLayoutParams();
                    lp.setMargins(0,80,0,0);
                    lvExpense.setLayoutParams(lp);
                }
                else if (!controller.getFilterFrom()){
                    updateChart();
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                        barChart.setVisibility(View.VISIBLE);
                        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) lvExpense.getLayoutParams();
                        lp.setMargins(0,330,0,0);
                        lvExpense.setLayoutParams(lp);
                    } else{
                        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) lvExpense.getLayoutParams();
                        lp.setMargins(0,0,0,0);
                        lvExpense.setLayoutParams(lp);
                    }

                    filterTextView.setVisibility(View.GONE);
                    clearBtn.setVisibility(View.GONE);

                }
            }
        });
    }

    @Override
    public void onPause() {
        if (popupWindow != null){
            closePopupWindow();
        }
        layout.setAlpha(1);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (popupWindow != null){
            closePopupWindow();
        }
        layout.setAlpha(1);
        super.onDestroy();
    }

    public void updateChart(){
        BarData data = this.controller.getCharData();
        barChart.setData(data);
        barChart.setDescription("");
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(1000);
    }

    public void setController(ExpenseController controller){
        this.controller = controller;
    }

    @Override
    public boolean onBackPressed() {
        if (popupWindow != null) {
            closePopupWindow();
            return true;
        }
        return false;
    }

    private class AddButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            if (popupWindow == null)
                controller.addExpenseFragment();
        }
    }

    private class ListViewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (popupWindow == null)
                controller.showExpenseDetail((Expense) parent.getItemAtPosition(position));
        }
    }

    private class FilterButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            if (popupWindow == null){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                View popupView = inflater.inflate(R.layout.popup_window, null);
                popupWindow = new PopupWindow(popupView, width, height, false);
                layout.setAlpha(0.5F);
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(false);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                ImageButton closeButton = popupView.findViewById(R.id.ib_close);
                Button setFilter = popupView.findViewById(R.id.btn_setFilter);
                Button clearFilter = popupView.findViewById(R.id.btn_clearFilter);
                final EditText filterFrom = popupView.findViewById(R.id.filter_from);
                final EditText filterTo = popupView.findViewById(R.id.filter_to);

                if (controller.getFilterFrom()){
                    filterFrom.setText(controller.getFromDate());
                }

                if (controller.getFilterTo()){
                    filterTo.setText(controller.getToDate());
                }

                closeButton.setOnClickListener(new View.OnClickListener (){
                    @Override
                    public void onClick(View v) {
                        closePopupWindow();
                    }
                });

                filterFrom.setOnClickListener(new View.OnClickListener (){
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                filterFrom.setText(formatter.format(calendar.getTime()));
                            }
                        };

                        try{
                            if (filterFrom.getText().toString().isEmpty())
                                date = new Date();
                            else{
                                date = formatter.parse(filterFrom.getText().toString());
                            }
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date);
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            new DatePickerDialog(getContext(), mDateSetListener, year, month, day).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                filterTo.setOnClickListener(new View.OnClickListener (){
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                filterTo.setText(formatter.format(calendar.getTime()));
                            }
                        };

                        try{
                            if (filterTo.getText().toString().isEmpty())
                                date = new Date();
                            else{
                                date = formatter.parse(filterTo.getText().toString());
                            }

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date);
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            new DatePickerDialog(getContext(), mDateSetListener, year, month, day).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                clearFilter.setOnClickListener(new View.OnClickListener (){
                    @Override
                    public void onClick(View v) {
                        controller.clearFilters();
                        closePopupWindow();
                    }
                });

                setFilter.setOnClickListener(new View.OnClickListener (){
                    @Override
                    public void onClick(View v) {
                        if (!filterFrom.getText().toString().isEmpty())
                            controller.setFilterFrom(filterFrom.getText().toString());

                        if (!filterTo.getText().toString().isEmpty())
                            controller.setFilterTo(filterTo.getText().toString());

                        closePopupWindow();
                    }
                });

            }
        }
    }

    private void closePopupWindow(){
        layout.setAlpha(1);
        popupWindow.dismiss();
        popupWindow = null;
    }
}
