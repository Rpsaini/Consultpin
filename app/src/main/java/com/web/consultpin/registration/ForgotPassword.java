package com.web.consultpin.registration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        initiateObj();
        init();

    }

    private void init() {
        final EditText et_email = findViewById(R.id.et_email);
        TextView tv_sign_in = findViewById(R.id.tv_sign_in);

        tv_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationRule.checkEmptyString(et_email) == 0) {
                    alertDialogs.alertDialog(ForgotPassword.this, getResources().getString(R.string.Required), getResources().getString(R.string.enteryouremail), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmail(et_email) == 0) {
                    alertDialogs.alertDialog(ForgotPassword.this, getResources().getString(R.string.Invalid), getResources().getString(R.string.entervalidemail), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                } else {

                    forgotPassword(et_email.getText().toString());
                }
            }
        });

    }


    private void forgotPassword(String email) {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("email", email);
            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("X-API-KEY", getXapiKey());
            obj.put("Token", savePreferences.reterivePreference(this, "session_token") + "");
            obj.put("uid", getRestParamsName("uid"));


            serverHandler.sendToServer(this, getApiUrl() + "forgot", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            Intent intent=new Intent(ForgotPassword.this,EnterOTP.class);
                            intent.putExtra(Utilclass.otp,jsonObject.getString("otp"));
                            intent.putExtra(Utilclass.email,email);
                            startActivityForResult(intent,1001);


                        } else {
                            alertDialogs.alertDialog(ForgotPassword.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001)
        {
            Intent intent=new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}