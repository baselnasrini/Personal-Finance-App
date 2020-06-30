package com.example.personalFinanceApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.personalFinanceApp.Expense;
import com.example.personalFinanceApp.Income;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_INCOME = "income";
    private static final String TABLE_EXPENSE = "expense";
    private static final String TABLE_QR = "qr";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "personalFinanceAppDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_INCOME_CREATE =
            "create table " + TABLE_INCOME + "(" +
                    COLUMN_ID + " integer not null primary key, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_AMOUNT + " integer not null, " +
                    COLUMN_CATEGORY + " text not null, " +
                    COLUMN_DATE + " date not null);";

    private static final String TABLE_EXPENSE_CREATE =
            "create table " + TABLE_EXPENSE + "(" +
                    COLUMN_ID + " integer not null primary key, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_AMOUNT + " integer not null, " +
                    COLUMN_CATEGORY + " text not null, " +
                    COLUMN_DATE + " date not null);";

    private static final String TABLE_QR_CREATE =
            "create table " + TABLE_QR + "(" +
                    COLUMN_ID + " text not null primary key, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_AMOUNT + " integer not null, " +
                    COLUMN_CATEGORY + " text not null);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_QR_CREATE);
        db.execSQL(TABLE_INCOME_CREATE);
        db.execSQL(TABLE_EXPENSE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QR);
        onCreate(db);
    }

    public String tableToString(SQLiteDatabase db, String tableName) {
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        tableString += cursorToString(allRows);
        return tableString;
    }

    public String cursorToString(Cursor cursor){
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }
        return cursorString;
    }

    public void addQR(String id, String title, Double amount, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.v("asd", tableToString(db,TABLE_EXPENSE));
        Log.v("asd", tableToString(db,TABLE_QR));
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, id);
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_AMOUNT, amount);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        db.insert(DatabaseHelper.TABLE_QR,"", values);
        Log.v("asd", tableToString(db,TABLE_QR));
        db.close();
    }

    public Expense getQR(String id){
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v("asd", tableToString(db,TABLE_QR));

        try {
            Expense expense = null;

            cursor = db.rawQuery("SELECT * FROM " + TABLE_QR +
                    " WHERE " + COLUMN_ID + " = '" + id + "'", null);
            if (cursor.moveToFirst()){
                int idIndex, titleIndex, amountIndex, categoryIndex;

                idIndex = cursor.getColumnIndex(COLUMN_ID);
                titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
                categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                expense = new Expense(cursor.getInt(idIndex), cursor.getString(titleIndex), Calendar.getInstance().getTime(), cursor.getDouble(amountIndex),
                        Expense.Category.valueOf( cursor.getString(categoryIndex)));
            }
            return expense;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }
    }

    public void addIncome(Income newIncome){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, newIncome.getTitle());
        values.put(DatabaseHelper.COLUMN_AMOUNT, newIncome.getAmount());
        values.put(DatabaseHelper.COLUMN_CATEGORY, newIncome.getCategory().name());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        values.put(DatabaseHelper.COLUMN_DATE, sdf.format(newIncome.getDate()));
        db.insert(DatabaseHelper.TABLE_INCOME, "", values);
        db.close();
    }

    public void addExpense(Expense newExpense){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, newExpense.getTitle());
        values.put(DatabaseHelper.COLUMN_AMOUNT, newExpense.getAmount());
        values.put(DatabaseHelper.COLUMN_CATEGORY, newExpense.getCategory().name());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        values.put(DatabaseHelper.COLUMN_DATE, sdf.format(newExpense.getDate()));
        db.insert(DatabaseHelper.TABLE_EXPENSE, "", values);
        db.close();
    }

    public ArrayList<Integer> getIncomeCategoriesTotal(){
        ArrayList<Integer> result= new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as SALARY FROM " + TABLE_INCOME + " WHERE " + COLUMN_CATEGORY + " = 'SALARY' ", null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("SALARY"))));
            }

            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as OTHER FROM " + TABLE_INCOME + " WHERE " + COLUMN_CATEGORY + " = 'OTHER' ", null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("OTHER"))));
            }
            return result;
        } finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }

    }

    public ArrayList<Integer> getExpenseCategoriesTotal(){
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            ArrayList<Integer> result= new ArrayList<>();

            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as ACCOMMODATION FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_CATEGORY + " = 'ACCOMMODATION' ", null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("ACCOMMODATION"))));
            }

            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as TRANSPORT FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_CATEGORY + " = 'TRANSPORT' ", null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("TRANSPORT"))));
            }

            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as FOOD FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_CATEGORY + " = 'FOOD' ", null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("FOOD"))));
            }

            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as LEISURE FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_CATEGORY + " = 'LEISURE' ", null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("LEISURE"))));
            }

            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as OTHER FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_CATEGORY + " = 'OTHER' ", null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("OTHER"))));
            }
            return result;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }

    }

    public Income[] getIncomesList(){
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            int idIndex, titleIndex, amountIndex, dateIndex, categoryIndex;
            Income[] incomesList;

            cursor = db.rawQuery("SELECT * FROM " + TABLE_INCOME +
                    " ORDER BY " + COLUMN_DATE + " DESC", null);

            incomesList = new Income[cursor.getCount()];
            idIndex = cursor.getColumnIndex(COLUMN_ID);
            titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
            dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

            for (int i = 0; i < incomesList.length; i++) {
                cursor.moveToPosition(i);
                String dateString = cursor.getString(dateIndex);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                incomesList[i] = new Income(cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        date,
                        cursor.getDouble(amountIndex),
                        Income.Category.valueOf( cursor.getString(categoryIndex)) );
            }
            return incomesList;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }

    }

    public Expense[] getExpenseList(){
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            int idIndex, titleIndex, amountIndex, dateIndex, categoryIndex;
            Expense[] expenseList;
            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE +
                    " ORDER BY " + COLUMN_DATE + " DESC", null);

            expenseList = new Expense[cursor.getCount()];
            idIndex = cursor.getColumnIndex(COLUMN_ID);
            titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
            dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

            for (int i = 0; i < expenseList.length; i++) {
                cursor.moveToPosition(i);
                String dateString = cursor.getString(dateIndex);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                expenseList[i] = new Expense(cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        date,
                        cursor.getDouble(amountIndex),
                        Expense.Category.valueOf( cursor.getString(categoryIndex)) );
            }
            return expenseList;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }

    }

    public void deleteIncome(Income currentIncome) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_INCOME, DatabaseHelper.COLUMN_ID + "=" + currentIncome.getId(), null);
        db.close();
    }

    public void deleteExpense(Expense currentExpense) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_EXPENSE, DatabaseHelper.COLUMN_ID + "=" + currentExpense.getId(), null);
        db.close();
    }

    public ArrayList<Integer> getSummary() {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            ArrayList<Integer> result = new ArrayList<>();

            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as INCOME FROM " + TABLE_INCOME , null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("INCOME"))));
            }

            cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT +
                    ") as EXPENSE FROM " + TABLE_EXPENSE , null);

            if (cursor.moveToFirst()) {
                result.add( (int) Math.round(cursor.getDouble(cursor.getColumnIndex("EXPENSE"))));
            }

            return result;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }

    }

    public Income[] getFilteredFromIncomes(Date fromDate) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            int idIndex, titleIndex, amountIndex, dateIndex, categoryIndex;
            Income[] incomesList;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            cursor = db.rawQuery("SELECT * FROM " + TABLE_INCOME + " WHERE " + COLUMN_DATE + " >= date('" + dateFormat.format(fromDate) +"') ORDER BY " + COLUMN_DATE + " DESC", null);

            incomesList = new Income[cursor.getCount()];
            idIndex = cursor.getColumnIndex(COLUMN_ID);
            titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
            dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

            for (int i = 0; i < incomesList.length; i++) {
                cursor.moveToPosition(i);
                String dateString = cursor.getString(dateIndex);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                incomesList[i] = new Income(cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        date,
                        cursor.getDouble(amountIndex),
                        Income.Category.valueOf( cursor.getString(categoryIndex)) );
            }
            return incomesList;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }

    }

    public Income[] getFilteredToIncomes(Date toDate) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            int idIndex, titleIndex, amountIndex, dateIndex, categoryIndex;
            Income[] incomesList;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            cursor = db.rawQuery("SELECT * FROM " + TABLE_INCOME + " WHERE " + COLUMN_DATE + " <= date('" + dateFormat.format(toDate) +"') ORDER BY " + COLUMN_DATE + " DESC", null);

            incomesList = new Income[cursor.getCount()];
            idIndex = cursor.getColumnIndex(COLUMN_ID);
            titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
            dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

            for (int i = 0; i < incomesList.length; i++) {
                cursor.moveToPosition(i);
                String dateString = cursor.getString(dateIndex);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                incomesList[i] = new Income(cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        date,
                        cursor.getDouble(amountIndex),
                        Income.Category.valueOf( cursor.getString(categoryIndex)) );
            }
            return incomesList;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }

    }

    public Income[] getFilteredBetweenIncomes(Date fromDate, Date toDate) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            int idIndex, titleIndex, amountIndex, dateIndex, categoryIndex;
            Income[] incomesList;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            cursor = db.rawQuery("SELECT * FROM " + TABLE_INCOME + " WHERE " + COLUMN_DATE + " BETWEEN '" + dateFormat.format(fromDate) +"' AND '" + dateFormat.format(toDate) +"' ORDER BY " + COLUMN_DATE + " DESC", null);

            incomesList = new Income[cursor.getCount()];
            idIndex = cursor.getColumnIndex(COLUMN_ID);
            titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
            dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

            for (int i = 0; i < incomesList.length; i++) {
                cursor.moveToPosition(i);
                String dateString = cursor.getString(dateIndex);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                incomesList[i] = new Income(cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        date,
                        cursor.getDouble(amountIndex),
                        Income.Category.valueOf( cursor.getString(categoryIndex)) );
            }
            return incomesList;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }
    }

    public Expense[] getFilteredBetweenExpenses(Date fromDate, Date toDate) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            int idIndex, titleIndex, amountIndex, dateIndex, categoryIndex;
            Expense[] expenseList;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_DATE + " BETWEEN '" + dateFormat.format(fromDate) +"' AND '" + dateFormat.format(toDate) +"' ORDER BY " + COLUMN_DATE + " DESC", null);

            expenseList = new Expense[cursor.getCount()];
            idIndex = cursor.getColumnIndex(COLUMN_ID);
            titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
            dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

            for (int i = 0; i < expenseList.length; i++) {
                cursor.moveToPosition(i);
                String dateString = cursor.getString(dateIndex);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                expenseList[i] = new Expense(cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        date,
                        cursor.getDouble(amountIndex),
                        Expense.Category.valueOf( cursor.getString(categoryIndex)) );
            }
            return expenseList;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }
    }

    public Expense[] getFilteredFromExpenses(Date fromDate) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            int idIndex, titleIndex, amountIndex, dateIndex, categoryIndex;
            Expense[] expenseList;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_DATE + " >= date('" + dateFormat.format(fromDate) +"') ORDER BY " + COLUMN_DATE + " DESC", null);

            expenseList = new Expense[cursor.getCount()];
            idIndex = cursor.getColumnIndex(COLUMN_ID);
            titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
            dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

            for (int i = 0; i < expenseList.length; i++) {
                cursor.moveToPosition(i);
                String dateString = cursor.getString(dateIndex);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                expenseList[i] = new Expense(cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        date,
                        cursor.getDouble(amountIndex),
                        Expense.Category.valueOf( cursor.getString(categoryIndex)) );
            }
            return expenseList;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }
    }

    public Expense[] getFilteredToExpenses(Date toDate) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            int idIndex, titleIndex, amountIndex, dateIndex, categoryIndex;
            Expense[] expenseList;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_DATE + " <= date('" + dateFormat.format(toDate) +"') ORDER BY " + COLUMN_DATE + " DESC", null);

            expenseList = new Expense[cursor.getCount()];
            idIndex = cursor.getColumnIndex(COLUMN_ID);
            titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
            dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

            for (int i = 0; i < expenseList.length; i++) {
                cursor.moveToPosition(i);
                String dateString = cursor.getString(dateIndex);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                expenseList[i] = new Expense(cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        date,
                        cursor.getDouble(amountIndex),
                        Expense.Category.valueOf( cursor.getString(categoryIndex)) );
            }
            return expenseList;
        }finally {
            if(cursor != null) { cursor.close(); }
            db.close();
        }
    }
}
