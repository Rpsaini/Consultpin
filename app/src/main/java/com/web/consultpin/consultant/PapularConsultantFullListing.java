package com.web.consultpin.consultant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.web.consultpin.R;
import com.web.consultpin.adapter.CategoryConsultantListingAdapter;
import com.web.consultpin.adapter.NewCategoryAdapter;
import com.web.consultpin.main.BaseActivity;

import java.util.ArrayList;

public class PapularConsultantFullListing extends BaseActivity {
    private  RecyclerView fulldetail_listing_reycler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papular_consultant_full_listing);
        initiateObj();
        removeActionBar();
        changestatusBarColor(0);
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
        toolBarTitle.setText(getResources().getString(R.string.papular_consultant));
        fulldetail_listing_reycler =findViewById(R.id.fulldetail_listing_reycler);
        loadData();
    }


    private void loadData()
    {
        fulldetail_listing_reycler.setNestedScrollingEnabled(false);
        fulldetail_listing_reycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        fulldetail_listing_reycler.setHasFixedSize(true);
        fulldetail_listing_reycler.setItemAnimator(new DefaultItemAnimator());
        CategoryConsultantListingAdapter horizontalCategoriesAdapter = new CategoryConsultantListingAdapter(new ArrayList<>(),this,"");
        fulldetail_listing_reycler.setAdapter(horizontalCategoriesAdapter);
    }
}