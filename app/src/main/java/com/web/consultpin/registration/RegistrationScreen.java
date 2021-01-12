package com.web.consultpin.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.main.BaseActivity;

public class RegistrationScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        removeActionBar();
        init();
    }

    private void init()
    {
        RelativeLayout rr_register =findViewById(R.id.rr_back);
        TextView tv_register =findViewById(R.id.tv_register);

        rr_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_register.findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegistrationScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}