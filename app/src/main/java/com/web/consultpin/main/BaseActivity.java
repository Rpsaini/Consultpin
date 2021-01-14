package com.web.consultpin.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.app.dialogsnpickers.AlertDialogs;
import com.app.preferences.SavePreferences;
import com.app.validation.ValidationRule;
import com.app.vollycommunicationlib.ServerHandler;
import com.app.vollycommunicationlib.UtilClass;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;

import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity
{
    public SavePreferences savePreferences;
    public ServerHandler serverHandler;
    public AlertDialogs alertDialogs;
    public ValidationRule validationRule;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

    }

    public void  initiateObj()
    {
        savePreferences=new SavePreferences();
        serverHandler=new ServerHandler();
        alertDialogs=new AlertDialogs();
        validationRule=new ValidationRule();
        changestatusBarColor(0);
    }
    public void removeActionBar()
    {
        getSupportActionBar().hide();
    }


    public void changestatusBarColor(int type)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.border_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.border_color));
        }
    }
    public String getAppVersion()
    {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";
    }

    public String getDeviceToken()
    {
        return savePreferences.reterivePreference(BaseActivity.this, Utilclass.device_Token)+"";

    }


    public String getRestParamsName(String keyname)
    {
        try {
            JSONObject dataObj = new JSONObject(savePreferences.reterivePreference(this, "data") + "");
            System.out.println("data==="+dataObj);
            return dataObj.getString(keyname);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "0";
    }


    //Also change qr code validation url
    public String getApiUrl()
    {
        return "http://52.66.238.215/v1/";
    }

    public String getXapiKey()
    {
        return  "7XzxHOqJ0NcyWvMSjIv9";
    }



    public void addClickEventEffet(final View viwView)
    {
        viwView.setAlpha(.5f);
        Handler hnd=new Handler();
        hnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                viwView.setAlpha(1f);
            }
        },100);
    }

    public void hideShowPassword(int x, EditText editText)
    {
        if(x==1)
        {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//show
        }
        else
        {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }



    }
}