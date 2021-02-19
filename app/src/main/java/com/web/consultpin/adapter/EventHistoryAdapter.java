package com.web.consultpin.adapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.AppointmentHistory;
import com.web.consultpin.consultant.PapularConsultantFullListing;
import com.web.consultpin.events.EventRequestActivity;
import com.web.consultpin.initiatecall.InitiateCallWebview;
import com.web.consultpin.jitsi.MAinJistsiActivity;
//import com.web.consultpin.initiatecall.InitiateCallWebview;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHistoryAdapter extends RecyclerView.Adapter<EventHistoryAdapter.MyViewHolder> {

    private ArrayList<JSONObject> datAr;
    private EventRequestActivity pActivity;
    private String imageUrl = "";


    public EventHistoryAdapter(ArrayList<JSONObject> ar, EventRequestActivity paActiviity) {
        datAr = ar;
        pActivity = paActiviity;



    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_list_of_event;
        ImageView event_image;
        TextView event_name, event_fee, event_number_of_user, event_time;


        public MyViewHolder(View view) {
            super(view);


            ll_list_of_event = view.findViewById(R.id.ll_list_of_event);
            event_name = view.findViewById(R.id.event_name);
            event_fee = view.findViewById(R.id.event_fee);
            event_image = view.findViewById(R.id.event_image);
            event_number_of_user = view.findViewById(R.id.event_number_of_user);
            event_time = view.findViewById(R.id.event_time);



        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {


            JSONObject jsonObject = datAr.get(position);
//            final String appointment_id = jsonObject.getString("appointment_id");
//            String user_name = jsonObject.getString("user_name");
//            String duration = jsonObject.getString("appointment_duration");
//            String appointmentTime = jsonObject.getString("appointment_date") + " " + jsonObject.getString("appointment_time");
//            String fee = jsonObject.getString("appointment_fee");
//            String appointment_status = jsonObject.getString("appointment_status");
//            final String profilePic = jsonObject.getString("profile_pic");
//            final String consultant_id = jsonObject.getString("consultant_id");
//            final String meeting_url = jsonObject.getString("meeting_url");


            //0 pending  1 apporved,2 rejected,3 cancelled,4 completed

//            if(pActivity.getRestParamsName(Utilclass.consultant_id).equalsIgnoreCase("0"))
//            {
//                if (appointment_status.equalsIgnoreCase("0"))
//                {
//                    holder.ll_status.setVisibility(View.GONE);
//                    holder.appointment_status.setText(pActivity.getString(R.string.pending));
//                    holder.txt_cancel.setVisibility(View.VISIBLE);
//
//                    holder.txt_cancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            showConfirmDialog(appointment_id, pActivity.getResources().getString(R.string.cancel_appointment), "3", position);
//                        }
//                    });
//                }
//                else {
//                    holder.ll_status.setVisibility(View.GONE);
//                    holder.txt_cancel.setVisibility(View.GONE);
//                    if (appointment_status.equalsIgnoreCase("1")) {
//                        holder.appointment_status.setText(pActivity.getString(R.string.Approved));
//                        holder.appointment_status.setTextColor(pActivity.getResources().getColor(R.color.green_color));
//                        holder.ll_status.setVisibility(View.INVISIBLE);
//                        holder.ll_best_restaurant.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                showUserDetails(user_name, profilePic, fee, appointment_id, consultant_id, meeting_url);
//
//
//                            }
//                        });
//                    } else if (appointment_status.equalsIgnoreCase("2")) {
//                        holder.appointment_status.setText(pActivity.getString(R.string.Rejected));
//                        holder.appointment_status.setTextColor(pActivity.getResources().getColor(R.color.yellow_color));
//                    } else if (appointment_status.equalsIgnoreCase("3")) {
//                        holder.appointment_status.setText(pActivity.getString(R.string.Cancelled));
//                        holder.appointment_status.setTextColor(pActivity.getResources().getColor(R.color.red_color));
//                    } else if (appointment_status.equalsIgnoreCase("5")) {
//                        holder.appointment_status.setText(pActivity.getString(R.string.Completed));
//                        holder.appointment_status.setTextColor(pActivity.getResources().getColor(R.color.green_color));
//                    }
//                }
//
//            }
//            else
//            {
//                if (appointment_status.equalsIgnoreCase("0"))
//                {
//                    holder.ll_status.setVisibility(View.VISIBLE);
//                    holder.txt_cancel.setVisibility(View.GONE);
//                    holder.appointment_status.setText(pActivity.getString(R.string.pending));
//                }
//                else {
//                    holder.ll_status.setVisibility(View.GONE);
//                    holder.txt_cancel.setVisibility(View.GONE);
//                    if(appointment_status.equalsIgnoreCase("1")) {
//                        holder.appointment_status.setText(pActivity.getString(R.string.Approved));
//                        holder.appointment_status.setTextColor(pActivity.getResources().getColor(R.color.green_color));
//                        holder.ll_status.setVisibility(View.INVISIBLE);
//                        holder.ll_best_restaurant.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                showUserDetails(user_name, profilePic, fee, appointment_id, consultant_id, meeting_url);
//
//
//                            }
//                        });
//                    } else if (appointment_status.equalsIgnoreCase("2")) {
//                        holder.appointment_status.setText(pActivity.getString(R.string.Rejected));
//                        holder.appointment_status.setTextColor(pActivity.getResources().getColor(R.color.yellow_color));
//                    } else if (appointment_status.equalsIgnoreCase("3")) {
//                        holder.appointment_status.setText(pActivity.getString(R.string.Cancelled));
//                        holder.appointment_status.setTextColor(pActivity.getResources().getColor(R.color.red_color));
//                    } else if (appointment_status.equalsIgnoreCase("5")) {
//                        holder.appointment_status.setText(pActivity.getString(R.string.Completed));
//                        holder.appointment_status.setTextColor(pActivity.getResources().getColor(R.color.green_color));
//                    }
//                }
//            }
//
//
//            holder.appointment_time.setText(appointmentTime);
//            holder.consultant_name.setText(user_name);
//            holder.time_duration.setText(duration + " Minutes");
//            holder.amount.setText(fee + pActivity.getResources().getString(R.string.lirasymbol));
//            showImage(profilePic, holder.consultant_image);
//
//
//            holder.txt_reject.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    showConfirmDialog(appointment_id, pActivity.getResources().getString(R.string.reject_confirmation), "2", position);
//
//                }
//            });
//
//            holder.txt_accept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showConfirmDialog(appointment_id, pActivity.getResources().getString(R.string.accept_confirmation), "1", position);
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


//    private void showConfirmDialog(final String Id, final String Msg, final String status, int postion) {
//        SimpleDialog simpleDialog = new SimpleDialog();
//        Dialog dialog = simpleDialog.simpleDailog(pActivity, R.layout.confirm_dialog, new ColorDrawable(android.graphics.Color.TRANSPARENT), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
//
//        TextView txt_messagee = dialog.findViewById(R.id.txt_messagee);
//        TextView txt_no = dialog.findViewById(R.id.txt_no);
//        TextView txt_yes = dialog.findViewById(R.id.txt_yes);
//
//        txt_messagee.setText(Msg);
//
//        txt_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        txt_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    final Map<String, String> m = new HashMap<>();
//                    m.put("appointment_id", Id);
//                    m.put("status", status);
//                    m.put("device_type", "android");
//                    m.put("device_token", pActivity.getDeviceToken() + "");
//
//                    final Map<String, String> obj = new HashMap<>();
//                    obj.put("token", pActivity.getRestParamsName(Utilclass.token));
//
//                    pActivity.serverHandler.sendToServer(pActivity, pActivity.getApiUrl() + "update-appointment-status", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
//                        @Override
//                        public void getRespone(String dta, ArrayList<Object> respons) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(dta);
//                                if (jsonObject.getBoolean("status")) {
//                                    System.out.println("home dataa===" + jsonObject);
//                                    try {
//
//                                        try {
//                                            dialog.dismiss();
//                                            JSONObject dataObj = datAr.get(postion);
//                                            dataObj.remove("appointment_status");
//                                            dataObj.put("appointment_status", status);
//                                            notifyDataSetChanged();
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                } else {
//                                    pActivity.alertDialogs.alertDialog(pActivity, pActivity.getResources().getString(R.string.Response), jsonObject.getString("msg"), pActivity.getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                                        @Override
//                                        public void getDialogEvent(String buttonPressed) {
//                                        }
//                                    });
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//    }


//    private void showUserDetails(String username, String userimage, String fee, String appointmentId, String consultant_id, String videocallUrl) {
//        try {
//
//
//            SimpleDialog simpleDialog = new SimpleDialog();
//            Dialog dialog = simpleDialog.simpleDailog(pActivity, R.layout.dialog_call_initiate, new ColorDrawable(android.graphics.Color.TRANSPARENT), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
//            ImageView img_hide = dialog.findViewById(R.id.img_hide);
//            ImageView txt_user_profile_img = dialog.findViewById(R.id.txt_user_profile_img);
//            TextView txt_username = dialog.findViewById(R.id.txt_username);
//            TextView txt_chat = dialog.findViewById(R.id.txt_chat);
//            TextView txt_videochat = dialog.findViewById(R.id.txt_videochat);
//            TextView txt_fee = dialog.findViewById(R.id.txt_fee);
//
//            txt_username.setText(username);
//            showImage(userimage, txt_user_profile_img);
//            txt_fee.setText(fee + " " + pActivity.getResources().getString(R.string.lirasymbol));
//
//
//            //jsonObject.getString("");
//
//            img_hide.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            txt_videochat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(pActivity, MAinJistsiActivity.class);
//                    intent.putExtra(Utilclass.appointment_id, appointmentId);
//                    intent.putExtra(Utilclass.consultant_id, consultant_id);
//                    intent.putExtra(Utilclass.videocall, videocallUrl);
//                    intent.putExtra(Utilclass.first_name, username);
//                    intent.putExtra(Utilclass.imageUrl, userimage);
//
//                    pActivity.startActivityForResult(intent,Utilclass.appointmentRequsestcode);
//                    dialog.dismiss();
//
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}