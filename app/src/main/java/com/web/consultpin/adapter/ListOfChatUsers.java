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
import com.web.consultpin.Utilclass;
import com.web.consultpin.chat.ChatActivity;
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_list_of_event;
        ImageView chat_user_image;
        TextView chat_user_name, chat_speciality;


        public MyViewHolder(View view) {
            super(view);
            ll_list_of_event = view.findViewById(R.id.ll_list_of_event);
            chat_user_name = view.findViewById(R.id.chat_user_name);
            chat_speciality = view.findViewById(R.id.chat_speciality);
            chat_user_image = view.findViewById(R.id.chat_user_image);
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
            final JSONObject jsonObject = datAr.get(position);


            holder.chat_user_name.setText(jsonObject.getString("name"));

            showImage(jsonObject.getString("profile_pic"), holder.chat_user_image);
            holder.ll_list_of_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        Intent intent = new Intent(pActivity, ChatActivity.class);
                        intent.putExtra(Utilclass.appointment_id, jsonObject.getString("appointment_id"));
                        intent.putExtra(Utilclass.username, jsonObject.getString("name"));
                        intent.putExtra(Utilclass.receiver_id, jsonObject.getString("receiver_user_id"));
                        intent.putExtra(Utilclass.sender_id, jsonObject.getString("sender_user_id"));
                        intent.putExtra(Utilclass.callFrom, "chatList");

                        if(jsonObject.has("sender_consultant_id"))
                        {
                            intent.putExtra(Utilclass.sender_consultant_id, jsonObject.getString("sender_consultant_id"));
                            intent.putExtra(Utilclass.receiver_consultant_id, "0");

                        }
                        else if(jsonObject.has("receiver_consultant_id"))
                        {

                            intent.putExtra(Utilclass.sender_consultant_id, "0");
                            intent.putExtra(Utilclass.receiver_consultant_id, jsonObject.getString("receiver_consultant_id"));
                        }
                        pActivity.startActivity(intent);

                       }
                      catch(Exception e)
                       {
                        e.printStackTrace();
                       }
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

        System.out.println("Chat data==="+url);
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