package com.web.consultpin.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.web.consultpin.R;

public class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    public void removeActionBar()
    {
        getSupportActionBar().hide();
    }
}