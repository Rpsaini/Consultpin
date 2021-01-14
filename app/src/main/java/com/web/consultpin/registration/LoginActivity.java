package com.web.consultpin.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
      {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initiateObj();
        removeActionBar();
        init();
    }

   private void init()
    {
        TextView tv_sign_in =findViewById(R.id.tv_sign_in);
        TextView tv_forgot_pwd =findViewById(R.id.tv_forgot_pwd);
        TextView register =findViewById(R.id.register);
       final EditText et_email =findViewById(R.id.et_email);
       final EditText et_pwd =findViewById(R.id.et_pwd);


        tv_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validationRule.checkEmptyString(et_email) == 0) {
                    alertDialogs.alertDialog(LoginActivity.this, "Required", "Enter Username.", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
//                if (validationRule.checkEmail(et_mail) == 0) {
//                    alertDialogs.alertDialog(RegistrationScreen.this, "Invalid", "Enter valid Email.", "Ok", "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }

                if (validationRule.checkEmptyString(et_pwd) == 0) {
                    alertDialogs.alertDialog(LoginActivity.this, "Required", "Enter password.", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                login(et_email.getText().toString(),et_pwd.getText().toString());


            }
        });
        tv_forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistrationScreen.class);
                startActivity(intent);
            }
        });
    }


    private void login(String email,String password)
    {

        try {
            final Map<String, String> m = new HashMap<>();
            m.put("email", email);
            m.put("password", password);
            m.put("device_type", "android");
            m.put("device_token", getDeviceToken()+"");

            final Map<String, String> obj = new HashMap<>();
            obj.put("X-API-KEY", getXapiKey());
            obj.put("Token", savePreferences.reterivePreference(this, "session_token") + "");
            obj.put("uid", getRestParamsName("uid"));


            serverHandler.sendToServer(this, getApiUrl() + "login", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            System.out.println("Login obj===" + jsonObject);

                        } else {
                            alertDialogs.alertDialog(LoginActivity.this, "Response", jsonObject.getString("msg"), "Ok", "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}