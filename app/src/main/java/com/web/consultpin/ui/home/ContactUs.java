package com.web.consultpin.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.AccountInformation;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactUs extends BaseActivity {

    private EditText ed_fullname,ed_email,ed_mobile,ed_messgae;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initiateObj();
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


        TextView toolbar_title =findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.contactus));

        ed_fullname=findViewById(R.id.ed_fullname);
        ed_email=findViewById(R.id.ed_email);
        ed_mobile=findViewById(R.id.ed_mobile);
        ed_messgae=findViewById(R.id.ed_messgae);
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationRule.checkEmptyString(ed_fullname) == 0) {
                    alertDialogs.alertDialog(ContactUs.this, getResources().getString(R.string.Required), getResources().getString(R.string.enterfullname), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if(validationRule.checkEmptyString(ed_email)==0) {
                    alertDialogs.alertDialog(ContactUs.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_email), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if(validationRule.checkEmail(ed_email)!=2)
                {
                alertDialogs.alertDialog(ContactUs.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_email), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {

                    }
                });
                return;
                }

                if(validationRule.checkEmptyString(ed_mobile)==0) {
                    alertDialogs.alertDialog(ContactUs.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_mobilenumber), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if(validationRule.checkEmptyString(ed_messgae)==0) {
                    alertDialogs.alertDialog(ContactUs.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_message), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }


                saveDetails();


            }
        });

    }

    private void saveDetails()
    {


        try {
            final Map<String, String> m = new HashMap<>();
            m.put("fullname", ed_fullname.getText()+"");
            m.put("email", ed_email.getText()+"");
            m.put("mobile", ed_mobile.getText()+"");
            m.put("message", ed_messgae.getText()+"");

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));


            serverHandler.sendToServer(ContactUs.this, getApiUrl() + "contact-us", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status"))
                        {

                            ed_email.setText("");
                            ed_fullname.setText("");
                            ed_messgae.setText("");
                            ed_mobile.setText("");

                            alertDialogs.alertDialog(ContactUs.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                }
                            });



                        } else {
                            alertDialogs.alertDialog(ContactUs.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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