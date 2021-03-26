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
import android.widget.RelativeLayout;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.EventHistoryAdapter;
import com.web.consultpin.adapter.ListOfChatUsers;
import com.web.consultpin.chat.ChatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chat extends Fragment {
    private View view;
    private MainActivity mainActivity;

    public Chat() {
        // Required empty public constructor
    }

    public static Chat newInstance(String param1, String param2) {
        Chat fragment = new Chat();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        getUserChatList();
        return view;
    }


    private void init(ArrayList<JSONObject> dataAr)
    {
        RecyclerView recycler_chat_view = view.findViewById(R.id.recycler_chat_view);
        RelativeLayout relativeLayout = view.findViewById(R.id.rr_nodata_view);
        if (dataAr.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
            recycler_chat_view.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            recycler_chat_view.setVisibility(View.VISIBLE);
        }
        ListOfChatUsers mAdapter = new ListOfChatUsers(dataAr, mainActivity);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_chat_view.setLayoutManager(horizontalLayoutManagaer);
        recycler_chat_view.setItemAnimator(new DefaultItemAnimator());
        recycler_chat_view.setAdapter(mAdapter);
    }

    private void getUserChatList()
    {

        try
        {
            final Map<String, String> m = new HashMap<>();


            if(Utilclass.isConsultantModeOn)
            {
                m.put("role", "1");
                m.put("user_id",mainActivity.getRestParamsName(Utilclass.consultant_id));
            }
            else
            {
                m.put("role", "0");
                m.put("user_id",mainActivity.getRestParamsName(Utilclass.user_id));

            }



            m.put("device_type", "android");
            m.put("device_token", mainActivity.getDeviceToken()+"");

            System.out.println("Chat data==="+m);

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", mainActivity.getRestParamsName(Utilclass.token) + "");



            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "get-chat-list", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status"))
                        {
                            try {

                                ArrayList<JSONObject> userListArray=new ArrayList<>();
                                JSONArray dataAr=jsonObject.getJSONArray("data");
                                for(int x=0;x<dataAr.length();x++)
                                {
                                    userListArray.add(dataAr.getJSONObject(x));

                                }

                                init(userListArray);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        } else {
                            init(new ArrayList<>());
                            mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                }
                            });
                        }
                    } catch (Exception e) {
                        init(new ArrayList<>());
                        e.printStackTrace();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}