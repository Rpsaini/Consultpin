package com.web.consultpin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import com.web.consultpin.consultant.BecomeAConsultant;
import com.web.consultpin.consultant.ListFavouritesdata;
import com.web.consultpin.consultant.TimeManagement;
import com.web.consultpin.events.EventRequestActivity;

import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.appointmenthistory.AppointMentHistoryFrg;
//import com.web.consultpin.ui.home.Appointment;
import com.web.consultpin.registration.SplashScreen;
import com.web.consultpin.ui.home.Chat;
import com.web.consultpin.ui.home.ConsultantHome;
import com.web.consultpin.ui.home.ContactUs;
import com.web.consultpin.ui.home.Home;

import com.web.consultpin.ui.home.Profile;
import com.web.consultpin.usersection.UserEventHistory;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

//cTrf8iWVRnCYhbhAY2d3bn:APA91bGxxQOpA6FoDgO7VpEphA3xg-mlNzzDtL-TgP--E0_WnK-kRXyomuF3OUOVFuCoAh7EuRXgCMFFSMzz9uXU4kjegRFzrW5nnkX0cdPpsZzLqNvwzuHfj6seK8__VxRwY9dOzlk4
public class MainActivity extends BaseActivity {

    private LinearLayout commonBottomBar;
    DrawerLayout drawer;
    private Fragment commonFragments;
    private String frgtag="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateObj();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

//        Intent intent = new Intent(MainActivity.this, BecomeAConsultant.class);
//        startActivity(intent);

        ImageView nav_oprn_toolbar = findViewById(R.id.nav_oprn_toolbar);
        nav_oprn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


        navigationMenu();

        switchUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void navigationMenu() {




        callFragment(new Home(), "home");
        LinearLayout bnave_ll_home = findViewById(R.id.bnave_ll_home);
        LinearLayout bnave_ll_chat = findViewById(R.id.bnave_ll_chat);
        LinearLayout bnave_ll_appontment = findViewById(R.id.bnave_ll_appontment);
        LinearLayout bnave_ll_profile = findViewById(R.id.bnave_ll_profile);

        commonBottomBar = bnave_ll_home;
        doBottomBarSelection(bnave_ll_home);
        bnave_ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBottomBarSelection(bnave_ll_home);
                commonBottomBar = bnave_ll_home;
                if(Utilclass.isConsultantModeOn)
                {
                    callFragment(new ConsultantHome(), "chome");
                }
                else
                {
                    callFragment(new Home(), "home");
                }

            }
        });


        bnave_ll_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBottomBarSelection(bnave_ll_chat);
                commonBottomBar = bnave_ll_chat;
                callFragment(new Chat(), "chat");
            }
        });


        bnave_ll_appontment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBottomBarSelection(bnave_ll_appontment);
                commonBottomBar = bnave_ll_appontment;
                callFragment(new AppointMentHistoryFrg(), "appointment");
            }
        });

        bnave_ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBottomBarSelection(bnave_ll_profile);
                commonBottomBar = bnave_ll_profile;
                callFragment(new Profile(), "profile");
            }
        });


        findViewById(R.id.become_consultant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BecomeAConsultant.class);
                startActivity(intent);
            }
        });




    }

    private void callFragment(Fragment fragment, String tag)
    {
        this.commonFragments=fragment;
        this.frgtag=tag;

        Fragment frg=null;
        if(fragment instanceof Home)
        {
            if(Utilclass.isConsultantModeOn)
            {
                frg=new ConsultantHome();
            }
            else
            {
                frg=new Home();
            }

        }
        else if(fragment instanceof Chat)
        {
            frg=new Chat();
        }
        else if(fragment instanceof ConsultantHome)
        {
            if(Utilclass.isConsultantModeOn)
            {
                frg=new ConsultantHome();
            }
            else
            {
                frg=new Home();
            }

        }
        else if(fragment instanceof AppointMentHistoryFrg)
        {
            frg=new AppointMentHistoryFrg();
        }
        else if(fragment instanceof Profile)
        {
            frg=new Profile();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.ll_fragment_container, frg, tag);
        fragmentTransaction.commit();

    }

    private void doBottomBarSelection(LinearLayout bnave_ll_home) {
        unselectBottomBar();
        int childCount = bnave_ll_home.getChildCount();
        for (int x = 0; x < childCount; x++) {
            View view = bnave_ll_home.getChildAt(x);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.buttonskyblue));
            }
            if (view instanceof ImageView) {
                ((ImageView) view).setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.buttonskyblue), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }

    private void unselectBottomBar() {
        int childCount = commonBottomBar.getChildCount();
        for (int x = 0; x < childCount; x++) {
            View view = commonBottomBar.getChildAt(x);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.bottom_navigation_color));
            }
            if (view instanceof ImageView) {
                ((ImageView) view).setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.bottom_navigation_color), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }


    private void leftsidemenuclicks()
    {
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView tv_events = navigationView.findViewById(R.id.tv_events);
        TextView addAppointment = findViewById(R.id.tv_setAppointment);
        View view_line = findViewById(R.id.view_line);
        TextView tv_logout = findViewById(R.id.tv_logout);
        TextView tv_favorite = findViewById(R.id.tv_favorite);
        View view_fave = findViewById(R.id.view_fave);
        TextView tv_aboutus = findViewById(R.id.tv_aboutus);


        if (!Utilclass.isConsultantModeOn)
        {
            tv_events.setText(getResources().getString(R.string.view_events));
            addAppointment.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
            tv_favorite.setVisibility(View.VISIBLE);
            view_fave.setVisibility(View.VISIBLE);


            tv_events.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(MainActivity.this, UserEventHistory.class);
                    startActivity(intent);
                }
            });

        } else {
            System.out.println("Inside else===>");
            addAppointment.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            tv_favorite.setVisibility(View.GONE);
            view_fave.setVisibility(View.GONE);
            tv_events.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(MainActivity.this, EventRequestActivity.class);
                    startActivity(intent);
                }
            });

        }

        findViewById(R.id.tv_addappointment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                LinearLayout bnave_ll_appontment = findViewById(R.id.bnave_ll_appontment);
                doBottomBarSelection(bnave_ll_appontment);
                commonBottomBar = bnave_ll_appontment;
                callFragment(new AppointMentHistoryFrg(), "appointment");
            }
        });


        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, TimeManagement.class);
                startActivity(intent);
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogs.alertDialog(MainActivity.this, getResources().getString(R.string.logout), getResources().getString(R.string.logout_text), getResources().getString(R.string.yes), getResources().getString(R.string.no), new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                            if(buttonPressed.equalsIgnoreCase(getResources().getString(R.string.yes)))
                            {
                                logout();
                            }
                        }
                    });
              }
          });


        tv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent=new Intent(MainActivity.this, ListFavouritesdata.class);
                startActivity(intent);
            }
        });

        tv_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ContactUs.class);
                startActivity(intent);
            }
        });


      }

    public void logout()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();

        savePreferences.savePreferencesData(this,"done","tutorial");
        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
        startActivity(intent);
        finishAffinity();



    }




    private void switchUser()
     {

        int consultanmtId = Integer.parseInt(getRestParamsName(Utilclass.consultant_id));
        ImageView img_switch_to_user = findViewById(R.id.img_switch_to_user);
        TextView txt_switch_to_user = findViewById(R.id.txt_switch_to_user);
        LinearLayout ll_switch_to_user = findViewById(R.id.ll_switch_to_user);
        LinearLayout become_consultant = findViewById(R.id.become_consultant);


        if (consultanmtId == 0)
          {
            Utilclass.isConsultantModeOn = false;
            ll_switch_to_user.setVisibility(View.GONE);
            become_consultant.setVisibility(View.VISIBLE);
        } else {
            Utilclass.isConsultantModeOn = true;
            ll_switch_to_user.setVisibility(View.VISIBLE);
            become_consultant.setVisibility(View.GONE);
            txt_switch_to_user.setText(getResources().getString(R.string.switchtouser));
            img_switch_to_user.setImageResource(R.drawable.ic_button);
            img_switch_to_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utilclass.isConsultantModeOn)
                    {
                        txt_switch_to_user.setText(getResources().getString(R.string.switchtoconsultant));
                        img_switch_to_user.setImageResource(R.drawable.ic_unselect_button);
                        Utilclass.isConsultantModeOn = false;

                    } else {
                        txt_switch_to_user.setText(getResources().getString(R.string.switchtouser));
                        img_switch_to_user.setImageResource(R.drawable.ic_button);
                        Utilclass.isConsultantModeOn = true;
                    }


                    callFragment(commonFragments,frgtag);
                    leftsidemenuclicks();

                }
            });
        }
         setSideMenuForUser();
         leftsidemenuclicks();
    }


    private void setSideMenuForUser() {
        TextView nav_name = findViewById(R.id.nav_name);
        ImageView rr_homenav = findViewById(R.id.rr_homenav);
        ImageView profileimage = findViewById(R.id.profileimage);

        showImage(getRestParamsName("profile_pic"), rr_homenav);
        showImage(getRestParamsName("profile_pic"), profileimage);

        nav_name.setText(getRestParamsName("first_name") + getRestParamsName("last_name"));

    }

    private void showImage(final String url, final ImageView header_img) {
        System.out.println("Image url==" + url);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(MainActivity.this)
                        .load(url)
                        .placeholder(R.drawable.profileavtar)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(header_img);
            }
        });


    }


}