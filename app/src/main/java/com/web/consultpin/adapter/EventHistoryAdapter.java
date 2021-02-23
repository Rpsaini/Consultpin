package com.web.consultpin.adapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.AppointmentHistory;
import com.web.consultpin.consultant.PapularConsultantFullListing;
import com.web.consultpin.events.EventRequestActivity;
import com.web.consultpin.initiatecall.InitiateCallWebview;
import com.web.consultpin.jitsi.MAinJistsiActivity;
//import com.web.consultpin.initiatecall.InitiateCallWebview;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHistoryAdapter extends RecyclerView.Adapter<EventHistoryAdapter.MyViewHolder> {

    private ArrayList<JSONObject> datAr;
    private EventRequestActivity pActivity;
    private String imageUrl = "";


    public EventHistoryAdapter(ArrayList<JSONObject> ar, EventRequestActivity paActiviity) {
        datAr = ar;
        pActivity = paActiviity;



    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_list_of_event;
        ImageView event_image;
        TextView event_name, event_fee, event_number_of_user, appointment_time,txt_cancel;


        public MyViewHolder(View view) {
            super(view);


            ll_list_of_event = view.findViewById(R.id.ll_list_of_event);
            event_name = view.findViewById(R.id.event_name);
            event_fee = view.findViewById(R.id.event_fee);
            event_image = view.findViewById(R.id.event_image);
            event_number_of_user = view.findViewById(R.id.event_number_of_user);
            appointment_time = view.findViewById(R.id.appointment_time);
            txt_cancel = view.findViewById(R.id.txt_cancel);






        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {


            JSONObject jsonObject = datAr.get(position);

            holder.event_name.setText(jsonObject.getString("description"));
            holder.event_fee.setText(jsonObject.getString("event_fee")+pActivity.getResources().getString(R.string.lirasymbol));
            holder.event_number_of_user.setText(pActivity.getResources().getString(R.string.numberofparticipaint)+"  :  "+jsonObject.getString("number_of_participants"));
            holder.appointment_time.setText(jsonObject.getString("event_date")+" "+jsonObject.getString("event_time"));
            showImage(jsonObject.getString("banner"),holder.event_image);


            holder.txt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pActivity.alertDialogs.alertDialog(pActivity, pActivity.getString(R.string.choose), "Choose Image either from camera or from gallary?", "Camera", "Gallary", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return datAr.size();
    }


    private void showImage(final String url, final ImageView header_img) {
        pActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(pActivity)
                        .load(url)
                        .placeholder(R.drawable.profileavtar)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(header_img);
            }
        });


    }



}