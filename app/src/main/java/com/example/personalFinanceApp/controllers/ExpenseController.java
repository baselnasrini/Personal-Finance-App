package com.example.personalFinanceApp.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalFinanceApp.Expense;
import com.example.personalFinanceApp.MainActivity;
import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.SharedViewModel;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.example.personalFinanceApp.ui.ExpenseFragment;
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

public class ExpenseController {
    private ExpenseFragment expenseFragment;
    private MainActivity activity;
    private DatabaseHelper databaseHelper;
    private Expense[] expensesList;
    private Boolean filterFrom = false;
    private Boolean filterTo = false;
    private Date fromDate ;
    private Date toDate ;
    private SharedViewModel sharedViewModel;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ExpenseController(MainActivity activity){
        this.activity = activity;
        this.databaseHelper = new DatabaseHelper(activity);

        this.sharedViewModel = ViewModelProviders.of(this.activity).get(SharedViewModel.class);

        this.expenseFragment = new ExpenseFragment();
        expenseFragment.setController(this);

        sharedViewModel.getExpenseFilterFromDate().observe(this.activity, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                fromDate = date;
            }
        });

        sharedViewModel.getExpenseFilterToDate().observe(this.activity, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                toDate = date;
            }
        });

        sharedViewModel.getIsExpenseFilterFrom().observe(this.activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                filterFrom = aBoolean;
                if (aBoolean)
                    updateList();
            }
        });

        sharedViewModel.getIsExpenseFilterTo().observe(this.activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                filterTo = aBoolean;
                if (aBoolean)
                    updateList();
            }
        });
        updateList();
    }

    public ExpenseFragment getFragment (){
        return this.expenseFragment;
    }

    public void addExpenseFragment() {
        AddExpenseController addFragContr = new AddExpenseController(this);
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
            this.expensesList = databaseHelper.getFilteredBetweenExpenses(fromDate, toDate);
        } else if(filterFrom){
            this.expensesList = databaseHelper.getFilteredFromExpenses(fromDate);
        }else if(filterTo){
            this.expensesList = databaseHelper.getFilteredToExpenses(toDate);
        } else {
            this.expensesList = databaseHelper.getExpenseList();
        }
        sharedViewModel.setExpenseAdapter(new ExpenseAdapter(activity, this.expensesList));
    }

    public void showExpenseDetail(Expense clickedExpense) {
        ExpenseDetailController detailFragCont = new ExpenseDetailController(this, clickedExpense);
        FragmentManager fragmentManager = ((MainActivity)this.getFragment().getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailFragCont.getFragment());
        fragmentTransaction.addToBackStack(null).commit();
    }


    public BarData getCharData() {
        ArrayList<Integer> DB = databaseHelper.getExpenseCategoriesTotal();

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(DB.get(0) , 0 ));
        entries.add(new BarEntry(DB.get(1) , 1 ));
        entries.add(new BarEntry(DB.get(2) , 2 ));
        entries.add(new BarEntry(DB.get(3) , 3 ));
        entries.add(new BarEntry(DB.get(4) , 4 ));

        BarDataSet bardataset = new BarDataSet(entries, "Expense");
        bardataset.setColors(ColorTemplate.PASTEL_COLORS);
        bardataset.setBarSpacePercent(60f);
        bardataset.setDrawValues(false);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Accomo.");
        labels.add("Transp.");
        labels.add("Food");
        labels.add("Leisure");
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
        this.sharedViewModel.setExpenseFilterFromDate(fromDate);
        this.sharedViewModel.setIsExpenseFilterFrom(true);
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
        this.sharedViewModel.setExpenseFilterToDate(toDate);
        this.sharedViewModel.setIsExpenseFilterTo(true);
        updateList();
    }

    public void clearFilters(){
        filterTo = false;
        filterFrom = false;
        toDate = null;
        fromDate = null;
        this.sharedViewModel.setIsExpenseFilterTo(false);
        this.sharedViewModel.setIsExpenseFilterFrom(false);
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

    private class ExpenseAdapter extends ArrayAdapter<Expense> {
        private LayoutInflater inflater;

        public ExpenseAdapter(Context context, Expense[] expenseList) {
            super(context, R.layout.listview_row,expenseList);
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Expense expense = getItem(position);
            ViewHolder holder;
            if(convertView==null) {
                convertView = inflater.inflate(R.layout.listview_row,parent,false);
                holder = new ViewHolder();
                holder.tvTitle = convertView.findViewById(R.id.row_txtTitle);
                holder.tvAmount = convertView.findViewById(R.id.row_txtAmount);
                holder.tvDate = convertView.findViewById(R.id.row_txtDate);
                holder.image = convertView.findViewById(R.id.row_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            String title = expense.getTitle();
            holder.tvTitle.setText(title.substring(0 , Math.min(title.length(), 25)));

            Double expenseDouble = expense.getAmount() * -1;
            DecimalFormat df = new DecimalFormat("#0.0");
            holder.tvAmount.setText( df.format(expenseDouble));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            holder.tvDate.setText(dateFormat.format(expense.getDate()));
            switch (expense.getCategory()){
                case TRANSPORT: holder.image.setImageResource(R.drawable.ic_transport); break;
                case ACCOMMODATION: holder.image.setImageResource(R.drawable.ic_accomodation); break;
                case LEISURE: holder.image.setImageResource(R.drawable.ic_leisure); break;
                case FOOD: holder.image.setImageResource(R.drawable.ic_food); break;
                case OTHER: holder.image.setImageResource(R.drawable.ic_other); break;
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
