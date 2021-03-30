package com.web.consultpin.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MyViewHolder> {
    private ChatActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
    private int type=0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_myview,ll_you;
        TextView txt_my_text,txt_mytime,txt_you,txt_you_time;
        ImageView img_youfile,img_myfile;

        public MyViewHolder(View view) {
            super(view);
            ll_myview = view.findViewById(R.id.ll_myview);
            txt_my_text = view.findViewById(R.id.txt_my_text);
            txt_mytime = view.findViewById(R.id.txt_mytime);

            ll_you = view.findViewById(R.id.ll_you);
            txt_you = view.findViewById(R.id.txt_you);
            txt_you_time = view.findViewById(R.id.txt_you_time);

            img_youfile = view.findViewById(R.id.img_youfile);
            img_myfile = view.findViewById(R.id.img_myfile);



//            Typeface face=Typeface.createFromAsset(ira1.getAssets(), "MontserratRegular.ttf");


        }
    }


    public ChatMessagesAdapter(ArrayList<JSONObject> moviesList, ChatActivity ct) {
        this.moviesList = moviesList;
        this.ira1 = ct;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            final JSONObject pos = (JSONObject) moviesList.get(position);


            if(pos.getString("sender_id").equalsIgnoreCase(ira1.getRestParamsName(Utilclass.user_id)))
            {
                holder.txt_my_text.setText(pos.getString("message"));
                holder.txt_mytime.setText(pos.getString("message_date_time"));
                holder.ll_myview.setVisibility(View.VISIBLE);
                holder.ll_you.setVisibility(View.GONE);


                if(pos.getString("message").length()==0)
                {
                    holder.txt_my_text.setVisibility(View.GONE);
                    holder.img_myfile.setVisibility(View.VISIBLE);
                    showImage(pos.getString("attachment"),holder.img_myfile);

                }
                else
                {
                    holder.txt_my_text.setVisibility(View.VISIBLE);
                    holder.img_myfile.setVisibility(View.GONE);
                }

            }
            else
            {
                holder.txt_you.setText(pos.getString("message"));
                holder.txt_you_time.setText(pos.getString("message_date_time"));
                holder.ll_myview.setVisibility(View.GONE);

                if(pos.getString("message").length()==0)
                {
                    holder.txt_you.setVisibility(View.GONE);
                    holder.img_youfile.setVisibility(View.VISIBLE);
                    showImage(pos.getString("attachment"),holder.img_youfile);
                }
                else
                {
                    holder.txt_you.setVisibility(View.VISIBLE);
                    holder.img_youfile.setVisibility(View.GONE);
                }



            }





        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private void showImage(final String url, final ImageView header_img) {
        System.out.println("Image url==" + url);
        ira1.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(ira1)
                        .load(url)
                        .placeholder(R.drawable.placeholderimg)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(header_img);
            }
        });

    }
    }