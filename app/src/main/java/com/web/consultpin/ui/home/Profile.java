package com.web.consultpin.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.consultant.EditProfileConsultant;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;

import com.web.consultpin.consultant.TimeManagement;
import com.web.consultpin.events.EventRequestActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {

private View view;
private ImageView img_profile,img_edit_profile;
private TextView tv_username,tv_usermobile,tv_user_email,tv_professionalbg,tv_speciality,tv_fee,txt_setappointment,txt_event_request;
private MainActivity mainActivity;
    public Profile() {
        // Required empty public constructor
    }


    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_profile, container, false);
        mainActivity=(MainActivity) getActivity();
        init();
        return view;
    }

    private void init()
    {
        String userId= mainActivity.getRestParamsName(Utilclass.user_id);
        String consultantId= mainActivity.getRestParamsName(Utilclass.consultant_id);
        final LinearLayout ll_consultant_layout =view.findViewById(R.id.ll_consultant_layout);
        img_profile = view.findViewById(R.id.img_profile);
        tv_username = view.findViewById(R.id.tv_username);
        tv_usermobile = view.findViewById(R.id.tv_usermobile);
        tv_user_email = view.findViewById(R.id.tv_user_email);
        img_edit_profile = view.findViewById(R.id.img_edit_profile);
        tv_professionalbg = view.findViewById(R.id.tv_professionalbg);
        tv_speciality = view.findViewById(R.id.tv_speciality);
        tv_fee = view.findViewById(R.id.tv_fee);
        txt_setappointment = view.findViewById(R.id.txt_setappointment);
        txt_event_request = view.findViewById(R.id.txt_event_request);

        if(!Utilclass.isConsultantModeOn)
        {
            ll_consultant_layout.setVisibility(View.GONE);
        }
        else
        {
            ll_consultant_layout.setVisibility(View.VISIBLE);
            img_edit_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mainActivity, EditProfileConsultant.class);
                    startActivity(intent);
                }
            });
        }
        ViewProfile();
        txt_setappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainActivity, TimeManagement.class);
                startActivity(intent);

            }
        });

        txt_event_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainActivity, EventRequestActivity.class);
                startActivity(intent);

            }
        });
    }




    private void showImage(final String url, final ImageView header_img) {


        System.out.println("profile image==="+url);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(mainActivity)
                        .load(url)
                        .placeholder(R.drawable.profileavtar)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                        .into(header_img);
            }
        });
    }

    private void ViewProfile() {
        try
          {
            final Map<String, String> m = new HashMap<>();
            m.put("device_type", "android");
            m.put("device_token", mainActivity.getDeviceToken() + "");
            m.put("consultant_id", mainActivity.getRestParamsName(Utilclass.consultant_id));
            m.put("user_id", mainActivity.getRestParamsName(Utilclass.user_id));


            final Map<String, String> obj = new HashMap<>();
            obj.put("token", mainActivity.getRestParamsName(Utilclass.token));

            System.out.println("View profile==="+m+"==="+mainActivity.getApiUrl()+"view-profile");
            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "get-profile", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        System.out.println("Profile data==="+jsonObject);
                        if (jsonObject.getBoolean("status")) {
                            try
                            {

                                JSONObject userdata=jsonObject.getJSONObject("data").getJSONObject("user_data");
                                JSONObject consultant_data=jsonObject.getJSONObject("data").getJSONObject("consultant_data");

                                tv_username.setText(userdata.getString("first_name")+" "+userdata.getString("first_name"));
                                tv_usermobile.setText(userdata.getString("phone"));
                                tv_user_email.setText(userdata.getString("email"));

                                if(consultant_data.has("specialties")) {
                                    tv_speciality.setText(consultant_data.getString("specialties"));
                                }
                                if(consultant_data.has("experience")) {
                                    tv_professionalbg.setText(consultant_data.getString("experience"));
                                }
                                if(consultant_data.has("rate")) {
                                    tv_fee.setText(consultant_data.getString("rate") + "TL");
                                }

                                showImage(userdata.getString("profile_pic"),img_profile);
                                mainActivity.savePreferences.savePreferencesData(mainActivity,consultant_data,Utilclass.consultant_data);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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