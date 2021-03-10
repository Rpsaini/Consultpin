package com.web.consultpin.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.dialogsnpickers.AlertDialogs;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.preferences.SavePreferences;
import com.app.validation.ValidationRule;
import com.app.vollycommunicationlib.ServerHandler;
import com.app.vollycommunicationlib.UtilClass;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.BecomeAConsultant;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import animationpackage.AnimationForViews;
import animationpackage.IsAnimationEndedCallback;

public class BaseActivity extends AppCompatActivity
{
    public SavePreferences savePreferences;
    public ServerHandler serverHandler;
    public AlertDialogs alertDialogs;
    public ValidationRule validationRule;
    public AnimationForViews animationForViews;
    private int screenHeight,screenWidth;

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
        animationForViews=new AnimationForViews();
        changestatusBarColor(0);
       // getSupportActionBar().hide();
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

    public void changestatusBarColorBlue()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.buttonskyblue, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.buttonskyblue));
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

        //{"userdata":{"user_id":"39","profile_pic":"http:\/\/52.66.238.215\/assets\/uploads\/default.png",
        // "email":"mail@gmail.com","first_name":"Ram","last_name":"lastnae","phone":"9787978797"},"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMzkiLCJmaXJzdF9uYW1lIjoiUmFtIiwibGFzdF9uYW1lIjoibGFzdG5hZSIsImVtYWlsIjoibWFpbEBnbWFpbC5jb20iLCJwaG9uZSI6Ijk3ODc5Nzg3OTciLCJjcmVhdGVkX29uIjoiMTYxMDYwODkwMiIsIkFQSV9USU1FIjoxNjExNzQ0NDMwfQ.uNpffzd0ypmpVn4SWL0d-9JnDjhdTzbJwedfGcdLjAU","image_url":"http:\/\/52.66.238.215\/assets\/uploads\/","status":true}
        try {
            JSONObject dataObj = new JSONObject(savePreferences.reterivePreference(this, Utilclass.loginDetail)+"");
            JSONObject userdata=dataObj.getJSONObject("userdata");
            System.out.println("data==="+dataObj);
            System.out.println("keyname==="+keyname);
            if(keyname.equalsIgnoreCase(Utilclass.token))
            {
                return dataObj.getString(keyname);
            }
            else
            {
                return userdata.getString(keyname);
            }

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
        return Utilclass.baseUrl;
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

    public void hideShowPassword(int x, EditText editText, ImageView imageView)
    {
        if(x==1)
        {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//show
            imageView.setImageResource(R.drawable.ic_eye);
        }
        else
        {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.ic_hide_password);
        }
   }

    public void animateUp(View sourcedestinationcontainer) {
        getScreenHeight();
        animationForViews.handleAnimation(this, sourcedestinationcontainer, 500, screenHeight, 00, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
            @Override
            public void getAnimationStatus(String status) {

                switch (status) {
                    case IsAnimationEndedCallback.animationCancel: {
                        break;
                    }
                    case IsAnimationEndedCallback.animationEnd: {


                        break;
                    }

                    case IsAnimationEndedCallback.animationRepeat: {
                        break;
                    }

                    case IsAnimationEndedCallback.animationStart: {
                        break;
                    }

                }
            }
        });
    }


    public void downSourceDestinationView(View sourcedestinationcontainer, final Dialog dialog) {
        animationForViews.handleAnimation(this, sourcedestinationcontainer, 500, 00, screenHeight, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
            @Override
            public void getAnimationStatus(String status) {

                switch (status) {
                    case IsAnimationEndedCallback.animationCancel: {
                        break;
                    }
                    case IsAnimationEndedCallback.animationEnd: {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        break;
                    }

                    case IsAnimationEndedCallback.animationRepeat: {
                        break;
                    }

                    case IsAnimationEndedCallback.animationStart: {
                        break;
                    }

                }
            }
        });
    }

    private void getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


    public  void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

   public static  boolean compareTwoDates(String startDate,String  endDate)
   {
       try {
           System.out.println("Start End Date=="+startDate+"=="+endDate);
           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
           Date startDateD = simpleDateFormat.parse(startDate);
           Date endDateD = simpleDateFormat.parse(endDate);
           if (startDateD.equals(endDateD)) {
               return true;
           }
          else if (startDateD.after(endDateD)) {
               System.out.println("Date1 is after Date2");
               return true;
           }
          else if (startDateD.before(endDateD))
           {
               System.out.println("Date1 is before Date2");
               return false;
           }
        }
       catch (Exception e)
       {
           e.printStackTrace();
       }
       return false;
   }


    public static  boolean compareTwoDatesonly(String startDate,String  endDate)
    {
        try {
            System.out.println("Start End Date=="+startDate+"=="+endDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDateD = simpleDateFormat.parse(startDate);
            Date endDateD = simpleDateFormat.parse(endDate);
            if (startDateD.equals(endDateD)) {
                return true;
            }
            else if (startDateD.after(endDateD)) {
                System.out.println("Date1 is after Date2");
                return true;
            }
            else if (startDateD.before(endDateD))
            {
                System.out.println("Date1 is before Date2");
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


}