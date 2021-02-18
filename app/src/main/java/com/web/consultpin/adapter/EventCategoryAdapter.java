package com.web.consultpin.adapter;

import android.content.Context;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.web.consultpin.R;
import com.web.consultpin.consultant.EventRequestActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;



public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.MyViewHolder> {
    private EventRequestActivity ira1;
    private JSONArray moviesList;
    Context mContext;




    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_reason;
        LinearLayout ll_selectReason;

        public MyViewHolder(View view) {
            super(view);
            txt_reason = view.findViewById(R.id.txt_reason);
            ll_selectReason = view.findViewById(R.id.ll_selectReason);




        }
    }


    public EventCategoryAdapter(JSONArray moviesList, EventRequestActivity ct) {
        this.moviesList = moviesList;
        this.ira1 = ct;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            final JSONObject pos = (JSONObject) moviesList.get(position);
            holder.txt_reason.setText(pos.getString("category_name"));
            //Id
            holder.ll_selectReason.setTag(pos.getString("id"));
            holder.ll_selectReason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
//                        if(type==0)
//                        {
//                            ira1.selected_reason_Id=pos.getString("Id");
//                            TextView txt_showselected_reason = ira1.findViewById(R.id.txt_showselected_reason);
//                            txt_showselected_reason.setText(pos.getString("Title"));
//                        }
//                        else
//                        {   ira1.selected_reference=pos.getString("Id");
//                            TextView txt_showselected_reference = ira1.findViewById(R.id.txt_showselected_reference);
//                            txt_showselected_reference.setText(pos.getString("Title"));
//                            if(pos.getString("Title").equalsIgnoreCase("Other"))
//                            {
//                                ira1.ll_if_other.setVisibility(View.VISIBLE);
//                            }
//                            else
//                            {
//                                ira1.ll_if_other.setVisibility(View.GONE);
//                            }
//
//                        }
//
//                        ira1.downSourceDestinationView(ira1.downView,ira1.reasonDialog);
                    } catch (Exception e) {
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
        return moviesList.length();
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