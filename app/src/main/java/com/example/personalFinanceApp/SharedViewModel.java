package com.example.personalFinanceApp;

import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<ArrayAdapter<Income>> incomeAdapter;
    private MutableLiveData<Boolean> isIncomeFilterTo, isIncomeFilterFrom;
    private MutableLiveData<Date> incomeFilterToDate, incomeFilterFromDate;
    private MutableLiveData<ArrayAdapter<Expense>> expenseAdapter;
    private MutableLiveData<Boolean> isExpenseFilterTo, isExpenseFilterFrom;
    private MutableLiveData<Date> expenseFilterToDate, expenseFilterFromDate;

    public SharedViewModel () {
        incomeAdapter = new MutableLiveData<>();
        isIncomeFilterTo = new MutableLiveData<>();
        isIncomeFilterFrom = new MutableLiveData<>();
        incomeFilterToDate = new MutableLiveData<>();
        incomeFilterFromDate = new MutableLiveData<>();
        expenseAdapter = new MutableLiveData<>();
        isExpenseFilterTo = new MutableLiveData<>();
        isExpenseFilterFrom = new MutableLiveData<>();
        expenseFilterToDate = new MutableLiveData<>();
        expenseFilterFromDate = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayAdapter<Income>> getIncomeAdapter() {
        return incomeAdapter;
    }

    public void setIncomeAdapter(ArrayAdapter<Income> incomeAdapter) {
        this.incomeAdapter.setValue(incomeAdapter);
        this.incomeAdapter.postValue(incomeAdapter);
    }


    public MutableLiveData<Boolean> getIsIncomeFilterTo() {
        return isIncomeFilterTo;
    }

    public void setIsIncomeFilterTo(Boolean isIncomeFilterTo) {
        this.isIncomeFilterTo.setValue(isIncomeFilterTo);
        this.isIncomeFilterTo.postValue(isIncomeFilterTo);

    }

    public MutableLiveData<Boolean> getIsIncomeFilterFrom() {
        return isIncomeFilterFrom;
    }

    public void setIsIncomeFilterFrom(Boolean isIncomeFilterFrom) {
        this.isIncomeFilterFrom.setValue(isIncomeFilterFrom);
        this.isIncomeFilterFrom.postValue(isIncomeFilterFrom);

    }

    public MutableLiveData<Date> getIncomeFilterToDate() {
        return incomeFilterToDate;
    }

    public void setIncomeFilterToDate(Date incomeFilterToDate) {
        this.incomeFilterToDate.setValue(incomeFilterToDate);
        this.incomeFilterToDate.postValue(incomeFilterToDate);
    }

    public MutableLiveData<Date> getIncomeFilterFromDate() {
        return incomeFilterFromDate;
    }

    public void setIncomeFilterFromDate(Date incomeFilterFromDate) {
        this.incomeFilterFromDate.setValue(incomeFilterFromDate);
        this.incomeFilterFromDate.postValue(incomeFilterFromDate);
    }

    public MutableLiveData<ArrayAdapter<Expense>> getExpenseAdapter() {
        return expenseAdapter;
    }

    public void setExpenseAdapter(ArrayAdapter<Expense> expenseAdapter) {
        this.expenseAdapter.setValue(expenseAdapter);
        this.expenseAdapter.postValue(expenseAdapter);
    }


    public MutableLiveData<Boolean> getIsExpenseFilterTo() {
        return isExpenseFilterTo;
    }

    public void setIsExpenseFilterTo(Boolean isExpenseFilterTo) {
        this.isExpenseFilterTo.setValue(isExpenseFilterTo);
        this.isExpenseFilterTo.postValue(isExpenseFilterTo);

    }

    public MutableLiveData<Boolean> getIsExpenseFilterFrom() {
        return isExpenseFilterFrom;
    }

    public void setIsExpenseFilterFrom(Boolean isExpenseFilterFrom) {
        this.isExpenseFilterFrom.setValue(isExpenseFilterFrom);
        this.isExpenseFilterFrom.postValue(isExpenseFilterFrom);

    }

    public MutableLiveData<Date> getExpenseFilterToDate() {
        return expenseFilterToDate;
    }

    public void setExpenseFilterToDate(Date expenseFilterToDate) {
        this.expenseFilterToDate.setValue(expenseFilterToDate);
        this.expenseFilterToDate.postValue(expenseFilterToDate);
    }

    public MutableLiveData<Date> getExpenseFilterFromDate() {
        return expenseFilterFromDate;
    }

    public void setExpenseFilterFromDate(Date expenseFilterFromDate) {
        this.expenseFilterFromDate.setValue(expenseFilterFromDate);
        this.expenseFilterFromDate.postValue(expenseFilterFromDate);
    }
}
