package com.web.consultpin.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.web.consultpin.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

    }
}