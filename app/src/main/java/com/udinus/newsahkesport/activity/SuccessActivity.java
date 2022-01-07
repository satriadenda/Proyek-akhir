package com.udinus.newsahkesport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.udinus.newsahkesport.R;

public class SuccessActivity  extends AppCompatActivity {
    private String user_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        getSupportActionBar().setTitle("TOKO OLAHRAGA NEWSAHKE");
        user_role = getIntent().getStringExtra("USER_ROLE");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SuccessActivity.this, MainActivity.class);
                i.putExtra("USER_ROLE", user_role);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
