package com.web.consultpin.consultant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.usersection.SetAppointmentByUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConsultantDetailView extends BaseActivity {
    String consultant_id = "";
    ImageView toolbar_back_arrow;
    TextView txt_price;
    TextView txt_consultantname;
    TextView txt_speciality;
    TextView txt_rating;
    TextView txt_review;
    TextView txt_detail;
    TextView set_appointment;
    ImageView img_consultant, img_categoryicon;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_detail_view);
        initiateObj();
        getSupportActionBar().hide();
        init();
    }

    private void init() {
        consultant_id = getIntent().getStringExtra(Utilclass.consultant_id);

        toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
        txt_price = findViewById(R.id.txt_price);
        txt_consultantname = findViewById(R.id.txt_consultantname);
        txt_speciality = findViewById(R.id.txt_speciality);
        txt_rating = findViewById(R.id.txt_rating);
        txt_review = findViewById(R.id.txt_review);
        txt_detail = findViewById(R.id.txt_detail);
        set_appointment = findViewById(R.id.set_appointment);
        img_consultant = findViewById(R.id.img_consultant);
        toolbar_title = findViewById(R.id.toolbar_title);
        img_categoryicon = findViewById(R.id.img_categoryicon);
        toolbar_title.setText(getResources().getString(R.string.consultant));
        addListener();
        consultantDetailApi();
    }


    private void consultantDetailApi() {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.user_id));

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token) + "");

            System.out.println("ViewProfile===" + m);

            serverHandler.sendToServer(this, getApiUrl() + "view-profile", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {

                        JSONObject jsonObject = new JSONObject(dta);
                        System.out.println("profile data==>>=" + jsonObject);
                        if (jsonObject.getBoolean("status")) {
                            try {


                                JSONObject dataObj = null;
                                dataObj = jsonObject.getJSONObject("data");

                                txt_consultantname.setText(dataObj.getString("name"));
                                txt_speciality.setText(dataObj.getString("category_name"));
                                txt_rating.setText("4.5/5");
                                txt_review.setText("122 Reviews");
                                txt_detail.setText(dataObj.getString("experience"));

                                txt_price.setText("10 "+getResources().getString(R.string.lirasymbol));
                                img_consultant = findViewById(R.id.img_consultant);
                                toolbar_title = findViewById(R.id.toolbar_title);
                                toolbar_title.setText(getResources().getString(R.string.consultant));

                                showImage(dataObj.getString("profile_pic"), img_consultant);
                                showImage(dataObj.getString("main_category_icon"), img_categoryicon);


                                //   "user_id": "41",
//                                    "profile_pic": "http:\/\/webcomclients.in\/consultpindev\/assets\/uploads\/ebdddbb0e39e0900248a01852c476d64.jpeg",
//                                    "experience": "6 years of experience",
//                                    "specialties": "Eyes",
//                                    "rate": "66",
//                                    "email": "ok1@mailinator.com",
//                                    "phone": "8989899090",
//                                    "name": "Gagan  sapra",
//                                    "profile_status": "1",
//                                    "main_category": "Doctor",
//                                    "main_category_icon": "http:\/\/webcomclients.in\/consultpindev\/assets\/uploads\/doctor@3x8.png",
//                                    "category_name": "ayurvedic,Gynacologist"
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(ConsultantDetailView.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

    private void addListener() {
        set_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultantDetailView.this, SetAppointmentByUser.class);
                intent.putExtra(Utilclass.consultant_id, consultant_id);
                startActivity(intent);
            }
        });
    }

    private void showImage(final String url, final ImageView header_img) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(ConsultantDetailView.this)
                        .load(url)
                        .placeholder(R.drawable.man)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(header_img);
            }
        });
    }
}