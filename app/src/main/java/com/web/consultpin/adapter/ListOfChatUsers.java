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
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.events.EventRequestActivity;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListOfChatUsers extends RecyclerView.Adapter<ListOfChatUsers.MyViewHolder> {

    private ArrayList<JSONObject> datAr;
    private MainActivity pActivity;



    public ListOfChatUsers(ArrayList<JSONObject> ar, MainActivity paActiviity) {
        datAr = ar;
        pActivity = paActiviity;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout ll_list_of_event;
        ImageView chat_user_image;
        TextView chat_user_name, chat_speciality;


        public MyViewHolder(View view) {
            super(view);


            ll_list_of_event = view.findViewById(R.id.ll_list_of_event);
            chat_user_name = view.findViewById(R.id.chat_user_name);
            chat_speciality = view.findViewById(R.id.chat_speciality);



        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listof_chat_users, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            //JSONObject jsonObject = datAr.get(position);

            }
           catch(Exception e) {
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
                        .placeholder(R.drawable.profileavtar)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(header_img);
            }
        });
    }

}