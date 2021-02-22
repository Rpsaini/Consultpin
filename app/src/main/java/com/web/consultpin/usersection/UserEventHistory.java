package com.web.consultpin.usersection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.web.consultpin.R;
import com.web.consultpin.events.ListOfEventsFragment;
import com.web.consultpin.main.BaseActivity;

public class UserEventHistory extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event_history);
        initiateObj();
        getSupportActionBar().hide();
        init();
        backActions();
    }
    private void backActions()
    {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.event_request));
        findViewById(R.id.toolbar_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        callFragment(new ListOfEventsFragment(),"events");
    }
    private void init()
    {


    }
    private void callFragment(Fragment fragment, String tag)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.ll_container, fragment, tag);

        fragmentTransaction.commit();

    }
}