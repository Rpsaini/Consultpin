package com.web.consultpin.consultant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorAdditionalInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.BuildConfig;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.CategoryAdapter;
import com.web.consultpin.adapter.SelectCategorySubCategoryAdapter;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.registration.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import animationpackage.IsAnimationEndedCallback;

public class BecomeAConsultant extends BaseActivity {

    private EditText ed_indivisual,ed_taskmoreabout,ed_specialist,txt_select_price_tl;
    private  ImageView ic_upload_licence;
    private TextView txt_selected_image,txt_selct_catgory,txt_select_sub_category,selected_image;
    private RelativeLayout rr_select_category,rr_select_sub_category,rr_select_price_tl;
    private String picture_path="",price_tl="";
    public String category_id="",selected_category_str="",selected_sub_category_str="";
    public Map<String,String> subcategory_id_Ar;
    private   RecyclerView select_category_recycle;
    private int selectionType=0;
    private String selectedPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_a_consultant);
        getSupportActionBar().hide();
        subcategory_id_Ar=new LinkedHashMap<>();
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


        ed_indivisual =findViewById(R.id.ed_indivisual);
        ed_taskmoreabout =findViewById(R.id.ed_taskmoreabout);
        ed_specialist =findViewById(R.id.ed_specialist);
        ic_upload_licence =findViewById(R.id.ic_upload_licence);
        txt_selected_image =findViewById(R.id.txt_selected_image);
        rr_select_category =findViewById(R.id.rr_select_category);
        txt_selct_catgory =findViewById(R.id.txt_select_catgory);
        rr_select_sub_category =findViewById(R.id.rr_select_sub_category);
        rr_select_price_tl =findViewById(R.id.rr_select_price_tl);
        txt_select_price_tl =findViewById(R.id.txt_select_price_tl);
        selected_image =findViewById(R.id.selected_image);
        txt_select_sub_category =findViewById(R.id.txt_select_sub_category);

        ic_upload_licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogs.alertDialog(BecomeAConsultant.this, getResources().getString(R.string.choose), getResources().getString(R.string.img_sel_msg), getResources().getString(R.string.camera), getResources().getString(R.string.camera), new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                        if (buttonPressed.equalsIgnoreCase("Camera")) {
                            selectImage(0);
                        } else {
                            selectImage(1);
                        }
                    }
                });
            }
        });

        rr_select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionType=0;
                selectCategory(0);
            }
        });

        rr_select_sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionType=1;
                selectCategory(1);
            }
        });

        findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String sub_category_id="";

                for(Map.Entry<String,String> map:subcategory_id_Ar.entrySet())
                {
                    sub_category_id=sub_category_id+","+map.getKey();
                }

                sub_category_id=sub_category_id.replaceFirst(",","");

                if (validationRule.checkEmptyString(ed_indivisual) == 0) {
                    alertDialogs.alertDialog(BecomeAConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.waring_describe), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(ed_taskmoreabout) == 0) {
                    alertDialogs.alertDialog(BecomeAConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.ed_taskmoreabout), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(ed_specialist) == 0) {
                    alertDialogs.alertDialog(BecomeAConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.ed_specialist), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (selected_category_str.length() == 0)
                {
                    alertDialogs.alertDialog(BecomeAConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_category), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }


                if (subcategory_id_Ar.size() == 0) {
                    alertDialogs.alertDialog(BecomeAConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_subcategory), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(txt_select_price_tl) == 0) {
                    alertDialogs.alertDialog(BecomeAConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_price_tl), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }



                Intent intent=new Intent(BecomeAConsultant.this, AccountInformation.class);
                intent.putExtra("ed_indivisual",ed_indivisual.getText().toString());
                intent.putExtra("ed_taskmoreabout",ed_taskmoreabout.getText().toString());
                intent.putExtra("ed_specialist",ed_specialist.getText().toString());
                intent.putExtra("category_id",category_id);
                intent.putExtra("sub_category_id",sub_category_id);
                intent.putExtra("txt_select_price_tl",txt_select_price_tl.getText().toString());
                intent.putExtra("license",selectedPath);
                startActivityForResult(intent,1111);

            }
        });


      }

      private void selectCategory(int x)
      {
          hideKeyboard(this);
          SimpleDialog simpleDialog = new SimpleDialog();
          final Dialog selectCategoryDialog = simpleDialog.simpleDailog(BecomeAConsultant.this, R.layout.select_category_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
          select_category_recycle=selectCategoryDialog.findViewById(R.id.select_category_recycler);
          ImageView img_hideview=selectCategoryDialog.findViewById(R.id.img_hideview);
          final RelativeLayout ll_relativelayout=selectCategoryDialog.findViewById(R.id.ll_relativelayout);
          final TextView select_title=selectCategoryDialog.findViewById(R.id.select_title);
          final TextView select_sub_title=selectCategoryDialog.findViewById(R.id.select_sub_title);
          final TextView tv_done=selectCategoryDialog.findViewById(R.id.tv_done);
          animateUp(ll_relativelayout);

          if(x==0)
          {
              select_title.setText(getResources().getString(R.string.select_category));
              select_sub_title.setText(getResources().getString(R.string.select_any_one));
          }
          else
          {
              select_title.setText(getResources().getString(R.string.select_subcategory));
              select_sub_title.setText(getResources().getString(R.string.select_any_two));
          }

          img_hideview.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  downSourceDestinationView(ll_relativelayout,selectCategoryDialog);
              }
          });

          tv_done.findViewById(R.id.tv_done).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  System.out.println("selected cat===="+selected_category_str+"=="+selected_sub_category_str);

                  downSourceDestinationView(ll_relativelayout,selectCategoryDialog);
                  if(selected_category_str!=null)
                  {
                      txt_selct_catgory.setText(selected_category_str+"");
                  }
                  if(selected_sub_category_str!=null)
                  {
                      selected_category_str=selected_category_str.replaceFirst(",","");
                      txt_select_sub_category.setText(selected_sub_category_str + "");
                  }

              }
          });
          getCateogory(x+"");
      }



      private void getCateogory(String catId)
      {
          try {
              final Map<String, String> m = new HashMap<>();
              m.put("category_id", catId);
              m.put("device_type", "android");
              m.put("device_token", getDeviceToken()+"");

              final Map<String, String> obj = new HashMap<>();
              obj.put("token", getRestParamsName(Utilclass.token));


              serverHandler.sendToServer(this, getApiUrl() + "categories", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                  @Override
                  public void getRespone(String dta, ArrayList<Object> respons) {
                      try {
                          JSONObject jsonObject = new JSONObject(dta);
                          if (jsonObject.getBoolean("status")) {

                              try {
                                String  icon_base_url=jsonObject.getString("icon_base_url");
                                JSONArray categories=jsonObject.getJSONArray("categories");
                                initHomeCategory(icon_base_url,categories);
                             }
                              catch (Exception e)
                              {
                                  e.printStackTrace();
                              }

                          } else {
                              alertDialogs.alertDialog(BecomeAConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                  @Override
                                  public void getDialogEvent(String buttonPressed) {
                                  }
                              });
                          }
                      } catch (Exception e) {
                          e.printStackTrace();
                      }

                  }
              });


          } catch (Exception e) {
              e.printStackTrace();
          }

      }
    private void initHomeCategory(String imageUrl,JSONArray dataAr)
    {
        select_category_recycle.setNestedScrollingEnabled(false);
        select_category_recycle.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        select_category_recycle.setHasFixedSize(true);
        select_category_recycle.setItemAnimator(new DefaultItemAnimator());
        SelectCategorySubCategoryAdapter horizontalCategoriesAdapter = new SelectCategorySubCategoryAdapter(dataAr,this,imageUrl,selectionType);
        select_category_recycle.setAdapter(horizontalCategoriesAdapter);


    }



//
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void selectImage(int actionCode) {
//        if (checkAndRequestPermissions() == 0) {
//            if (actionCode == 0) {
//                dispatchTakePictureIntent();
//            } else if (actionCode == 1) {
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(pickPhoto, actionCode);
//            }
//        } else if (checkAndRequestPermissions() == 1) {
//            //  showtoast.showToast(PersonalDetails.this, "Go to  Cabs App settings and enable required Permission");
//        }
//    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
//
//Bitmap bmap;
//    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        if(requestCode == 1001)
//         {
//            init();
//         }
//        if(requestCode==1111)
//        {
//            finish();
//        }
//         else {
//            bmap = null;
//            Uri selectedImage = null;
//
//           // if (resultCode != RESULT_CANCELED)
//            {
//                System.out.println("inside resualt code==="+requestCode+"=="+resultCode);
//                switch (requestCode)
//                {
//                    case 0:
//                        try
//                        {
////                            System.out.println("selfie==="+user_profile_img);
//                            bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
//
////                            user_profile_img.setImageBitmap(bmap);
//                            System.out.println("selefie===>"+bmap);
//                            selectedPath = getRealPathFromURI(getImageUri(this, bmap));
//                            selected_image.setText(selectedPath);
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    case 1:
//
//                            if (imageReturnedIntent != null) {
//                                try {
//                                    selectedImage = imageReturnedIntent.getData();
//                                    selectedPath = getRealPathFromURI(selectedImage);
//                                    InputStream image_stream = getContentResolver().openInputStream(selectedImage);
//                                    bmap = BitmapFactory.decodeStream(image_stream);
//                                    System.out.println("selefie===>"+bmap);
//                                    selected_image.setText(selectedPath);
////                                  user_profile_img.setImageBitmap(bmap);
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        break;
//                }
//
//            }
//        }
//    }

//    private int checkAndRequestPermissions() {
//
//        int permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
//        int readExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        int writeExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
//        }
//        if (readExternal != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//        if (writeExternal != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this,
//                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
//            return 1;
//        }
//
//        return 0;
//    }


//    public String getRealPathFromURI(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        @SuppressWarnings("deprecation")
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }


//    String mCurrentPhotoPath;
//
//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    static final int REQUEST_TAKE_PHOTO = 0;
//    Uri photoURI;


//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                System.out.println("inside exception===" + ex.getMessage());
//
//            }
//            if (photoFile != null) {
//
//                photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
//
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
//                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
//                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }


//    class UploadVideo extends AsyncTask<Void, Void, String> {
//        ProgressDialog uploading;
//        String path1 = "", imageType1 = "";
//        private Map<String, String> mapData;
//
//
//        public UploadVideo(String path, String imageType, Map<String, String> mapData) {
//            path1 = path;
//            imageType1 = imageType;
//            this.mapData = mapData;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            uploading = ProgressDialog.show(BecomeAConsultant.this, "Uploading....", "Please wait...", false, false);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            uploading.dismiss();
//            try {
//
//                System.out.println("inside response===" + s);
//                if (s.equalsIgnoreCase("500")) {
//                    Toast.makeText(BecomeAConsultant.this, "This file is not supported.Please select another image", Toast.LENGTH_LONG).show();
//
//                } else {
//                    final JSONObject obj = new JSONObject(s);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                String status = obj.get("status") + "";
//                                if (status.equalsIgnoreCase("true")) {
//
//
//                                    savePreferences.savePreferencesData(BecomeAConsultant.this, obj.getJSONObject("data") + "", "data");
//                                    //{"status":true,"msg":"Your profile is updated successfully!","code":200,"data":{"first_name":"aman","email":"amitk@gmail.com","profile_image":"http:\/\/webcomclients.in\/quickmenu-api\/assets\/images\/users\/profile_1600410452_IMG-20200916-WA0003_233.jpgprofile_image_","gender":"Male","dob":"2020-09-18"}}
//                                    alertDialogs.alertDialog(BecomeAConsultant.this, "Response", obj.getString("msg"), "Ok", "", new DialogCallBacks() {
//                                        @Override
//                                        public void getDialogEvent(String buttonPressed) {
//                                            init();
//                                        }
//                                    });
//
//                                } else {
//                                    alertDialogs.alertDialog(BecomeAConsultant.this, "Response", obj.getString("error"), "Ok", "", new DialogCallBacks() {
//                                        @Override
//                                        public void getDialogEvent(String buttonPressed) {
//                                        }
//                                    });
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
////            Upload u = new Upload();
////            String msg = u.uploadVideo(path1, "", ProfileScreen.this, getApiUrl() + "update-profile", "profile_image", mapData);
//            return "";
//        }


    //}





    //============



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void selectImage(int actionCode) {
        if (checkAndRequestPermissions() == 0) {
            if (actionCode == 0) {
                dispatchTakePictureIntent();
            } else if (actionCode == 1) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, actionCode);
            }
        }
    }

    private Bitmap bmap;
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        System.out.println("inside============" + requestCode + "===" + resultCode);
        bmap = null;
        Uri selectedImage = null;
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
//                 if(imageReturnedIntent != null)
                {
                    try {

                        bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        Uri uri = getImageUri(this, bmap);
                        selectedPath = getRealPathFromURI(uri);
                        selected_image.setText(selectedPath);
                        System.out.println("Selected camera image====" + selectedPath);

                      } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        if (imageReturnedIntent != null) {
                            try {
                                selectedImage = imageReturnedIntent.getData();
                                 selectedPath  = getRealPathFromURI(selectedImage);

                                InputStream image_stream = getContentResolver().openInputStream(selectedImage);
                                bmap = BitmapFactory.decodeStream(image_stream);
                                selected_image.setText(selectedPath);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }

            // new ConvertImage().execute();
        }
    }

    static final int REQUEST_TAKE_PHOTO = 0;
    Uri photoURI;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("inside exception===" + ex.getMessage());

            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.web.consultpin.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private int checkAndRequestPermissions() {

        int permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int readExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (readExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writeExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return 1;
        }

        return 0;
    }


}