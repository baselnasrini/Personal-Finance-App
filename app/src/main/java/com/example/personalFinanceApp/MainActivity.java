package com.example.personalFinanceApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalFinanceApp.controllers.AddQrController;
import com.example.personalFinanceApp.controllers.ExpenseController;
import com.example.personalFinanceApp.controllers.IncomeController;
import com.example.personalFinanceApp.controllers.HomeController;
import com.example.personalFinanceApp.database.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private HomeController homeController;
    private IncomeController incomeController;
    private ExpenseController expenseController;
    private BottomNavigationView navView;
    private final static int SET_USERNAME = 1;
    public final static int ADD_NEW_QR = 2;
    public final static int SCAN_QR = 3;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences= getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        username= sharedPreferences.getString("username",null);

        if(username == null){
            Intent intent= new Intent( MainActivity.this, AddUserActivity.class);
            startActivityForResult(intent, SET_USERNAME);
        }


        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);


        homeController = new HomeController(this);
        incomeController = new IncomeController(this);
        expenseController = new ExpenseController(this);

        if(savedInstanceState == null){
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.btn_change_user){
            Intent intent= new Intent( MainActivity.this, AddUserActivity.class);
            intent.putExtra( "USERNAME", this.username );
            startActivityForResult(intent, SET_USERNAME);
        } else if (item.getItemId() == R.id.btn_add_qr){
            navView.setSelectedItemId(R.id.navigation_home);
            AddQrController qrController = new AddQrController(this);
            qrController.showFragment();
            IntentIntegrator qrScan = new IntentIntegrator(this);
            qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            qrScan.setPrompt("Scan a QR Code to add a new QR Code value.");
            qrScan.setCaptureActivity(CaptureActivityPortrait.class);
            qrScan.setOrientationLocked(false);
            Intent intent = qrScan.createScanIntent();
            startActivityForResult(intent, ADD_NEW_QR);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences= getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(resultCode==Activity.RESULT_OK && requestCode== SET_USERNAME ) {
            this.username = data.getStringExtra("USERNAME");
            editor.putString("username",this.username);
            editor.apply();
            homeController = new HomeController(this);
            navView.setSelectedItemId(R.id.navigation_home);
            homeController.setTexts();
        } else if (resultCode==Activity.RESULT_CANCELED && requestCode== SET_USERNAME){
            if (this.username != null){
                navView.setSelectedItemId(R.id.navigation_home);
            } else {
                this.finish();
            }
        } else {

            if(requestCode == ADD_NEW_QR){
                if(resultCode==Activity.RESULT_CANCELED){
                    Toast.makeText(this, "QR code Not Found", Toast.LENGTH_LONG).show();
                } else if (resultCode==Activity.RESULT_OK){
                    DatabaseHelper databaseHelper = new DatabaseHelper(this);
                    String scannedId = data.getStringExtra("SCAN_RESULT");

                    if (databaseHelper.getQR(scannedId) == null){
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        TextView tvQrId = fragment.getView().findViewById(R.id.txt_add_qr_id);
                        tvQrId.setText(scannedId);
                    } else{
                        Toast.makeText(this, "Scanned QR already exist in the database, try another code.", Toast.LENGTH_LONG).show();
                    }

                }
            } else if(requestCode == SCAN_QR){
                    if(resultCode==Activity.RESULT_CANCELED) {
                        Toast.makeText(this, "QR code Not Found", Toast.LENGTH_LONG).show();
                    } else if (resultCode==Activity.RESULT_OK){
                        DatabaseHelper databaseHelper = new DatabaseHelper(this);
                        String scannedId = data.getStringExtra("SCAN_RESULT");
                        Expense result= databaseHelper.getQR(scannedId);
                        if (result != null){
                            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            View view = fragment.getView();
                            TextView tvTitle = view.findViewById(R.id.expense_txtTitle);
                            TextView amount = view.findViewById(R.id.expense_txtAmount);
                            Spinner categorySpinner = view.findViewById(R.id.spin_category);

                            tvTitle.setText(result.getTitle());
                            DecimalFormat df = new DecimalFormat("0.##");
                            amount.setText("" + result.getAmount());
                            categorySpinner.setSelection(getSpinnerIndex(categorySpinner, result.getCategory().name()));
                        } else{
                            Toast.makeText(this, "QR code value doesn't exist in the database", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        }

    }

    private int getSpinnerIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return -1;
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (!fragmentManager.isDestroyed()){
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();
        }
    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navigation_home:
                            homeController.setTexts();
                            setFragment( homeController.getFragment());
                            return true;
                        case R.id.navigation_income:
                            setFragment( incomeController.getFragment());
                            return true;
                        case R.id.navigation_expense:
                            setFragment( expenseController.getFragment());
                            return true;
                    }
                    return true;
                }
            };

}