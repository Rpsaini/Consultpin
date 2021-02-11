package com.web.consultpin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.consultant.AppointmentHistory;
import com.web.consultpin.consultant.PapularConsultantFullListing;

import org.json.JSONObject;

import java.util.ArrayList;




public class AppointmentHistoryAdapter extends RecyclerView.Adapter<AppointmentHistoryAdapter.MyViewHolder> {

    private ArrayList<JSONObject> datAr;
    private MainActivity pActivity;
    private String imageUrl="";


    public AppointmentHistoryAdapter(ArrayList<JSONObject> ar, MainActivity paActiviity, String url) {
        datAr = ar;
        pActivity = paActiviity;
        imageUrl=url;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_best_restaurant;
        ImageView consultant_image;
        TextView consultant_name,time_duration,appointment_time,amount;

        public MyViewHolder(View view) {
            super(view);


            ll_best_restaurant = view.findViewById(R.id.ll_best_restaurant);
            consultant_name = view.findViewById(R.id.consultant_name);
            time_duration = view.findViewById(R.id.time_duration);
            appointment_time = view.findViewById(R.id.appointment_time);
            consultant_image = view.findViewById(R.id.consultant_image);
            amount = view.findViewById(R.id.amount);






        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_history_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {



            JSONObject jsonObject=datAr.get(position);

            String user_name=jsonObject.getString("user_name");
            String duration=jsonObject.getString("appointment_duration");
            String appointmentTime=jsonObject.getString("appointment_date")+" "+jsonObject.getString("appointment_time");
            String fee=jsonObject.getString("appointment_fee");


            holder.appointment_time.setText(appointmentTime);
            holder.consultant_name.setText(user_name);
            holder.time_duration.setText(duration+" Minutes");
            holder.amount.setText(fee+pActivity.getResources().getString(R.string.lirasymbol));
            showImage(jsonObject.getString("profile_pic"),holder.consultant_image);

//            "profile_pic": "ebdddbb0e39e0900248a01852c476d64.jpeg",
//                    "user_name": "Gagan  sapra",
//                    "appointment_duration": "30",
//                    "appointment_date": "2021-02-21",
//                    "appointment_time": "11:30:00",
//                    "appointment_fee": "300.00"

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