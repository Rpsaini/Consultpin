package com.web.consultpin.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.web.consultpin.R;
import com.web.consultpin.events.AddEventsFragment;
import com.web.consultpin.events.EventRequestActivity;

import org.json.JSONArray;
import org.json.JSONObject;


public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.MyViewHolder> {
    private EventRequestActivity ira1;
    private JSONArray moviesList;
    Context mContext;
    private AddEventsFragment addEventsFragment;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_reason;
        LinearLayout ll_selectReason;


        public MyViewHolder(View view) {
            super(view);
            txt_reason = view.findViewById(R.id.txt_reason);
            ll_selectReason = view.findViewById(R.id.ll_selectReason);

        }
    }


    public EventCategoryAdapter(JSONArray moviesList, EventRequestActivity ct, AddEventsFragment addEventsFragment) {
        this.moviesList = moviesList;
        this.ira1 = ct;
        this.addEventsFragment=addEventsFragment;


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

                        addEventsFragment.event_cat_id=v.getTag()+"";
                        addEventsFragment.downView();
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