package com.web.consultpin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.consultant.PapularConsultantFullListing;
import com.web.consultpin.consultant.SetAppointMent;

import org.json.JSONObject;

import java.util.ArrayList;


public class AlreadyAddedTimeAdapter extends RecyclerView.Adapter<AlreadyAddedTimeAdapter.MyViewHolder> {

    private ArrayList<JSONObject> datAr;
    private SetAppointMent pActivity;



    public AlreadyAddedTimeAdapter(ArrayList<JSONObject> ar, SetAppointMent paActiviity) {
        datAr = ar;
        pActivity = paActiviity;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_selected_time;

        public MyViewHolder(View view) {
            super(view);



            tv_selected_time = view.findViewById(R.id.tv_selected_time);



        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timing_list_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {





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
                        .placeholder(R.drawable.man)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(header_img);
            }
        });


    }

}