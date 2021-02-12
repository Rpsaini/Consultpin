package com.web.consultpin.consultant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.web.consultpin.R;
import com.web.consultpin.main.BaseActivity;

public class EventRequestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_request);
        initiateObj();
        getSupportActionBar().hide();
        init();
    }
    private void init()
    {

        EditText ed_about_event =findViewById(R.id.ed_about_event);
        TextView ed_datefrom =findViewById(R.id.ed_datefrom);
        TextView ed_date_end =findViewById(R.id.ed_date_end);
        TextView txt_event_category =findViewById(R.id.txt_event_category);
        ImageView img_ispaid =findViewById(R.id.img_ispaid);
        EditText ed_paid_fee =findViewById(R.id.ed_paid_fee);
        EditText ed_numberofdays =findViewById(R.id.ed_numberofdays);
        TextView selected_image =findViewById(R.id.selected_image);
        TextView tv_save =findViewById(R.id.tv_save);

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
          });

      }

   }