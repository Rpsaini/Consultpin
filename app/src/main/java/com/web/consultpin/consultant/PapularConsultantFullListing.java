package com.web.consultpin.consultant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.CategoryConsultantListingAdapter;
import com.web.consultpin.adapter.NewCategoryAdapter;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PapularConsultantFullListing extends BaseActivity {
    private  RecyclerView fulldetail_listing_reycler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papular_consultant_full_listing);

        initiateObj();
        removeActionBar();
        changestatusBarColor(0);
        init();
        getCategoryWiseConsultant();
    }
    private void init()
    {
        findViewById(R.id.toolbar_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView toolBarTitle =findViewById(R.id.toolbar_title);
        toolBarTitle.setText(getResources().getString(R.string.papular_consultant));
        fulldetail_listing_reycler =findViewById(R.id.fulldetail_listing_reycler);

    }


    private void loadData(ArrayList<JSONObject> dataAr)
    {

        RelativeLayout relativeLayout =findViewById(R.id.rr_nodata_view);
        if(dataAr.size()==0)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            fulldetail_listing_reycler.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
            fulldetail_listing_reycler.setVisibility(View.VISIBLE);
        }

        fulldetail_listing_reycler.setNestedScrollingEnabled(false);
        fulldetail_listing_reycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        fulldetail_listing_reycler.setHasFixedSize(true);
        fulldetail_listing_reycler.setItemAnimator(new DefaultItemAnimator());
        CategoryConsultantListingAdapter horizontalCategoriesAdapter = new CategoryConsultantListingAdapter(dataAr,this,"");
        fulldetail_listing_reycler.setAdapter(horizontalCategoriesAdapter);
    }


    private void getCategoryWiseConsultant()
    {


        try {
            final Map<String, String> m = new HashMap<>();

            m.put("category_id", getIntent().getStringExtra(Utilclass.category_id));
            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));

            System.out.println("catehorydata before==="+m);

            serverHandler.sendToServer(this, getApiUrl() + "get-category-detail", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status"))
                        {
                            System.out.println("catehorydata dataa==="+jsonObject);

                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray subCategoryArray=data.getJSONArray("sub_category");

//                            "category_name": "Psychologist",
//                                "category_id": "12",
//                                "total": "1"
                            JSONArray consultant=data.getJSONArray("consultant");

                            ArrayList<JSONObject> dataAr=new ArrayList<>();
                            for(int x=0;x<consultant.length();x++)
                            {
                             dataAr.add(consultant.getJSONObject(x));
                            }

                            loadData(dataAr);

                        } else {
                            alertDialogs.alertDialog(PapularConsultantFullListing.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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