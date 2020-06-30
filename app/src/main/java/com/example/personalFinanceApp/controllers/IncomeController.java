package com.example.personalFinanceApp.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalFinanceApp.Income;
import com.example.personalFinanceApp.MainActivity;
import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.SharedViewModel;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.example.personalFinanceApp.ui.IncomeFragment;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class IncomeController {
    private IncomeFragment incomeFragment;
    private MainActivity activity;
    private DatabaseHelper databaseHelper;
    private Income[] incomesList;
    private Boolean filterFrom = false;
    private Boolean filterTo = false;
    private Date fromDate ;
    private Date toDate ;
    private SharedViewModel sharedViewModel;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public IncomeController(MainActivity activity){
        this.activity = activity;
        this.databaseHelper = new DatabaseHelper(activity);

        this.sharedViewModel = ViewModelProviders.of(this.activity).get(SharedViewModel.class);

        this.incomeFragment = new IncomeFragment();
        incomeFragment.setController(this);

        sharedViewModel.getIncomeFilterFromDate().observe(this.activity, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                fromDate = date;
            }
        });

        sharedViewModel.getIncomeFilterToDate().observe(this.activity, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                toDate = date;
            }
        });

        sharedViewModel.getIsIncomeFilterFrom().observe(this.activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                filterFrom = aBoolean;
                if (aBoolean)
                    updateList();
            }
        });

        sharedViewModel.getIsIncomeFilterTo().observe(this.activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                filterTo = aBoolean;
                if (aBoolean)
                    updateList();
            }
        });

        updateList();

    }

    public IncomeFragment getFragment (){
        return this.incomeFragment;
    }

    public void addIncomeFragment() {
        AddIncomeController addFragContr = new AddIncomeController(this);
        FragmentManager fragmentManager = ((MainActivity)this.getFragment().getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, addFragContr.getFragment());
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void goBack(){
        FragmentManager fragmentManager = ((MainActivity)this.getFragment().getContext()).getSupportFragmentManager();
        fragmentManager.popBackStack();
        updateList();
    }

    public void updateList(){
        if (filterTo == null){
            filterTo = false;
        }
        if (filterFrom == null){
            filterFrom = false;
        }

        if(filterTo && filterFrom){
            this.incomesList = databaseHelper.getFilteredBetweenIncomes(fromDate, toDate);
        } else if(filterFrom){
            this.incomesList = databaseHelper.getFilteredFromIncomes(fromDate);
        }else if(filterTo){
            this.incomesList = databaseHelper.getFilteredToIncomes(toDate);
        } else {
            this.incomesList = databaseHelper.getIncomesList();
        }
        sharedViewModel.setIncomeAdapter(new IncomeAdapter(activity, this.incomesList));
    }

    public void showIncomeDetail(Income clickedIncome) {
        IncomeDetailController detailFragCont = new IncomeDetailController(this, clickedIncome);
        FragmentManager fragmentManager = ((MainActivity)this.getFragment().getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailFragCont.getFragment());
        fragmentTransaction.addToBackStack(null).commit();
    }

    public BarData getCharData() {
        ArrayList<Integer> DB = databaseHelper.getIncomeCategoriesTotal();

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(DB.get(0) , 0 ));
        entries.add(new BarEntry(DB.get(1) , 1 ));

        BarDataSet bardataset = new BarDataSet(entries, "Income");
        bardataset.setColors(ColorTemplate.PASTEL_COLORS);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Salary");
        labels.add("Other");

        BarData data = new BarData(labels, bardataset);
        data.setDrawValues(true);

        return data;
    }

    public void setFilterFrom(String date) {
        filterFrom = true;
        try {
            fromDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.sharedViewModel.setIncomeFilterFromDate(fromDate);
        this.sharedViewModel.setIsIncomeFilterFrom(true);
        updateList();
    }

    public boolean getFilterFrom(){
        if (filterFrom == null)
            filterFrom = false;
        return filterFrom;
    }

    public boolean getFilterTo(){
        if (filterTo == null)
            filterTo = false;
        return filterTo;
    }

    public String getFromDate(){
        String str = null;
        if (fromDate != null){
            str = formatter.format(fromDate);
        }
        return str;
    }

    public String getToDate(){
        String str = null;
        if (toDate != null){
            str = formatter.format(toDate);
        }
        return str;
    }

    public void setFilterTo(String date) {
        filterTo = true;

        try {
            toDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.sharedViewModel.setIncomeFilterToDate(toDate);
        this.sharedViewModel.setIsIncomeFilterTo(true);
        updateList();
    }

    public void clearFilters(){
        filterTo = false;
        filterFrom = false;
        toDate = null;
        fromDate = null;
        this.sharedViewModel.setIsIncomeFilterTo(false);
        this.sharedViewModel.setIsIncomeFilterFrom(false);

        updateList();
    }

    public String getFilterText(){
        String str = null;

        if(filterTo && filterFrom){
            str = "Filtered between " + formatter.format(fromDate) + " and " + formatter.format(toDate);
        } else if(filterFrom){
            str = "Filtered From " + formatter.format(fromDate);
        }else if(filterTo) {
            str = "Filtered To " + formatter.format(toDate);
        }
        return str;
    }

    private class IncomeAdapter extends ArrayAdapter<Income> {
        private LayoutInflater inflater;

        public IncomeAdapter(Context context, Income[] incomeList) {
            super(context,R.layout.listview_row,incomeList);
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Income income = getItem(position);
            ViewHolder holder;
            if(convertView==null) {
                convertView = inflater.inflate(R.layout.listview_row,parent,false);
                holder = new ViewHolder();
                holder.tvTitle = (TextView)convertView.findViewById(R.id.row_txtTitle);
                holder.tvAmount = (TextView)convertView.findViewById(R.id.row_txtAmount);
                holder.tvDate = (TextView)convertView.findViewById(R.id.row_txtDate);
                holder.image = (ImageView)convertView.findViewById(R.id.row_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            String title = income.getTitle();
            holder.tvTitle.setText(title.substring(0 , Math.min(title.length(), 25)));

            Double incomeDouble = income.getAmount();
            DecimalFormat df = new DecimalFormat("#0.0");
            holder.tvAmount.setText(df.format(incomeDouble));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            holder.tvDate.setText(dateFormat.format(income.getDate()));
            switch (income.getCategory()){
                case SALARY: holder.image.setImageResource(R.drawable.ic_salary); break;
                case OTHER: holder.image.setImageResource(R.drawable.ic_wallet); break;
            }
            return convertView;
        }

        private class ViewHolder {
            private TextView tvTitle;
            private TextView tvDate;
            private ImageView image;
            private TextView tvAmount;
        }
    }


}
