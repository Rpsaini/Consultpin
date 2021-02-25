package com.web.consultpin.usersection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.EventHistoryAdapter;
import com.web.consultpin.events.ListOfEventsFragment;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserEventHistory extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event_history);
        System.out.println("User event history====");
        initiateObj();
        getSupportActionBar().hide();

        backActions();
    }
    private void backActions()
    {
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_title.setText(getResources().getString(R.string.event_request));
        findViewById(R.id.toolbar_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(ArrayList<JSONObject> dataAr)
    {
        RecyclerView user_event_history = findViewById(R.id.user_event_history);
        RelativeLayout relativeLayout =findViewById(R.id.rr_nodata_view);
        if(dataAr.size()==0)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            user_event_history.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
            user_event_history.setVisibility(View.VISIBLE);
        }
        EventHistoryAdapter mAdapter = new EventHistoryAdapter(dataAr,this);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        user_event_history.setLayoutManager(horizontalLayoutManagaer);
        user_event_history.setItemAnimator(new DefaultItemAnimator());
        user_event_history.setAdapter(mAdapter);
    }

    private void getEventList()
    {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", "0");
            final Map<String, String> obj = new HashMap<>();
            obj.put("token",getRestParamsName(Utilclass.token));




            System.out.println("before===="+m);

            serverHandler.sendToServer(this, getApiUrl() + "event-list", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

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
                            alertDialogs.alertDialog(UserEventHistory.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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