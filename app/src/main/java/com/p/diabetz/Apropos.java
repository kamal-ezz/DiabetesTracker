package com.p.diabetz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Apropos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apropos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
