package com.example.personalFinanceApp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Expense implements Parcelable {

    public enum Category {
        FOOD, LEISURE, TRANSPORT, ACCOMMODATION, OTHER
    }

    private String title;
    private Date date;
    private Double amount;
    private Category category;
    private int id;

    public Expense (int id,String title, Date date, Double amount, Category category){
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.category = category;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId(){
        return this.id;
    }

    public void setTd(int id){
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(date.getTime());
        dest.writeDouble(amount);
        dest.writeString(category.name());
        dest.writeInt(id);
    }

    protected Expense(Parcel in) {
        title = in.readString();
        amount = in.readDouble();
        id = in.readInt();
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

}
