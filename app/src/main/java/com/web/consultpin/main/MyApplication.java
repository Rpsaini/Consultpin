package com.web.consultpin.main;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import com.app.preferences.SavePreferences;
import com.google.firebase.FirebaseApp;
import com.web.consultpin.R;

import fontspackageForTextView.DefineYourAppFont;

public class MyApplication extends MultiDexApplication {

    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mInstance;




    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SavePreferences.prefName=getResources().getString(R.string.app_name);
        FirebaseApp.initializeApp(this);
        DefineYourAppFont.fontNameRegular="fonts/OpenSans-Regular.ttf";
        DefineYourAppFont.fontNameBold="fonts/OpenSans-Bold.ttf";
        DefineYourAppFont.fontNameBoldExtra="fonts/OpenSans-ExtraBold.ttf";
        DefineYourAppFont.fontNameItalic="fonts/OpenSans-Italic.ttf";
        DefineYourAppFont.fontNameBoldItalic="OpenSans-BoldItalic.ttf";
        DefineYourAppFont.fontNameLiteItalic="fonts/OpenSans-LightItalic.ttf";
        DefineYourAppFont.fontNameBoldMedium="fonts/OpenSans-SemiBold.ttf";


        mInstance = this;

    }





    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}