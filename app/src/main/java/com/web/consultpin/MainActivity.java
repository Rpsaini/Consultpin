package com.web.consultpin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import com.web.consultpin.consultant.AppointmentHistory;
import com.web.consultpin.consultant.BecomeAConsultant;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.ui.home.Appointment;
import com.web.consultpin.ui.home.Chat;
import com.web.consultpin.ui.home.Home;

import com.web.consultpin.ui.home.Profile;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends BaseActivity {

    private LinearLayout commonBottomBar;
//    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateObj();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        ImageView nav_oprn_toolbar =findViewById(R.id.nav_oprn_toolbar);

        nav_oprn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        navigationMenu();

        leftsidemenuclicks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp()
//    {
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
////                || super.onSupportNavigateUp();
//    }


    private void navigationMenu()
    {


        callFragment(new Home(),"home");
        LinearLayout bnave_ll_home = findViewById(R.id.bnave_ll_home);
        LinearLayout bnave_ll_chat = findViewById(R.id.bnave_ll_chat);
        LinearLayout bnave_ll_appontment = findViewById(R.id.bnave_ll_appontment);
        LinearLayout bnave_ll_profile = findViewById(R.id.bnave_ll_profile);
        commonBottomBar=bnave_ll_home;
        doBottomBarSelection(bnave_ll_home);
        bnave_ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBottomBarSelection(bnave_ll_home);
                commonBottomBar=bnave_ll_home;
                callFragment(new Home(),"home");
            }
        });


        bnave_ll_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBottomBarSelection(bnave_ll_chat);
                commonBottomBar=bnave_ll_chat;
                callFragment(new Chat(),"chat");
            }
        });


        bnave_ll_appontment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBottomBarSelection(bnave_ll_appontment);
                commonBottomBar=bnave_ll_appontment;
                callFragment(new Appointment(),"appointment");
            }
        });

        bnave_ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBottomBarSelection(bnave_ll_profile);
                commonBottomBar=bnave_ll_profile;
                callFragment(new Profile(),"profile");
            }
        });


        findViewById(R.id.become_consultant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, BecomeAConsultant.class);
                startActivity(intent);
            }
        });

    }
    private void callFragment(Fragment fragment,String tag)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.ll_fragment_container, fragment, tag);
        fragmentTransaction.commit();

    }

    private void doBottomBarSelection(LinearLayout bnave_ll_home)
    {
        unselectBottomBar();
        int childCount=bnave_ll_home.getChildCount();
        for(int x=0;x<childCount;x++)
        {
            View view=bnave_ll_home.getChildAt(x);
            if(view instanceof TextView)
            {
                ((TextView)view).setTextColor(getResources().getColor(R.color.buttonskyblue));
            }
            if(view instanceof ImageView)
            {
                ((ImageView)view).setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.buttonskyblue), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }
    private void unselectBottomBar()
    {
        int childCount=commonBottomBar.getChildCount();
        for(int x=0;x<childCount;x++)
        {
            View view=commonBottomBar.getChildAt(x);
            if(view instanceof TextView)
            {
                ((TextView)view).setTextColor(getResources().getColor(R.color.bottom_navigation_color));
            }
            if(view instanceof ImageView)
            {
                ((ImageView)view).setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.bottom_navigation_color), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }


    private void leftsidemenuclicks()
    {
        NavigationView navigationView = findViewById(R.id.nav_view);
       TextView appointmentHistory =navigationView.findViewById(R.id.tv_appointmenthistory);
       appointmentHistory.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this, AppointmentHistory.class);
               startActivity(intent);
           }
       });
    }


}