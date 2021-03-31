package com.web.consultpin.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        initiateObj();
        removeActionBar();
        init();
    }

    private void init()
    {
        RelativeLayout rr_back =findViewById(R.id.rr_back);
        TextView tv_register =findViewById(R.id.tv_register);

        EditText et_firstname =findViewById(R.id.et_firstname);
        EditText et_lastname =findViewById(R.id.et_lastname);
        EditText et_mail =findViewById(R.id.et_mail);
        EditText ed_mobile =findViewById(R.id.ed_mobile);
        EditText et_password =findViewById(R.id.et_password);
        EditText et_conf_password =findViewById(R.id.et_conf_password);
        CheckBox chk_termsselected =findViewById(R.id.chk_termsselected);
        ImageView img_eye_password =findViewById(R.id.img_eye_password);
        ImageView img_eye_conf_password =findViewById(R.id.img_eye_conf_password);
        TextView txt_login =findViewById(R.id.txt_login);

        img_eye_password.setTag("0");
        img_eye_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            if(v.getTag().equals("0")) {
                img_eye_password.setTag("1");
                hideShowPassword(1, et_password,img_eye_password);
            }
            else
            {
                img_eye_password.setTag("0");
                hideShowPassword(0, et_password,img_eye_password);
            }

            }
        });

        img_eye_conf_password.setTag("0");
        img_eye_conf_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(v.getTag().equals("0")) {
                    img_eye_conf_password.setTag("1");
                    hideShowPassword(1, et_conf_password,img_eye_conf_password);
                }
                else
                {
                    img_eye_conf_password.setTag("0");
                    hideShowPassword(0, et_conf_password,img_eye_conf_password);
                }

            }
        });




        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addClickEventEffet(tv_register);
                if (validationRule.checkEmptyString(et_firstname) == 0) {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_firstname), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(et_lastname) == 0) {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_lastname), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(et_mail) == 0) {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.entervalidemail), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmail(et_mail) == 0) {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Invalid), getResources().getString(R.string.entervalidemail), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(ed_mobile) == 0) {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_mobilenumber), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkMobileNumber(ed_mobile, 10) == 1) {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Invalid), getResources().getString(R.string.enter_valid_mobilenumber), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(et_password) == 0) {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_password), getResources().getString(R.string.Invalid), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkPssword(et_password, et_conf_password) != 2) {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Invalid), getResources().getString(R.string.enter_conf_password), getResources().getString(R.string.Invalid), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if(!chk_termsselected.isChecked())
                {
                    alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Response), getResources().getString(R.string.select_term_condition), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                signupApi(et_mail.getText().toString(),et_password.getText().toString(),et_conf_password.getText().toString(),et_lastname.getText().toString(),et_firstname.getText().toString(),ed_mobile.getText().toString());

                }
        });
        rr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addClickEventEffet(rr_back);
               finish();
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClickEventEffet(txt_login);
                finish();
            }
        });


    }



    private void signupApi(String email,String password,String conf_password,String lastname,String name,String mobile)
    {

        try {
            final Map<String, String> m = new HashMap<>();
            m.put("email", email);
            m.put("password", password);
            m.put("confirm_password", conf_password+"");
            m.put("surname", lastname+"");
            m.put("name", name+"");
            m.put("mobile", mobile+"");
            m.put("device_type", "android");
            m.put("device_token", getDeviceToken()+"");

            final Map<String, String> obj = new HashMap<>();
//            obj.put("X-API-KEY", getXapiKey());
//            obj.put("Token", savePreferences.reterivePreference(this, "session_token") + "");
//            obj.put("uid", getRestParamsName("uid"));




            serverHandler.sendToServer(this, getApiUrl() + "register", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            System.out.println("Restaurant obj===" + jsonObject);

                            alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Required), jsonObject.getString("msg"), "Ok", "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed)
                                {
                                    Intent intent=new Intent(RegistrationScreen.this,LoginActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            });

                        } else {
                            alertDialogs.alertDialog(RegistrationScreen.this, getResources().getString(R.string.Required), jsonObject.getString("msg"), "Ok", "", new DialogCallBacks() {
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