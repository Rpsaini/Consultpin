package com.web.consultpin.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.adapter.EventHistoryAdapter;
import com.web.consultpin.adapter.ListOfChatUsers;

import org.json.JSONObject;

import java.util.ArrayList;


public class Chat extends Fragment {
    private View view;
    private MainActivity mainActivity;

    public Chat() {
        // Required empty public constructor
    }

    public static Chat newInstance(String param1, String param2) {
        Chat fragment = new Chat();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        init(new ArrayList<>());
        return view;
    }


    private void init(ArrayList<JSONObject> dataAr) {
        RecyclerView recycler_chat_view = view.findViewById(R.id.recycler_chat_view);
        RelativeLayout relativeLayout = view.findViewById(R.id.rr_nodata_view);
        if (dataAr.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
            recycler_chat_view.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            recycler_chat_view.setVisibility(View.VISIBLE);
        }
        ListOfChatUsers mAdapter = new ListOfChatUsers(dataAr, mainActivity);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_chat_view.setLayoutManager(horizontalLayoutManagaer);
        recycler_chat_view.setItemAnimator(new DefaultItemAnimator());
        recycler_chat_view.setAdapter(mAdapter);
    }

}