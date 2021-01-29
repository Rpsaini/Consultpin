package com.web.consultpin.consultant;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.web.consultpin.R;
import com.web.consultpin.main.BaseActivity;

public class EditProfileConsultant extends BaseActivity {

    private ImageView img_profile;
    private EditText et_name,ed_profession,ed_phone,ed_email,ed_profession_bg,ed_specialist,ed_fee;
    TextView tv_update;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initiateObj();
        removeActionBar();
        changestatusBarColorBlue();
        init();

    }


    private void init()
    {
       ImageView profile_back = findViewById(R.id.profile_back);
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_profile=findViewById(R.id.img_profile);
        ed_profession=findViewById(R.id.ed_profession);
        ed_phone=findViewById(R.id.ed_phone);
        ed_email=findViewById(R.id.ed_email);
        ed_profession_bg=findViewById(R.id.ed_profession_bg);
        ed_specialist=findViewById(R.id.ed_specialist);
        ed_fee=findViewById(R.id.ed_fee);
        tv_update=findViewById(R.id.tv_update);
        update();
    }


    private void update()
    {
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationRule.checkEmptyString(ed_profession) == 0) {
                    alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_your_name), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(ed_email) == 0) {
                    alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_phone), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(ed_phone) == 0) {
                    alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_email), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(ed_profession_bg) == 0) {
                    alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_proffessional_bg), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(ed_specialist) == 0) {
                    alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_speciality), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(ed_fee) == 0) {
                    alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_fee), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }




            }
        });
    }





}