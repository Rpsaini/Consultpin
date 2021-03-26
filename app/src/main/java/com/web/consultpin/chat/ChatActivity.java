package com.web.consultpin.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.UtilClass;
import com.google.gson.JsonArray;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.registration.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChatActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        initiateObj();
        init();
        getUserMessages();
        replyMsg();
    }

    private void init() {

        ImageView toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
        toolbar_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView nameTxt = findViewById(R.id.toolbar_title);
        nameTxt.setText(getIntent().getStringExtra(Utilclass.username));

    }

    private void showMessages(ArrayList<JSONObject> dataAr) {
        RecyclerView reply_recycler_view = findViewById(R.id.reply_recycler_view);
        ChatMessagesAdapter transferPurposeAdater = new ChatMessagesAdapter(dataAr, ChatActivity.this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reply_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        reply_recycler_view.setItemAnimator(new DefaultItemAnimator());
        reply_recycler_view.setAdapter(transferPurposeAdater);
        reply_recycler_view.scrollToPosition(dataAr.size() - 1);
    }


    private void getUserMessages() {

        try {
            final Map<String, String> m = new HashMap<>();

            if(getIntent().getStringExtra(Utilclass.callFrom).equalsIgnoreCase("chatList")) {
                m.put("appointment_id", "0");
            }
            else
            {
                m.put("appointment_id", getIntent().getStringExtra(Utilclass.appointment_id));
            }
            m.put("receiver_user_id", getIntent().getStringExtra(Utilclass.receiver_id));
            m.put("sender_user_id", getIntent().getStringExtra(Utilclass.sender_id));
            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token) + "");

            serverHandler.sendToServer(this, getApiUrl() + "get-chat-history", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                System.out.println("chatdata===" + jsonObject);

                                JSONArray dataAr =jsonObject.getJSONArray("data");
                                ArrayList<JSONObject> messageAr=new ArrayList<>();
                                for(int x=0;x<dataAr.length();x++)
                                {
                                    messageAr.add(dataAr.getJSONObject(x));
                                }

                                showMessages(messageAr);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(ChatActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

    private EditText ed_reply;
    private void replyMsg() {
         ed_reply = findViewById(R.id.ed_reply);
        RelativeLayout rr_postmessage = findViewById(R.id.rr_postmessage);
        rr_postmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_reply.getText().toString().length() == 0) {

                } else {
                    submitTicket(ed_reply.getText().toString());
                }
            }
        });

    }


    private void submitTicket(String msg) {
        try
        {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("appointment_id",getIntent().getStringExtra(Utilclass.appointment_id));
        m.put("receiver_user_id", getIntent().getStringExtra(Utilclass.receiver_id));
        m.put("sender_user_id", getIntent().getStringExtra(Utilclass.sender_id));
        m.put("message", msg);

        m.put("sender_consultant_id", getIntent().getStringExtra(Utilclass.sender_consultant_id));
        m.put("receiver_consultant_id", getIntent().getStringExtra(Utilclass.receiver_consultant_id));

        System.out.println("Before==="+m);

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token) + "");


        serverHandler.sendToServer(this, getApiUrl() + "send-chat-message", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                System.out.println("Messgae send==="+jsonObject);
                                getUserMessages();
                                ed_reply.setText("");

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(ChatActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

