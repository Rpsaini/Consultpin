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
import com.web.consultpin.adapter.ListOfFavoritesAdapter;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListFavouritesdata extends BaseActivity {

    private ArrayList<JSONObject> dataAr=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favouritesdata);
        initiateObj();
        getSupportActionBar().hide();
        getFavList();
        goBack();
    }

    private void goBack()
    {
        TextView toolbar_title =findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.favorite_consultants));
        findViewById(R.id.toolbar_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(ArrayList<JSONObject> dataAr)
    {

       RecyclerView recycler_view_favorites =findViewById(R.id.recycler_view_favorites);
       RelativeLayout relativeLayout =findViewById(R.id.rr_nodata_view);

        if(dataAr.size()==0)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            recycler_view_favorites.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
            recycler_view_favorites.setVisibility(View.VISIBLE);
        }

        recycler_view_favorites.setNestedScrollingEnabled(false);
        recycler_view_favorites.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recycler_view_favorites.setHasFixedSize(true);
        recycler_view_favorites.setItemAnimator(new DefaultItemAnimator());
        ListOfFavoritesAdapter horizontalCategoriesAdapter = new ListOfFavoritesAdapter(dataAr,this,"");
        recycler_view_favorites.setAdapter(horizontalCategoriesAdapter);

    }


    private void getFavList()
       {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("user_id", getRestParamsName(Utilclass.user_id));
            final Map<String, String> obj = new HashMap<>();
            obj.put("token",getRestParamsName(Utilclass.token));



            serverHandler.sendToServer(this, getApiUrl() + "getfavourites", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);

                        System.out.println("List of fav===="+jsonObject);
                        if (jsonObject.getBoolean("status")) {
                            try {
//
                                JSONArray dataAr=jsonObject.getJSONArray("data");
                                ArrayList<JSONObject> dataObjAr=new ArrayList<>();
                                for(int x=0;x<dataAr.length();x++)
                                {
                                    dataObjAr.add(dataAr.getJSONObject(x));
                                }
                                init(dataObjAr);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            init(new ArrayList<>());
                            alertDialogs.alertDialog(ListFavouritesdata.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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