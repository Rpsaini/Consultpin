package com.web.consultpin.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {

private View view;
private ImageView img_profile,img_edit_profile;
private TextView tv_username,tv_usermobile,tv_user_email,tv_professionalbg,tv_speciality,tv_fee;
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
        img_profile=view.findViewById(R.id.img_profile);
        tv_username=view.findViewById(R.id.tv_username);
        tv_usermobile=view.findViewById(R.id.tv_usermobile);
        tv_user_email=view.findViewById(R.id.tv_user_email);
        img_edit_profile=view.findViewById(R.id.img_edit_profile);
        tv_professionalbg=view.findViewById(R.id.tv_professionalbg);
        tv_speciality=view.findViewById(R.id.tv_speciality);
        tv_fee=view.findViewById(R.id.tv_fee);
        ViewProfile();
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
            m.put("user_id", mainActivity.getRestParamsName("user_id"));


            final Map<String, String> obj = new HashMap<>();
            obj.put("Authorization", mainActivity.getRestParamsName(Utilclass.token));

            System.out.println("View profile==="+m+"==="+mainActivity.getApiUrl()+"view-profile");
            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "view-profile", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        System.out.println("Profile data==="+jsonObject);
                        if (jsonObject.getBoolean("status")) {
                            try
                            {

                                JSONObject data=jsonObject.getJSONObject("data");

                                tv_username.setText(data.getString("name"));
                                tv_usermobile.setText(data.getString("phone"));
                                tv_user_email.setText(data.getString("email"));
                                tv_speciality.setText(data.getString("category_name"));
                                tv_professionalbg.setText(data.getString("experience"));
                                tv_fee.setText(data.getString("rate")+"TL");
                                int lastIndex=mainActivity.getRestParamsName("profile_pic").lastIndexOf("/");
                                String imageUrl=mainActivity.getRestParamsName("profile_pic").substring(0,lastIndex);
                                showImage(imageUrl+"/"+data.getString("profile_pic"),img_profile);


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