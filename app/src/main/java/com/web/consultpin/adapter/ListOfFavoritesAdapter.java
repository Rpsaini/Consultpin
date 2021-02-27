package com.web.consultpin.adapter;

import android.content.Intent;
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
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.ConsultantDetailView;
import com.web.consultpin.consultant.ListFavouritesdata;
import com.web.consultpin.consultant.PapularConsultantFullListing;

import org.json.JSONObject;

import java.util.ArrayList;


public class ListOfFavoritesAdapter extends RecyclerView.Adapter<ListOfFavoritesAdapter.MyViewHolder>
{

    private ArrayList<JSONObject> datAr;
    private ListFavouritesdata pActivity;
    private String imageUrl="";


    public ListOfFavoritesAdapter(ArrayList<JSONObject> ar, ListFavouritesdata paActiviity, String url) {
        datAr = ar;
        pActivity = paActiviity;
        imageUrl=url;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_category_listing;
        ImageView fav_consultant_image;
        TextView fav_consultant_name,fav_specility;

        public MyViewHolder(View view) {
            super(view);

            fav_consultant_image = view.findViewById(R.id.fav_consultant_image);
            fav_consultant_name = view.findViewById(R.id.fav_consultant_name);
            fav_specility = view.findViewById(R.id.fav_specility);



        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_of_favorites_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {



            JSONObject jsonObject=datAr.get(position);
            holder.fav_consultant_name.setText(jsonObject.getString("name"));
            holder.fav_specility.setText(jsonObject.getString("category_name"));
            showImage(imageUrl+""+jsonObject.getString("profile_pic"),holder.fav_consultant_image);


//            "user_id": "77",
//                    "profile_pic": "http:\/\/webcomclients.in\/consultpindev\/assets\/uploads\/e42bd3197f58fa17570eb6158142629e.jpg",
//                    "name": "rohit kumar",
//                    "category_name": "Relationship Astrology,Mundane Astrology",
//                    "main_category": "Astrology",
//                    "consultant_id": "63"


//            holder.ll_category_listing.setTag(position);
//            holder.ll_category_listing.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    try {
//
//                        Intent intent=new Intent(pActivity, ConsultantDetailView.class);
//                        intent.putExtra(Utilclass.user_id,jsonObject.getString("user_id"));
//                        intent.putExtra(Utilclass.consultant_id,jsonObject.getString("consultant_id"));
//                        pActivity.startActivityForResult(intent,Utilclass.appointmentRequsestcode);
//
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            });




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