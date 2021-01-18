package com.web.consultpin.consultant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.web.consultpin.R;

public class BecomeAConsultant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_a_consultant);
        getSupportActionBar().hide();

        init();
    }

    private void init()
    {
        findViewById(R.id.toolbar_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbarTitle =findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.becom_consultant));
    }
}