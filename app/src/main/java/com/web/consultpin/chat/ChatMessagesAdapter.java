package com.web.consultpin.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;
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

        public MyViewHolder(View view) {
            super(view);
            ll_myview = view.findViewById(R.id.ll_myview);
            txt_my_text = view.findViewById(R.id.txt_my_text);
            txt_mytime = view.findViewById(R.id.txt_mytime);

            ll_you = view.findViewById(R.id.ll_you);
            txt_you = view.findViewById(R.id.txt_you);
            txt_you_time = view.findViewById(R.id.txt_you_time);



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


//            "id": "112",
//                    "user_id": "0",
//                    "consultant_id": "0",
//                    "appointment_id": "0",
//                    "sender_id": "93",
//                    "receiver_id": "72",
//                    "message": "Hi chander sir",
//                    "attachment_name": "",
//                    "file_ext": "",
//                    "mime_type": "",
//                    "message_date_time": "2021-03-15 13:59:32",
//                    "ip_address": "106.66.42.13"

            if(pos.getString("sender_id").equalsIgnoreCase(ira1.getRestParamsName(Utilclass.user_id)))
            {
                holder.txt_my_text.setText(pos.getString("message"));
                holder.txt_mytime.setText(pos.getString("message_date_time"));
                holder.ll_myview.setVisibility(View.VISIBLE);
                holder.ll_you.setVisibility(View.GONE);
            }
            else
            {
                holder.txt_you.setText(pos.getString("message"));
                holder.txt_you_time.setText(pos.getString("message_date_time"));
                holder.ll_myview.setVisibility(View.GONE);


            }


//            "TicketId": "57d7f5eb-df1a-4ac5-b95c-2206d3b43cef",
//                    "Message": "description",
//                    "LastUpdated": "Tuesday, 16 March 2021 10:41",
//                    "status": "Success",
//                    "ErrorMessage": "List Found",
//                    "AttachFile": "",
//                    "CreatedDate": "Tuesday, 16 March 2021 10:41",
//                    "Title": "hello sub",
//                    "Type": 1,
//                    "IsMainMessage": true



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


}