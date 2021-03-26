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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dialogsnpickers.AlertDialogs;
import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.AccountInformation;
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
    private AppCompatActivity pActivity;
    private String imageUrl = "";


    public EventHistoryAdapter(ArrayList<JSONObject> ar, AppCompatActivity paActiviity) {
        datAr = ar;
        pActivity = paActiviity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_list_of_event;
        ImageView event_image;
        TextView event_name, event_fee, event_number_of_user, appointment_time, txt_cancel,event_des;


        public MyViewHolder(View view) {
            super(view);
            ll_list_of_event = view.findViewById(R.id.ll_list_of_event);
            event_name = view.findViewById(R.id.event_name);
            event_fee = view.findViewById(R.id.event_fee);
            event_image = view.findViewById(R.id.event_image);
            event_number_of_user = view.findViewById(R.id.event_number_of_user);
            appointment_time = view.findViewById(R.id.appointment_time);
            txt_cancel = view.findViewById(R.id.txt_cancel);
            event_des = view.findViewById(R.id.event_des);

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
            holder.event_des.setText(jsonObject.getString("description"));
            holder.event_name.setText(jsonObject.getString("event_name"));
            holder.event_fee.setText(jsonObject.getString("event_fee") + pActivity.getResources().getString(R.string.lirasymbol));
            holder.event_number_of_user.setText(pActivity.getResources().getString(R.string.numberofparticipaint) + "  :  " + jsonObject.getString("number_of_participants"));
            holder.appointment_time.setText(jsonObject.getString("start_date") + " to  " + jsonObject.getString("end_date"));
            showImage(jsonObject.getString("banner"), holder.event_image);


            holder.ll_list_of_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    showDialog(jsonObject);
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

    private void showDialog(JSONObject jsonObject) {
        try {

            SimpleDialog simpleDialog = new SimpleDialog();
            final Dialog eventDetaildialog = simpleDialog.simpleDailog(pActivity, R.layout.event_list_dialog, new ColorDrawable(pActivity.getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);

            ImageView img_hideview = eventDetaildialog.findViewById(R.id.img_cross);

            TextView event_name = eventDetaildialog.findViewById(R.id.event_name);
            TextView event_fee = eventDetaildialog.findViewById(R.id.event_fee);
            ImageView event_image = eventDetaildialog.findViewById(R.id.event_image);
            TextView event_number_of_user = eventDetaildialog.findViewById(R.id.event_number_of_user);
            TextView appointment_time = eventDetaildialog.findViewById(R.id.appointment_time);
            TextView event_desc = eventDetaildialog.findViewById(R.id.event_desc);

            event_name.setText(jsonObject.getString("event_name"));
            event_desc.setText(jsonObject.getString("description"));
            event_fee.setText(jsonObject.getString("event_fee") + pActivity.getResources().getString(R.string.lirasymbol));
            event_number_of_user.setText(pActivity.getResources().getString(R.string.numberofparticipaint) + "  :  " + jsonObject.getString("number_of_participants"));
            appointment_time.setText(jsonObject.getString("start_date") + " to  " + jsonObject.getString("end_date"));
            showImage(jsonObject.getString("banner"), event_image);


            img_hideview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventDetaildialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}