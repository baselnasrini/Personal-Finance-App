package com.example.personalFinanceApp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Income implements Parcelable {

    public enum Category {
        SALARY, OTHER
    }

    private String title;
    private Date date;
    private Double amount;
    private Category category;
    private int id;

    public Income (int id, String title, Date date, Double amount, Category category){
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public static final Creator<Income> CREATOR = new Creator<Income>() {
        @Override
        public Income createFromParcel(Parcel in) {
            return new Income(in);
        }

        @Override
        public Income[] newArray(int size) {
            return new Income[size];
        }
    };

    protected Income(Parcel in) {
        title = in.readString();
        amount = in.readDouble();
        id = in.readInt();
    }

}
