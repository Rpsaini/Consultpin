package com.web.consultpin.consultant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.SimpleDialog;
import com.web.consultpin.R;
import com.web.consultpin.adapter.SelectCategorySubCategoryAdapter;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class AccountInformation extends BaseActivity {

    private RelativeLayout rr_select_your_buisness,rr_select_bank;
    public   TextView tv_your_buisness,tv_select_bank;
   private EditText ed_firstname,ed_lastname,ed_nationid_number,ed_email,ed_iban;
   private RadioButton terms_radio;
   private int selectionType;
   private RecyclerView select_category_recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
        getSupportActionBar().hide();
        initiateObj();
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

        TextView toolbarTitle =findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.becom_consultant));

         String ed_indivisual=getIntent().getStringExtra("ed_indivisual");
        String ed_taskmoreabout=getIntent().getStringExtra("ed_taskmoreabout");
        String ed_specialist=getIntent().getStringExtra("ed_specialist");
        String category_id=getIntent().getStringExtra("category_id");
        String sub_category_id=getIntent().getStringExtra("sub_category_id");
        String txt_select_price_tl=getIntent().getStringExtra("txt_select_price_tl");


        rr_select_your_buisness=findViewById(R.id.rr_select_your_buisness);
        tv_your_buisness=findViewById(R.id.tv_your_buisness);
        ed_firstname=findViewById(R.id.ed_firstname);
        ed_lastname=findViewById(R.id.ed_lastname);
        ed_nationid_number=findViewById(R.id.ed_nationid_number);
        ed_email=findViewById(R.id.ed_email);
        tv_select_bank=findViewById(R.id.tv_select_bank);
        rr_select_bank=findViewById(R.id.rr_select_bank);
        ed_iban=findViewById(R.id.ed_iban);
        terms_radio=findViewById(R.id.terms_radio);


        rr_select_your_buisness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionType=0;
                selectDialog(0);
            }
        });
        rr_select_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionType=1;
                selectDialog(1);
            }
        });


        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    private void selectDialog(int x)
    {
        try {

            SimpleDialog simpleDialog = new SimpleDialog();
            final Dialog selectCategoryDialog = simpleDialog.simpleDailog(AccountInformation.this, R.layout.select_category_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
            select_category_recycle = selectCategoryDialog.findViewById(R.id.select_category_recycler);
            ImageView img_hideview = selectCategoryDialog.findViewById(R.id.img_hideview);
            final RelativeLayout ll_relativelayout = selectCategoryDialog.findViewById(R.id.ll_relativelayout);
            final TextView select_title = selectCategoryDialog.findViewById(R.id.select_title);
            final TextView select_sub_title = selectCategoryDialog.findViewById(R.id.select_sub_title);
            final TextView tv_done = selectCategoryDialog.findViewById(R.id.tv_done);
            animateUp(ll_relativelayout);


            if (x == 0)//business type
            {
                select_title.setText(getResources().getString(R.string.select_buisness));
                select_sub_title.setText("");
                JSONArray buisnessTypeAr = new JSONArray();
                JSONObject type1 = new JSONObject();
                type1.put("name", "Private Firm");

                JSONObject type2 = new JSONObject();
                type2.put("name", "Individual");

                JSONObject type3 = new JSONObject();
                type3.put("name", "Company");

                buisnessTypeAr.put(type2);
                buisnessTypeAr.put(type1);
                buisnessTypeAr.put(type3);

                initHomeCategory("null",buisnessTypeAr);

            } else//bank list
            {
                select_title.setText(getResources().getString(R.string.select_your_bank));
                select_sub_title.setText("");

                JSONArray buisnessTypeAr = new JSONArray();
                JSONObject type1 = new JSONObject();
                type1.put("name", "ICICI Bank");

                JSONObject type2 = new JSONObject();
                type2.put("name", "HDFC bank");

                JSONObject type3 = new JSONObject();
                type3.put("name", "SBI bank");

                buisnessTypeAr.put(type2);
                buisnessTypeAr.put(type1);
                buisnessTypeAr.put(type3);

                initHomeCategory("null",buisnessTypeAr);
            }

            img_hideview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downSourceDestinationView(ll_relativelayout, selectCategoryDialog);
                }
            });

            tv_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downSourceDestinationView(ll_relativelayout,selectCategoryDialog);


                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void initHomeCategory(String imageUrl, JSONArray dataAr)
    {
        select_category_recycle.setNestedScrollingEnabled(false);
        select_category_recycle.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        select_category_recycle.setHasFixedSize(true);
        select_category_recycle.setItemAnimator(new DefaultItemAnimator());
        SelectCategorySubCategoryAdapter horizontalCategoriesAdapter = new SelectCategorySubCategoryAdapter(dataAr,this,imageUrl,selectionType);
        select_category_recycle.setAdapter(horizontalCategoriesAdapter);


    }
}