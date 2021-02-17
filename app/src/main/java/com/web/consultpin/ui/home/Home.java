package com.web.consultpin.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.CategoryAdapter;
import com.web.consultpin.adapter.NewCategoryAdapter;
import com.web.consultpin.adapter.PapularConsultantAdapter;
import com.web.consultpin.registration.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends Fragment {

    private View view;
    private MainActivity mainActivity;

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();
        getDashboardData();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return view;
    }

    private void getDashboardData() {

        try {
            final Map<String, String> m = new HashMap<>();
            m.put("device_type", "android");
            m.put("device_token", mainActivity.getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", mainActivity.getRestParamsName(Utilclass.token));


            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "dashboard", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status"))
                        {
                            System.out.println("home dataa==="+jsonObject);
                            try {
                                String baseUrl = jsonObject.getString("icon_base_url");
                                JSONArray categories = jsonObject.getJSONArray("categories");
                                JSONArray popular_consultants = jsonObject.getJSONArray("popular_consultants");
                                JSONArray new_consultants = jsonObject.getJSONArray("new_consultants");

                                ArrayList<JSONObject> catAr = new ArrayList<>();
                                for (int x = 0; x < categories.length(); x++) {
                                    catAr.add(categories.getJSONObject(x));
                                }

                                ArrayList<JSONObject> newconsultantAr = new ArrayList<>();
                                for (int x = 0; x < new_consultants.length(); x++) {
                                    newconsultantAr.add(new_consultants.getJSONObject(x));
                                }


                                ArrayList<JSONObject> papularConsultantAr = new ArrayList<>();
                                for (int x = 0; x < popular_consultants.length(); x++) {
                                    papularConsultantAr.add(popular_consultants.getJSONObject(x));
                                }
                                initHomeCategory(baseUrl,catAr);
                                consultantdata(baseUrl,papularConsultantAr);
                                newConsultant(baseUrl,newconsultantAr);





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


    private void initHomeCategory(String imageUrl,ArrayList<JSONObject> dataAr)
    {
        RecyclerView categoryRecycle =view.findViewById(R.id.recyclerview_popular_cat);

        categoryRecycle.setNestedScrollingEnabled(false);
        categoryRecycle.setLayoutManager(new LinearLayoutManager(mainActivity,
                LinearLayoutManager.HORIZONTAL, false));
        categoryRecycle.setHasFixedSize(true);
        categoryRecycle.setItemAnimator(new DefaultItemAnimator());
        CategoryAdapter horizontalCategoriesAdapter = new CategoryAdapter(dataAr,mainActivity,imageUrl);
        categoryRecycle.setAdapter(horizontalCategoriesAdapter);

        consultantdata(imageUrl,dataAr);
    }

    private void consultantdata(String imageUrl,ArrayList<JSONObject> dataAr)
    {
        RecyclerView recyclerview_popular_consultant =view.findViewById(R.id.recyclerview_popular_consultant);

        recyclerview_popular_consultant.setNestedScrollingEnabled(false);
        recyclerview_popular_consultant.setLayoutManager(new LinearLayoutManager(mainActivity,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerview_popular_consultant.setHasFixedSize(true);
        recyclerview_popular_consultant.setItemAnimator(new DefaultItemAnimator());
        PapularConsultantAdapter horizontalCategoriesAdapter = new PapularConsultantAdapter(dataAr,mainActivity,imageUrl);
        recyclerview_popular_consultant.setAdapter(horizontalCategoriesAdapter);
    }

    private void newConsultant(String imageUrl,ArrayList<JSONObject> dataAr)
    {
        RecyclerView recyclerview_new_category =view.findViewById(R.id.recyclerview_new_category);
        recyclerview_new_category.setNestedScrollingEnabled(false);
        recyclerview_new_category.setLayoutManager(new LinearLayoutManager(mainActivity,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerview_new_category.setHasFixedSize(true);
        recyclerview_new_category.setItemAnimator(new DefaultItemAnimator());
        NewCategoryAdapter horizontalCategoriesAdapter = new NewCategoryAdapter(dataAr,mainActivity,imageUrl);
        recyclerview_new_category.setAdapter(horizontalCategoriesAdapter);
    }




    private void getCategoryData() {

        try {
            final Map<String, String> m = new HashMap<>();
            m.put("device_type", "android");
            m.put("device_token", mainActivity.getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("Authorization", mainActivity.getRestParamsName(Utilclass.token));


            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "dashboard", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {
                                String baseUrl = jsonObject.getString("icon_base_url");
                                JSONArray categories = jsonObject.getJSONArray("categories");
                                ArrayList<JSONObject> catAr = new ArrayList<>();
                                for (int x = 0; x < categories.length(); x++) {
                                    catAr.add(categories.getJSONObject(x));
                                }
                                initHomeCategory(baseUrl,catAr);


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