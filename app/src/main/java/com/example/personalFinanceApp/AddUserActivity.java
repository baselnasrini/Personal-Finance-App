package com.example.personalFinanceApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddUserActivity extends AppCompatActivity {
    EditText txtUsername;
    Button btnSave;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_user);
        txtUsername = findViewById(R.id.txt_username);
        btnSave = findViewById(R.id.btn_save);

        this.username = getIntent().getStringExtra("USERNAME");

        if (this.username != null){
            txtUsername.setText(this.username);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getUsername()){
                    Intent result = new Intent();
                    result.putExtra("USERNAME", username);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
            }
        });
    }

    public boolean getUsername() {
        this.username = txtUsername.getText().toString().trim();
        if (this.username.length() > 0){
            return true;
        }
        return false;
    }
}
