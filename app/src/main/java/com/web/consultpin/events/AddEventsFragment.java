package com.web.consultpin.events;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.EventCategoryAdapter;
import com.web.consultpin.interfaces.AddEventInterface;
import com.web.consultpin.interfaces.ApiProduction;
import com.web.consultpin.interfaces.RxAPICallHelper;
import com.web.consultpin.interfaces.RxAPICallback;

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

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class AddEventsFragment extends Fragment {

    private View view;
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
    private JSONArray categoryArray;
    public String event_cat_id = "";
    private EventRequestActivity eventRequestActivity;

    public AddEventsFragment() {
        // Required empty public constructor
    }


    public static AddEventsFragment newInstance(String param1, String param2) {
        AddEventsFragment fragment = new AddEventsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_events, container, false);
        eventRequestActivity = (EventRequestActivity) getActivity();
        init();
        getTodayTiming();
        getEventCatgory();
        return view;
    }

    private void init() {

        ed_about_event = view.findViewById(R.id.ed_about_event);
        ed_datefrom = view.findViewById(R.id.ed_datefrom);
        ed_date_end = view.findViewById(R.id.ed_date_end);
        txt_event_category = view.findViewById(R.id.txt_event_category);
        img_ispaid = view.findViewById(R.id.img_ispaid);
        ed_paid_fee = view.findViewById(R.id.ed_paid_fee);
        ed_numberofparticipaints = view.findViewById(R.id.ed_numberofparticipaints);
        selected_image = view.findViewById(R.id.selected_image);
        ic_upload_eventphoto = view.findViewById(R.id.ic_upload_eventphoto);
        tv_save = view.findViewById(R.id.tv_save);



        ed_datefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate((TextView) v);
            }
        });

        ed_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHourPicker((TextView) v);
            }
        });

        txt_event_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCategoryDialog(categoryArray);

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

                eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, getResources().getString(R.string.choose), "Choose Image either from camera or from gallary?", "Camera", "Gallary", new DialogCallBacks() {
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


                if (eventRequestActivity.validationRule.checkEmptyString(ed_about_event) == 0) {
                    eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, getResources().getString(R.string.Response), getResources().getString(R.string.enter_event_detail), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }
                if (ed_date_end.getText().toString().length() == 0) {
                    eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, getResources().getString(R.string.Required), getResources().getString(R.string.selecteventtime), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }
                if (eventRequestActivity.validationRule.checkEmptyString(ed_numberofparticipaints) == 0) {
                    eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, getResources().getString(R.string.Response), getResources().getString(R.string.numberofdays), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }


                if (eventRequestActivity.validationRule.checkEmptyString(ed_paid_fee) == 0) {
                    eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, getResources().getString(R.string.Response), getResources().getString(R.string.enter_event_fee), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }

                saveEvent();
            }
        });

    }

    private void getTodayTiming() {
        try {
            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = formatter.format(todayDate);
            ed_datefrom.setText(todayString);


        } catch (Exception e) {
            System.out.println("Message====" + e.getMessage());
        }

    }

    private void startDate(final TextView textView) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd = new DatePickerDialog(eventRequestActivity,
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
            m.put("device_token", eventRequestActivity.getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", eventRequestActivity.getRestParamsName(Utilclass.token));

            eventRequestActivity.serverHandler.sendToServer(eventRequestActivity, eventRequestActivity.getApiUrl() + "get-event-categories", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Event data===" + dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            try {

                                categoryArray = jsonObject.getJSONArray("data");


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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
    RelativeLayout downView;

    private void showCategoryDialog(JSONArray dataAr) {
        final SimpleDialog simpleDialog = new SimpleDialog();
        designDialog = simpleDialog.simpleDailog(eventRequestActivity, R.layout.category_layout, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
        downView = designDialog.findViewById(R.id.ll_relativelayout);

        designDialog.findViewById(R.id.ll_relativelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRequestActivity.downSourceDestinationView(downView, designDialog);
            }
        });

        designDialog.findViewById(R.id.img_hideview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRequestActivity.downSourceDestinationView(downView, designDialog);
            }
        });
        eventRequestActivity.animateUp(downView);


        RecyclerView recyclerViewForTransferReason = designDialog.findViewById(R.id.recycler_view_for_reason);
        EventCategoryAdapter categoryAdapter = new EventCategoryAdapter(dataAr, eventRequestActivity,this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(eventRequestActivity, LinearLayoutManager.VERTICAL, false);
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
        if (resultCode != eventRequestActivity.RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
//                 if(imageReturnedIntent != null)
                {
                    try {

                        bmap = MediaStore.Images.Media.getBitmap(eventRequestActivity.getContentResolver(), photoURI);

                        Uri uri = getImageUri(eventRequestActivity, bmap);
                        selectedPath = getRealPathFromURI(uri);
                        selected_image.setText(selectedPath);
                        System.out.println("Selected camera image====" + selectedPath);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case 1:
                    if (resultCode == eventRequestActivity.RESULT_OK) {
                        if (imageReturnedIntent != null) {
                            try {
                                selectedImage = imageReturnedIntent.getData();
                                selectedPath = getRealPathFromURI(selectedImage);

                                InputStream image_stream = eventRequestActivity.getContentResolver().openInputStream(selectedImage);
                                bmap = BitmapFactory.decodeStream(image_stream);
                                selected_image.setText(selectedPath);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }

        }
    }

    static final int REQUEST_TAKE_PHOTO = 0;
    Uri photoURI;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(eventRequestActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("inside exception===" + ex.getMessage());

            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(eventRequestActivity,
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
        File storageDir = eventRequestActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        Cursor cursor = eventRequestActivity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private int checkAndRequestPermissions() {

        int permissionCAMERA = ContextCompat.checkSelfPermission(eventRequestActivity, android.Manifest.permission.CAMERA);
        int readExternal = ContextCompat.checkSelfPermission(eventRequestActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternal = ContextCompat.checkSelfPermission(eventRequestActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
            ActivityCompat.requestPermissions(eventRequestActivity,
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

    public void downView() {

        eventRequestActivity.downSourceDestinationView(downView, designDialog);
    }

    private void saveEvent() {
        File file = new File(selectedPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("banner", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());


        RequestBody consultant_id = RequestBody.create(MediaType.parse("text/plain"), "3");
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ed_about_event.getText().toString());
        RequestBody event_date = RequestBody.create(MediaType.parse("text/plain"), ed_datefrom.getText().toString());
        RequestBody event_time = RequestBody.create(MediaType.parse("text/plain"), ed_date_end.getText().toString());
        RequestBody is_paid = RequestBody.create(MediaType.parse("text/plain"), isPaid + "");
        RequestBody number_of_participants = RequestBody.create(MediaType.parse("text/plain"), ed_numberofparticipaints.getText().toString());
        RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), event_cat_id + "");
        RequestBody event_fee = RequestBody.create(MediaType.parse("text/plain"), ed_paid_fee.getText().toString());


        AddEventInterface contestService = ApiProduction.getInstance(eventRequestActivity).provideService(AddEventInterface.class);
        Observable<ResponseBody> responseObservable = contestService.saveEvent(consultant_id, description, event_date,
                event_time, is_paid, number_of_participants, category_id, event_fee, eventRequestActivity.getRestParamsName(Utilclass.token)
                , body, filename);

        System.out.println("Response===" + consultant_id + "===" + description + "===" + event_date + "==" + event_time + "===" + is_paid + "===" + number_of_participants + "===" + category_id + "===" + event_fee);

        RxAPICallHelper.call(responseObservable, new RxAPICallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody t) {
                System.out.println("Inside Success=====>" + t.toString());

                eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, getResources().getString(R.string.Response), "Event request saved successfully.", "ok", "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                        if (buttonPressed.equalsIgnoreCase("ok")) {
                          //  viewPager.setCurrentItem(1);
                            eventRequestActivity.viewPager.setCurrentItem(1);

                        }
                    }
                });

                try {
                    JSONObject jsonObject=new JSONObject(t.toString());
                    System.out.println("Jsondata===="+jsonObject);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailed(Throwable throwable) {
                System.out.println("Inside failed=====>" + throwable.getMessage());
                eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, getResources().getString(R.string.Response), "Event request not saved.", "ok", "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                        if (buttonPressed.equalsIgnoreCase("ok")) {


                        }
                    }
                });
            }
        });

    }


    public void showHourPicker(TextView textview) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);

                }
                textview.setText(hourOfDay + ":" + minute + ":00");
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(eventRequestActivity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle(getResources().getString(R.string.chooseimage));
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

}