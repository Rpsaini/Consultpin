package com.web.consultpin.consultant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.usersection.SetAppointmentByUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

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
    RelativeLayout rr_like;
    ImageView img_like;
    private int isLiked = 0;

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
        rr_like = findViewById(R.id.rr_like);
        img_like = findViewById(R.id.img_like);
        toolbar_title.setText(getResources().getString(R.string.consultant));
        addListener();
        consultantDetailApi();
    }


    private void consultantDetailApi() {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));
            m.put("user_id", getRestParamsName(Utilclass.user_id));

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token) + "");

            System.out.println("ViewProfile===" + m);

            serverHandler.sendToServer(this, getApiUrl() + "get-consultant-details", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
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

                                txt_detail.setText(dataObj.getString("experience"));

                                txt_price.setText(dataObj.getString("rate") + getResources().getString(R.string.lirasymbol));
                                img_consultant = findViewById(R.id.img_consultant);
                                toolbar_title = findViewById(R.id.toolbar_title);
                                toolbar_title.setText(getResources().getString(R.string.consultant));

                                showImage(dataObj.getString("profile_pic"), img_consultant);
                                showImage(dataObj.getString("main_category_icon"), img_categoryicon);


                                String totalReview = dataObj.getString("total_reviews");
                                String rating = dataObj.getString("rating");
                                txt_review.setText(totalReview + " Reviews");
                                txt_rating.setText("5/5");
                                if (!rating.equalsIgnoreCase("null")) {
                                    txt_rating.setText(rating + "/5");
                                }

                                if (dataObj.getString("is_favourite").equalsIgnoreCase("0"))
                                {
                                    isLiked = 0;

                                    img_like.setColorFilter(ContextCompat.getColor(ConsultantDetailView.this, R.color.texthintcolor), android.graphics.PorterDuff.Mode.SRC_IN);

                                } else {
                                    img_like.setColorFilter(ContextCompat.getColor(ConsultantDetailView.this, R.color.red_color), android.graphics.PorterDuff.Mode.SRC_IN);
                                    isLiked = 1;
                                }


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
                startActivityForResult(intent, Utilclass.appointmentRequsestcode);
            }
        });

        rr_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  dd

                setLikeDislike();
            }
        });
    }

    private void showImage(final String url, final ImageView header_img) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(ConsultantDetailView.this)
                        .load(url)
                        .placeholder(R.drawable.profileavtar)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(header_img);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Dataa bacl====" + requestCode);
        if (requestCode == Utilclass.appointmentRequsestcode) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    private void setLikeDislike() {
        final Map<String, String> m = new HashMap<>();
        m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));
        m.put("user_id", getRestParamsName(Utilclass.user_id));

        m.put("device_type", "android");
        m.put("device_token", getDeviceToken() + "");

        final Map<String, String> obj = new HashMap<>();
        obj.put("token", getRestParamsName(Utilclass.token));

        System.out.println("before=====" + obj);
        serverHandler.sendToServer(this, getApiUrl() + "favourites", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("LikeDislikeData data===" + dta);
                    JSONObject jsonObject = new JSONObject(dta);
                    if (jsonObject.getBoolean("status")) {
                        try {
                            if (isLiked == 0) {
                                img_like.setColorFilter(getResources().getColor(R.color.red_color), android.graphics.PorterDuff.Mode.SRC_IN);
                                isLiked = 1;
                            } else {
                                img_like.setColorFilter(getResources().getColor(R.color.border_color), android.graphics.PorterDuff.Mode.SRC_IN);
                                isLiked = 0;
                            }

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

    }
}