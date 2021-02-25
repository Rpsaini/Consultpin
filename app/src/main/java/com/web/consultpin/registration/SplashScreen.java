package com.web.consultpin.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.main.BaseActivity;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
        changestatusBarColor(0);
        removeActionBar();
        initiateObj();
//
//        String userid=savePreferences.reterivePreference(SplashScreen.this, Utilclass.user_id).toString();
//        System.out.println("USer id===>"+userid);
//        if(userid.equalsIgnoreCase("0"))
//        {
            Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
            startActivity(intent);
            finish();
//        }
//        else
//        {
//            Intent intent=new Intent(SplashScreen.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }
}