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
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.ConsultantDetailView;
import com.web.consultpin.consultant.PapularConsultantFullListing;
import com.web.consultpin.usersection.SetAppointmentByUser;

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


        LinearLayout ll_category_listing;
        ImageView consultant_image;
        TextView consultant_name,specialties,txt_rate_count,txt_review_count,consultant_fee;

        public MyViewHolder(View view) {
            super(view);


            ll_category_listing = view.findViewById(R.id.ll_category_listing);
            consultant_image = view.findViewById(R.id.consultant_image);
            consultant_name = view.findViewById(R.id.consultant_name);
            consultant_fee = view.findViewById(R.id.consultant_fee);
            specialties = view.findViewById(R.id.experiance);
            txt_rate_count = view.findViewById(R.id.txt_rate_count);
            txt_review_count = view.findViewById(R.id.txt_review_count);



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



            JSONObject jsonObject=datAr.get(position);
            holder.consultant_name.setText(jsonObject.getString("name"));
            holder.specialties.setText(jsonObject.getString("specialties")+"\n"+jsonObject.getString("experience"));
            showImage(imageUrl+""+jsonObject.getString("profile_pic"),holder.consultant_image);
            holder.consultant_fee.setText(jsonObject.getString("rate")+pActivity.getResources().getString(R.string.lirasymbol));


//            "user_id": "74",
//                    "profile_pic": "http:\/\/webcomclients.in\/consultpindev\/assets\/uploads\/portrait-happy-young-handsome-indian-man-doctor-smiling-studio-shot-against-white-background-137823661.jpg",
//                    "name": "Web Consultant",
//                    "experience": "I am ayurvedic specialist",
//                    "specialties": "Bams",
//                    "rate": "300",
//                    "email": "webconsultant@gmail.com",
//                    "phone": "1234567899",
//                    "consultant_id": "54",
//                    "category": "8,10",
//                    "id": "54",
//                    "category_name": "ayurvedic,General Physician"


            holder.ll_category_listing.setTag(position);
            holder.ll_category_listing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        Intent intent=new Intent(pActivity, ConsultantDetailView.class);
                        intent.putExtra(Utilclass.user_id,jsonObject.getString("user_id"));
                        intent.putExtra(Utilclass.consultant_id,jsonObject.getString("consultant_id"));
                        pActivity.startActivityForResult(intent,Utilclass.appointmentRequsestcode);

                    }
                    catch (Exception e)
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