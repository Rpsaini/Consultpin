package com.web.consultpin.consultant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.web.consultpin.R;
import com.web.consultpin.adapter.AppointmentHistoryAdapter;
import com.web.consultpin.adapter.CategoryConsultantListingAdapter;
import com.web.consultpin.main.BaseActivity;

import java.util.ArrayList;

public class AppointmentHistory extends BaseActivity {

    private RecyclerView appointment_history_recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);
        initiateObj();
        removeActionBar();
        init();
    }

    private void init()
    {
        findViewById(R.id.toolbar_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView toolBarTitle =findViewById(R.id.toolbar_title);
        toolBarTitle.setText(getResources().getString(R.string.appointment_history));
        appointment_history_recycler =findViewById(R.id.appointment_history_recycler);
   //    loadData();
    }

//    private void loadData()
//    {
//        appointment_history_recycler.setNestedScrollingEnabled(false);
//        appointment_history_recycler.setLayoutManager(new LinearLayoutManager(this,
//                LinearLayoutManager.VERTICAL, false));
//        appointment_history_recycler.setHasFixedSize(true);
//        appointment_history_recycler.setItemAnimator(new DefaultItemAnimator());
//        AppointmentHistoryAdapter horizontalCategoriesAdapter = new AppointmentHistoryAdapter(new ArrayList<>(),mai,"");
//        appointment_history_recycler.setAdapter(horizontalCategoriesAdapter);
//    }
}