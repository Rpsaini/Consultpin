//package com.web.consultpin.ui.home;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.app.dialogsnpickers.DialogCallBacks;
//import com.app.vollycommunicationlib.CallBack;
//import com.web.consultpin.R;
//import com.web.consultpin.Utilclass;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomeFrg#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class HomeFrg extends Fragment {
//
//
//    public HomeFrg() {
//        // Required empty public constructor
//    }
//
//
//    public static HomeFrg newInstance(String param1, String param2) {
//        HomeFrg fragment = new HomeFrg();
//
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
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//
//
//    private void getDashboardData()
//    {
//
//        try {
//            final Map<String, String> m = new HashMap<>();
//            m.put("device_type", "android");
//            m.put("device_token", mainActivity.getDeviceToken()+"");
//
//            final Map<String, String> obj = new HashMap<>();
//            obj.put("Token", mainActivity.getRestParamsName(Utilclass.token));
//
//            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "dashboard", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
//                @Override
//                public void getRespone(String dta, ArrayList<Object> respons) {
//                    try {
//                        System.out.println("Dashboard data===="+dta);
//                        JSONObject jsonObject = new JSONObject(dta);
//                        if (jsonObject.getBoolean("status")) {
//
//                            try
//                            {
//
//                            }
//                            catch (Exception e)
//                            {
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
//    }
//}