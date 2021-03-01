package com.web.consultpin.adapter;


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
import com.google.gson.JsonArray;
import com.web.consultpin.R;

import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.usersection.SetAppointmentByUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TimeAndMoneyAdapter extends RecyclerView.Adapter<TimeAndMoneyAdapter.MyViewHolder> {

    private ArrayList<String> datAr;
    private SetAppointmentByUser pActivity;
    LinearLayout commonLAyout;
    TextView commonTextView;




    public TimeAndMoneyAdapter(ArrayList<String> ar, SetAppointmentByUser paActiviity) {
        datAr = ar;
        pActivity = paActiviity;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_selected_time;
        private LinearLayout ll_time_selection;
        public MyViewHolder(View view) {
            super(view);
            tv_selected_time = view.findViewById(R.id.tv_selected_time);
            ll_time_selection = view.findViewById(R.id.ll_time_selection);
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
            String timeStr=datAr.get(position);
            holder.tv_selected_time.setText(timeStr);



            if(commonLAyout!=null)
             {
                 holder.ll_time_selection.setBackgroundResource(R.drawable.blue_drawable);
                 commonTextView.setTextColor(pActivity.getColor(R.color.white));
             }

            holder.ll_time_selection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    System.out.println("time money layout==="+commonLAyout);

                    if(commonLAyout!=null)
                    {
                        commonLAyout.setBackgroundResource(R.drawable.roundcorner_drawable);
                        commonTextView.setTextColor(pActivity.getColor(R.color.black));
                    }
                    holder.ll_time_selection.setBackgroundResource(R.drawable.blue_drawable);
                    commonLAyout=holder.ll_time_selection;
                    commonTextView=holder.tv_selected_time;
                    holder.tv_selected_time.setTextColor(pActivity.getColor(R.color.white));
                    pActivity.timeDuration=holder.tv_selected_time.getText().toString();
                    commonTextView.setTextColor(pActivity.getColor(R.color.white));
                    System.out.println("time money layout==="+commonLAyout);
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