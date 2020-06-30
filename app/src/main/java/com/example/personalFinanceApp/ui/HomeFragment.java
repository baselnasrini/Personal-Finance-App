package com.example.personalFinanceApp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.personalFinanceApp.R;
import com.example.personalFinanceApp.controllers.HomeController;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;

public class HomeFragment extends Fragment {
    private TextView tv_title;
    private TextView tv_summary;
    private HomeController controller;
    private String titleStr;
    private String summaryStr;
    private PieChart pieChart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        tv_title = view.findViewById(R.id.txt_homeTitle);
        tv_title.setText( this.titleStr);
        tv_summary = view.findViewById(R.id.txt_homeSummary);
        tv_summary.setText( this.summaryStr);
        pieChart = view.findViewById(R.id.pieChart_Home);
        setHomeChart();

    }

    public void setController(HomeController controller){
        this.controller = controller;
    }

    public void setText(String title, String summary){
        this.titleStr = title;
        this.summaryStr = summary;
    }

    public void setHomeChart(){
        PieData data = this.controller.setChartData();
        if (data != null){
        pieChart.setData(data);
        pieChart.animateXY(500, 500);
        pieChart.invalidate();
        }
    }


}
