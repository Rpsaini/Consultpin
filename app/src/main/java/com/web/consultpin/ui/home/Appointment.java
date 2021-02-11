//package com.web.consultpin.ui.home;
//
//import android.app.DatePickerDialog;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.DatePicker;
//import android.widget.GridLayout;
//import android.widget.GridView;
//import android.widget.TextView;
//
//import com.app.dialogsnpickers.DialogCallBacks;
//import com.app.vollycommunicationlib.CallBack;
//import com.app.vollycommunicationlib.UtilClass;
//import com.web.consultpin.MainActivity;
//import com.web.consultpin.R;
//import com.web.consultpin.Utilclass;
//import com.web.consultpin.adapter.AlreadyAddedTimeAdapter;
//import com.web.consultpin.adapter.AppointmentHistoryAdapter;
//import com.web.consultpin.adapter.CustomTimingGrid;
//import com.web.consultpin.registration.LoginActivity;
//
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//public class Appointment extends Fragment {
//
//    private View view;
//    private TextView ed_datefrom, ed_date_end;
//    private MainActivity mainActivity;
//
//    public Appointment() {
//        // Required empty public constructor
//    }
//
//
//    public static Appointment newInstance(String param1, String param2) {
//        Appointment fragment = new Appointment();
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_appointment, container, false);
//        mainActivity = (MainActivity) getActivity();
//        init();
//        viewTimingGrid();
//        listeners();
//        getAlreadyAddedTime();
//        return view;
//    }
//
//    private void init() {
//        ed_datefrom = view.findViewById(R.id.ed_datefrom);
//        ed_date_end = view.findViewById(R.id.ed_date_end);
//    }
//
//    private void listeners() {
//        ed_datefrom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startDate((TextView) v);
//            }
//        });
//
//        ed_date_end.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startDate((TextView) v);
//            }
//        });
//    }
//
//    private void viewTimingGrid() {
//        GridView grid_timing = view.findViewById(R.id.grid_timing);
//        final ArrayList<Integer> keysAr = new ArrayList<>();
//        for (int x = 1; x <= 29; x++) {
//            keysAr.add(x);
//        }
////        keysAr.add(0);
//        grid_timing.setAdapter(new CustomTimingGrid((MainActivity) getActivity(), keysAr));
//    }
//
//    private void startDate(final TextView textView) {
//        final Calendar myCalendar = Calendar.getInstance();
//
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                String myFormat = "yyyy-MM-dd"; //In which you need put here
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//                textView.setText(sdf.format(myCalendar.getTime()));
//            }
//
//        };
//    }
//
//    private void showAlreadyAddedTime() {
//        RecyclerView recyclerview_alreadyAddedTime = view.findViewById(R.id.recyclerview_alreadyAddedTime);
//
//        recyclerview_alreadyAddedTime.setNestedScrollingEnabled(false);
//        recyclerview_alreadyAddedTime.setLayoutManager(new LinearLayoutManager(getActivity(),
//                LinearLayoutManager.VERTICAL, false));
//        recyclerview_alreadyAddedTime.setHasFixedSize(true);
//        recyclerview_alreadyAddedTime.setItemAnimator(new DefaultItemAnimator());
//        AlreadyAddedTimeAdapter horizontalCategoriesAdapter = new AlreadyAddedTimeAdapter(new ArrayList<>(), mainActivity);
//        recyclerview_alreadyAddedTime.setAdapter(horizontalCategoriesAdapter);
//
//    }
//
//
//    private void getAlreadyAddedTime() {
//
//
//        try {
//            final Map<String, String> m = new HashMap<>();
//            m.put("consultant_id", mainActivity.getRestParamsName("user_id"));
//
//            m.put("device_type", "android");
//            m.put("device_token", mainActivity.getDeviceToken() + "");
//
//            final Map<String, String> obj = new HashMap<>();
//            obj.put("token", mainActivity.getRestParamsName(Utilclass.token) + "");
//
//
//            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
//                @Override
//                public void getRespone(String dta, ArrayList<Object> respons) {
//                    try {
//                        System.out.println("Appointment data===" + dta);
//                        JSONObject jsonObject = new JSONObject(dta);
//                        if (jsonObject.getBoolean("status")) {
//
//                            try {
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                                @Override
//                                public void getDialogEvent(String buttonPressed) {
//                                }
//                            });
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//}