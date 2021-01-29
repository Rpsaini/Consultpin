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
import com.web.consultpin.consultant.PapularConsultantFullListing;

import org.json.JSONObject;

import java.util.ArrayList;


public class CategoryConsultantListingAdapter extends RecyclerView.Adapter<CategoryConsultantListingAdapter.MyViewHolder> {

    private ArrayList<JSONObject> datAr;
    private PapularConsultantFullListing pActivity;
    private String imageUrl="";


    public CategoryConsultantListingAdapter(ArrayList<JSONObject> ar, PapularConsultantFullListing paActiviity, String url) {
        datAr = ar;
        pActivity = paActiviity;
        imageUrl=url;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_best_restaurant;
        ImageView consultant_image;
        TextView consultant_name,specialties;

        public MyViewHolder(View view) {
            super(view);


//            ll_best_restaurant = view.findViewById(R.id.ll_best_restaurant);
//            consultant_name = view.findViewById(R.id.consultant_name);
//            consultant_image = view.findViewById(R.id.consultant_image);
//            specialties = view.findViewById(R.id.specialties);



        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_consultant_listing, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {



//            JSONObject jsonObject=datAr.get(position);
//            holder.consultant_name.setText(jsonObject.getString("category_name"));
//            holder.specialties.setText(jsonObject.getString("specialties"));
//            showImage(imageUrl+""+jsonObject.getString("profile_pic"),holder.consultant_image);
//            holder.ll_best_restaurant.setTag(position);
//            holder.ll_best_restaurant.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    try {
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
        return datAr.size()+20;
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