package com.web.consultpin.consultant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.SelectCategorySubCategoryAdapter;
import com.web.consultpin.interfaces.ApiProduction;
import com.web.consultpin.interfaces.ImageUpload;
import com.web.consultpin.interfaces.RxAPICallHelper;
import com.web.consultpin.interfaces.RxAPICallback;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.registration.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.transform.Result;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AccountInformation extends BaseActivity {

    private RelativeLayout rr_select_your_buisness,rr_select_bank;
    public TextView tv_your_buisness,tv_select_bank,ed_firstname,ed_lastname,ed_email;
    private EditText ed_nationid_number,ed_city,ed_provience,ed_iban,ed_postal_Address,ed_tax_office,ed_companyname;
    private RadioButton terms_radio;
    private int selectionType;
    private RecyclerView select_category_recycle;
    public String accountTypeId="";
    private LinearLayout ll_company_name,ll_post_officenumber;


    private TextView tc_number;

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

        rr_select_your_buisness=findViewById(R.id.rr_select_your_buisness);
        tv_your_buisness=findViewById(R.id.tv_your_buisness);
        ed_firstname=findViewById(R.id.ed_firstname);
        ed_lastname=findViewById(R.id.ed_lastname);
        ed_nationid_number=findViewById(R.id.ed_nationid_number);
        ed_email=findViewById(R.id.ed_email);
        ed_city=findViewById(R.id.ed_city);
        ed_provience=findViewById(R.id.ed_provience);
        ed_postal_Address=findViewById(R.id.ed_postal_Address);
        ed_tax_office=findViewById(R.id.ed_tax_office);
        ll_company_name=findViewById(R.id.ll_company_name);
        ed_companyname=findViewById(R.id.ed_companyname);
        ll_post_officenumber=findViewById(R.id.ll_post_officenumber);

        tv_select_bank=findViewById(R.id.tv_select_bank);
        rr_select_bank=findViewById(R.id.rr_select_bank);
        ed_iban=findViewById(R.id.ed_iban);
        tc_number=findViewById(R.id.tc_number);
        terms_radio=findViewById(R.id.terms_radio);

        ed_firstname.setText(getRestParamsName("first_name"));
        ed_lastname.setText(getRestParamsName("last_name"));
        ed_email.setText(getRestParamsName("email"));


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
                if (tv_your_buisness.getText().toString().length() == 0) {
                    alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_buisness), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                System.out.println("Account type====>"+accountTypeId);
                if(accountTypeId.equalsIgnoreCase("2"))
                {
                    if (validationRule.checkEmptyString(ed_companyname) == 0) {
                        alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.entercompanyname), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {

                            }
                        });
                        return;
                    }

                    if (validationRule.checkEmptyString(ed_tax_office) == 0) {
                        alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_tax_office), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {

                            }
                        });
                        return;
                    }
                }




                if (validationRule.checkEmptyString(ed_nationid_number) == 0)
                {

                    String msg=getResources().getString(R.string.enter_national_idnumber);
                    if(accountTypeId.equalsIgnoreCase("2"))
                    {
                        msg=getResources().getString(R.string.enter_tc_number);
                    }

                    alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), msg, getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }


                if (validationRule.checkEmptyString(ed_city) == 0) {
                    alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_cityname), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }



                if (validationRule.checkEmptyString(ed_provience) == 0) {
                    alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_provience), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }


                if (validationRule.checkEmptyString(ed_postal_Address) == 0) {
                    alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.ed_postal_Address), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (tv_select_bank.getText().toString().length() == 0) {
                    alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_your_bank), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(ed_iban) == 0) {
                    alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_iban), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (!terms_radio.isChecked()) {
                    alertDialogs.alertDialog(AccountInformation.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_terms), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                else
                {
                    Map<String,String> map =new LinkedHashMap<>();

                    String ed_indivisual=getIntent().getStringExtra("ed_indivisual");
                    String ed_taskmoreabout=getIntent().getStringExtra("ed_taskmoreabout");
                    String ed_specialist=getIntent().getStringExtra("ed_specialist");
                    String category_id=getIntent().getStringExtra("category_id");
                    String sub_category_id=getIntent().getStringExtra("sub_category_id");
                    String txt_select_price_tl=getIntent().getStringExtra("txt_select_price_tl");
                    String license=getIntent().getStringExtra("license");


                    map.put("tax_office",ed_tax_office.getText().toString());
                    map.put("company",ed_companyname.getText().toString());
                    map.put("account_type",accountTypeId);
                    map.put("identity",ed_nationid_number.getText().toString());
                    map.put("bank",tv_select_bank.getText().toString());
                    map.put("iban",ed_iban.getText().toString());
                    map.put("title",ed_indivisual);
                    map.put("experience",ed_taskmoreabout);
                    map.put("specialties",ed_specialist);
                    map.put("category_id",category_id);
                    map.put("sub_category_id",sub_category_id);
                    map.put("rate",txt_select_price_tl);
                    map.put("city",ed_city.getText().toString());
                    map.put("provience",ed_provience.getText().toString());
                    map.put("postal_code",ed_postal_Address.getText().toString());
                    map.put("user_id",getRestParamsName("user_id"));
//                    final Map<String, String> obj = new HashMap<>();
//                    obj.put("token", getRestParamsName(Utilclass.token));

                    File file = new File(license);
                    if (file != null) {

                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), "license");
                        MultipartBody.Part body = MultipartBody.Part.createFormData("license", file.getName(), requestBody);

                        System.out.println("multipart ==="+body);
                        RequestBody tax_office = RequestBody.create(MediaType.parse("text/plain"), ed_tax_office.getText().toString());
                        RequestBody company = RequestBody.create(MediaType.parse("text/plain"), ed_companyname.getText().toString());
                        RequestBody account_type = RequestBody.create(MediaType.parse("text/plain"), accountTypeId);
                        RequestBody identity = RequestBody.create(MediaType.parse("text/plain"), ed_nationid_number.getText().toString());
                        RequestBody bank = RequestBody.create(MediaType.parse("text/plain"), tv_select_bank.getText().toString());
                        RequestBody iban = RequestBody.create(MediaType.parse("text/plain"), ed_iban.getText().toString());
                        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), ed_indivisual);
                        RequestBody experience = RequestBody.create(MediaType.parse("text/plain"), ed_taskmoreabout);
                        RequestBody specialties = RequestBody.create(MediaType.parse("text/plain"), ed_specialist);
                        RequestBody category_id_new = RequestBody.create(MediaType.parse("text/plain"), category_id);
                        RequestBody sub_category_id_new = RequestBody.create(MediaType.parse("text/plain"), sub_category_id);
                        RequestBody rate = RequestBody.create(MediaType.parse("text/plain"), txt_select_price_tl);
                        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), ed_city.getText().toString());
                        RequestBody provience = RequestBody.create(MediaType.parse("text/plain"), ed_provience.getText().toString());
                        RequestBody postal_code = RequestBody.create(MediaType.parse("text/plain"), ed_postal_Address.getText().toString());
                        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), getRestParamsName("user_id"));
                        RequestBody licensenew = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                        ImageUpload contestService = ApiProduction.getInstance(AccountInformation.this).provideService(ImageUpload.class);
                        Observable<String> responseObservable = contestService.uploadImage(tax_office, company, account_type,
                                identity, bank, iban, title, experience, specialties, category_id_new, sub_category_id_new, rate,
                                city, provience, postal_code, user_id, getRestParamsName(Utilclass.token)
                                , body);



                        RxAPICallHelper.call(responseObservable, new RxAPICallback<String>() {
                            @Override
                            public void onSuccess(String t) {
                                System.out.println("Inside failed=====>" + t);
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                System.out.println("Inside failed=====>" + throwable.getMessage());
                            }
                        });

                    }

                  }

                }

        });

    }


    private void selectDialog(int x)
    {
        try {

            hideKeyboard(this);
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
                    if(x==0)
                     {
                        if(accountTypeId.equalsIgnoreCase("1"))
                        {
                            ll_company_name.setVisibility(View.GONE);
                            ll_post_officenumber.setVisibility(View.GONE);
                            tc_number.setHint(getResources().getString(R.string.enter_national_idnumber));
                            ed_nationid_number.setHint(getResources().getString(R.string.enter_national_idnumber));

                        }
                        else if(accountTypeId.equalsIgnoreCase("2"))
                        {
                            ll_company_name.setVisibility(View.VISIBLE);
                            ll_post_officenumber.setVisibility(View.VISIBLE);
                            tc_number.setHint(getResources().getString(R.string.enter_national_idnumber));
                            ed_nationid_number.setHint(getResources().getString(R.string.enter_national_idnumber));
                        }
                        else if(accountTypeId.equalsIgnoreCase("3"))
                        {
                            tc_number.setHint(getResources().getString(R.string.tc_number));
                            ed_tax_office.setHint(getResources().getString(R.string.tax_office));
                            ed_nationid_number.setHint(getResources().getString(R.string.tc_number));
                            ll_company_name.setVisibility(View.VISIBLE);
                            ll_post_officenumber.setVisibility(View.VISIBLE);


                        }
                    }
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