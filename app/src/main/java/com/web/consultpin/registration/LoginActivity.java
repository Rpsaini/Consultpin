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
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
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
               addClickEventEffet(tv_sign_in);

                if (validationRule.checkEmptyString(et_email) == 0) {
                    alertDialogs.alertDialog(LoginActivity.this, getResources().getString(R.string.Required), getResources().getString(R.string.enteryouremail), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }


                if (validationRule.checkEmptyString(et_pwd) == 0) {
                    alertDialogs.alertDialog(LoginActivity.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_password), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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
                addClickEventEffet(tv_forgot_pwd);
                Intent intent=new Intent(LoginActivity.this,ForgotPassword.class);
                startActivityForResult(intent,1001);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClickEventEffet(register);
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

                            try {

                                savePreferences.savePreferencesData(LoginActivity.this,jsonObject, Utilclass.loginDetail);
                                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(LoginActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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