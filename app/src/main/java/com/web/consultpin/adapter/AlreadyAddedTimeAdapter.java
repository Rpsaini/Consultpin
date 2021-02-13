package com.web.consultpin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.web.consultpin.R;
import com.web.consultpin.consultant.SetTimeByConsultant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AlreadyAddedTimeAdapter extends RecyclerView.Adapter<AlreadyAddedTimeAdapter.MyViewHolder> {

    private JSONArray datAr;
    private SetTimeByConsultant pActivity;


    public AlreadyAddedTimeAdapter(JSONArray ar, SetTimeByConsultant paActiviity) {
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
           String timeStr=datAr.getString(position);
           holder.tv_selected_time.setText(timeStr);
            System.out.println("map--->in="+pActivity.preTimeMapAlready);
           if(pActivity.preTimeMapAlready.containsKey(position))
           {
               holder.tv_selected_time.setBackgroundResource(R.drawable.blue_drawable);
               holder.tv_selected_time.setTextColor(pActivity.getResources().getColor(R.color.white));
           }
           else
           {
               holder.tv_selected_time.setBackgroundResource(R.drawable.roundcorner_drawable);
               holder.tv_selected_time.setTextColor(pActivity.getResources().getColor(R.color.black));
           }
           holder.tv_selected_time.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v)
               {
                   System.out.println("map--->"+pActivity.preTimeMapAlready);
                   if(pActivity.preTimeMapAlready.containsKey(position))
                   {
                       pActivity.preTimeMapAlready.remove(position);
                   }
                   else
                   {
                       pActivity.preTimeMapAlready.put(position,holder.tv_selected_time.getText()+"");

                   }
                   pActivity.notifyMap(pActivity.preTimeMapCustom,"custom");
                   notifyDataSetChanged();

               }
           });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return datAr.length();
    }


    private void showImage(final String url, final ImageView header_img)
    {
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