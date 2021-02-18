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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.EventCategoryAdapter;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRequestActivity extends BaseActivity {
    EditText ed_about_event;
    TextView ed_datefrom;
    TextView ed_date_end;
    TextView txt_event_category;
    ImageView img_ispaid, ic_upload_eventphoto;
    EditText ed_paid_fee;
    EditText ed_numberofparticipaints;
    TextView selected_image;
    TextView tv_save;
    int isPaid = 0;
    private String selectedPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_request);
        initiateObj();
        getSupportActionBar().hide();
        init();
        getTodayTiming();
        getEventCatgory();
    }

    private void init() {

        ed_about_event = findViewById(R.id.ed_about_event);
        ed_datefrom = findViewById(R.id.ed_datefrom);
        ed_date_end = findViewById(R.id.ed_date_end);
        txt_event_category = findViewById(R.id.txt_event_category);
        img_ispaid = findViewById(R.id.img_ispaid);
        ed_paid_fee = findViewById(R.id.ed_paid_fee);
        ed_numberofparticipaints = findViewById(R.id.ed_numberofparticipaints);
        selected_image = findViewById(R.id.selected_image);
        ic_upload_eventphoto = findViewById(R.id.ic_upload_eventphoto);
        tv_save = findViewById(R.id.tv_save);



        ed_datefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate((TextView) v);
            }
        });

        ed_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate((TextView) v);
            }
        });

        txt_event_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img_ispaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaid == 0) {
                    img_ispaid.setImageResource(R.drawable.ic_unselect_button);
                    isPaid = 0;
                } else {
                    img_ispaid.setImageResource(R.drawable.ic_button);
                    isPaid = 1;
                }

            }
        });

        ic_upload_eventphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogs.alertDialog(EventRequestActivity.this, getResources().getString(R.string.choose), "Choose Image either from camera or from gallary?", "Camera", "Gallary", new DialogCallBacks() {
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


        tv_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validationRule.checkEmptyString(ed_about_event) == 0) {
                            alertDialogs.alertDialog(EventRequestActivity.this, getResources().getString(R.string.Response), getResources().getString(R.string.enter_event_detail), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                }
                            });
                            return;
                        }
                        if (validationRule.checkEmptyString(ed_numberofparticipaints) == 0) {
                            alertDialogs.alertDialog(EventRequestActivity.this, getResources().getString(R.string.Response), getResources().getString(R.string.numberofdays), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                }
                            });
                            return;
                        }
                        if (validationRule.checkEmptyString(ed_paid_fee) == 0) {
                            alertDialogs.alertDialog(EventRequestActivity.this, getResources().getString(R.string.Response), getResources().getString(R.string.enter_event_fee), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                }
                            });
                            return;
                        }

                        saveEvents();
                    }
                });

    }

    private void getTodayTiming() {
        try {
            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = formatter.format(todayDate);
            ed_datefrom.setText(todayString);
            ed_date_end.setText(todayString);

        } catch (Exception e) {
            System.out.println("Message====" + e.getMessage());
        }

    }

    private void startDate(final TextView textView) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        textView.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();

    }


    private void getEventCatgory() {
        try {

            final Map<String, String> m = new HashMap<>();
            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));

            serverHandler.sendToServer(this, getApiUrl() + "get-event-categories", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Event data===" + dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            try {


                                showCategoryDialog(jsonObject.getJSONArray("data"));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(EventRequestActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

    Dialog designDialog;

    private void showCategoryDialog(JSONArray dataAr) {
        final SimpleDialog simpleDialog = new SimpleDialog();
        designDialog = simpleDialog.simpleDailog(this, R.layout.category_layout, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
        RelativeLayout downView = designDialog.findViewById(R.id.ll_relativelayout);
        TextView transfer_title = designDialog.findViewById(R.id.transfer_title);
        designDialog.findViewById(R.id.ll_relativelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSourceDestinationView(downView, designDialog);
            }
        });

        designDialog.findViewById(R.id.img_hideview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSourceDestinationView(downView, designDialog);
            }
        });
        animateUp(downView);


        RecyclerView recyclerViewForTransferReason = designDialog.findViewById(R.id.recycler_view_for_reason);
        EventCategoryAdapter categoryAdapter = new EventCategoryAdapter(dataAr, this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewForTransferReason.setLayoutManager(horizontalLayoutManagaer);
        recyclerViewForTransferReason.setItemAnimator(new DefaultItemAnimator());
        recyclerViewForTransferReason.setAdapter(categoryAdapter);

    }


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
                                selectedPath = getRealPathFromURI(selectedImage);

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
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return 1;
        }

        return 0;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    //====================end of image






    private void saveEvents() {
        try {
            final Map<String, String> m = new HashMap<>();

            m.put("consultant_id", "3");
            m.put("description", ed_about_event.getText().toString());
            m.put("event_date", ed_datefrom.getText().toString());
            m.put("event_time", "8:0:00");
            m.put("is_paid", isPaid+"");
            m.put("number_of_participants", ed_numberofparticipaints.getText().toString());
            m.put("category_id", "1");
            m.put("banner", selectedPath);
            m.put("event_fee", ed_paid_fee.getText().toString());

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token) + "");

            System.out.println("save Event===" + m);

            serverHandler.sendToServer(this, getApiUrl() + "create-event-request", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {

                        JSONObject jsonObject = new JSONObject(dta);
                        System.out.println("profile data==>>=" + jsonObject);
                        if (jsonObject.getBoolean("status")) {
                            try {



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(EventRequestActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


}