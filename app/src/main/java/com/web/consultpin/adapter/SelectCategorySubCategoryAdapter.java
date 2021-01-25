package com.web.consultpin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.consultant.AccountInformation;
import com.web.consultpin.consultant.BecomeAConsultant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class SelectCategorySubCategoryAdapter extends RecyclerView.Adapter<SelectCategorySubCategoryAdapter.MyViewHolder> {

    private JSONArray datAr;
    private AppCompatActivity pActivity;
    private String imageUrl="";
    private int selectionType=0;
    private  CheckBox commonChekBox;



    public SelectCategorySubCategoryAdapter(JSONArray ar, AppCompatActivity paActiviity, String url,int selectionTYpe) {
        datAr = ar;
        pActivity = paActiviity;
        imageUrl=url;
        selectionType=selectionTYpe;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_best_restaurant;
        ImageView category_image;
        TextView category_name;
        CheckBox chk_selectcategory;

        public MyViewHolder(View view) {
            super(view);


            ll_best_restaurant = view.findViewById(R.id.ll_best_restaurant);
            category_image = view.findViewById(R.id.category_image);
            category_name = view.findViewById(R.id.category_name);
            chk_selectcategory = view.findViewById(R.id.chk_selectcategory);



        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_category_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {

            final JSONObject jsonObject=datAr.getJSONObject(position);

             if(pActivity instanceof  BecomeAConsultant)
             {
                 holder.category_name.setText(jsonObject.getString("category_name"));
                 showImage(imageUrl+""+jsonObject.getString("category_icon"),holder.category_image);
                 holder.ll_best_restaurant.setTag(position);

                 holder.chk_selectcategory.setTag(jsonObject.getString("category_id"));
                 holder.chk_selectcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                     @Override
                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                         try {
                             if (selectionType == 0) {
                                 if (commonChekBox != null) {
                                     commonChekBox.setChecked(false);
                                 }
                                 commonChekBox = holder.chk_selectcategory;
                                 ((BecomeAConsultant) pActivity).category_id = buttonView.getTag() + "";
                                 ((BecomeAConsultant) pActivity).selected_category_str = jsonObject.getString("category_name");
                             } else if (selectionType == 1) {
                                 if (((BecomeAConsultant) pActivity).subcategory_id_Ar.size() == 2) {
                                     buttonView.setChecked(false);
                                     ((BecomeAConsultant) pActivity).alertDialogs.alertDialog(pActivity, pActivity.getResources().getString(R.string.Response), pActivity.getResources().getString(R.string.category_count), pActivity.getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                         @Override
                                         public void getDialogEvent(String buttonPressed) {

                                         }
                                     });
                                 } else {
                                     ((BecomeAConsultant) pActivity).selected_sub_category_str = ((BecomeAConsultant) pActivity).selected_sub_category_str + "," + jsonObject.getString("category_name");
                                     ((BecomeAConsultant) pActivity).subcategory_id_Ar.put(buttonView.getTag().toString(), buttonView.getTag().toString());
                                 }

                             }

                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 });

             }
             else if(pActivity instanceof AccountInformation)
             {
                 holder.category_name.setText(jsonObject.getString("name"));
                 holder.category_image.setVisibility(View.GONE);
                 holder.ll_best_restaurant.setTag(position);
                 holder.chk_selectcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                     @Override
                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                         try {
                             if (selectionType == 0) {
                                 if (commonChekBox != null) {
                                     commonChekBox.setChecked(false);
                                 }
                                 commonChekBox = holder.chk_selectcategory;
                                 ((AccountInformation) pActivity).tv_your_buisness.setText(jsonObject.getString("name"));

                             } else if (selectionType == 1) {
                                 if (commonChekBox != null)
                                 {
                                     commonChekBox.setChecked(false);
                                 }
                                 commonChekBox = holder.chk_selectcategory;
                                 ((AccountInformation) pActivity).tv_select_bank.setText(jsonObject.getString("name"));
                             }

                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 });
             }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return datAr.length();
    }


    private void showImage(final String url, final ImageView header_img) {
        pActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(pActivity)
                        .load(url)
                        .placeholder(R.drawable.profileavtar)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(header_img);
            }
        });


    }



//            "category_id": "7",
//                    "category_name": "test4",
//                    "category_icon": "",
//                    "parent_id": "1",
//                    "status": "1"
}