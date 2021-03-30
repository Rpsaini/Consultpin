package com.web.consultpin.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.UtilClass;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.EditProfileConsultant;
import com.web.consultpin.interfaces.AddEventInterface;
import com.web.consultpin.interfaces.ApiProduction;
import com.web.consultpin.interfaces.RxAPICallHelper;
import com.web.consultpin.interfaces.RxAPICallback;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.registration.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ChatActivity extends BaseActivity {


    private String selectedPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        initiateObj();
        init();
        getUserMessages(0);
        replyMsg();
        sendNotification();
    }

    private void init() {

        ImageView toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
        toolbar_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView nameTxt = findViewById(R.id.toolbar_title);
        nameTxt.setText(getIntent().getStringExtra(Utilclass.username));

    }

    private void showMessages(ArrayList<JSONObject> dataAr) {
        RecyclerView reply_recycler_view = findViewById(R.id.reply_recycler_view);
        ChatMessagesAdapter transferPurposeAdater = new ChatMessagesAdapter(dataAr, ChatActivity.this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reply_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        reply_recycler_view.setItemAnimator(new DefaultItemAnimator());
        reply_recycler_view.setAdapter(transferPurposeAdater);
        reply_recycler_view.scrollToPosition(dataAr.size() - 1);
    }


    private void getUserMessages(int isload) {
        try {
            final Map<String, String> m = new HashMap<>();
            if (getIntent().getStringExtra(Utilclass.callFrom).equalsIgnoreCase("chatList")) {
                m.put("appointment_id", "0");
            } else {
                m.put("appointment_id", getIntent().getStringExtra(Utilclass.appointment_id));
            }
            m.put("receiver_user_id", getIntent().getStringExtra(Utilclass.receiver_id));
            m.put("sender_user_id", getIntent().getStringExtra(Utilclass.sender_id));
            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token) + "");

            serverHandler.sendToServer(this, getApiUrl() + "get-chat-history", m, isload, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        System.out.println("chat data===="+jsonObject);
                        if(jsonObject.getBoolean("status")) {
                            try {

                                System.out.println("chatdata===" + jsonObject);

                                JSONArray dataAr = jsonObject.getJSONArray("data");
                                ArrayList<JSONObject> messageAr = new ArrayList<>();
                                for (int x = 0; x < dataAr.length(); x++) {
                                    messageAr.add(dataAr.getJSONObject(x));
                                }

                                showMessages(messageAr);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(ChatActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

    private EditText ed_reply;

    private void replyMsg() {
        ed_reply = findViewById(R.id.ed_reply);
        ImageView  img_postmessage = findViewById(R.id.img_postmessage);
        img_postmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_reply.getText().toString().length() == 0) {

                } else {
                    submitTicket(ed_reply.getText().toString());
                }
            }
        });

        findViewById(R.id.img_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
//                alertDialogs.alertDialog(ChatActivity.this, getResources().getString(R.string.choose), "Choose Image either from camera or from gallary?", "Camera", "Gallary", new DialogCallBacks() {
//                    @Override
//                    public void getDialogEvent(String buttonPressed) {
//
//
////
////                        if (buttonPressed.equalsIgnoreCase("Camera")) {
////                            selectImage(0);
////                        } else {
////                            selectImage(1);
////                        }
//                    }
//                });
            }
        });

    }


    private void submitTicket(String msg) {
        try {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("appointment_id", getIntent().getStringExtra(Utilclass.appointment_id));
            m.put("receiver_user_id", getIntent().getStringExtra(Utilclass.receiver_id));
            m.put("sender_user_id", getIntent().getStringExtra(Utilclass.sender_id));
            m.put("message", msg);

            m.put("sender_consultant_id", getIntent().getStringExtra(Utilclass.sender_consultant_id));
            m.put("receiver_consultant_id", getIntent().getStringExtra(Utilclass.receiver_consultant_id));


            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token) + "");


            serverHandler.sendToServer(this, getApiUrl() + "send-chat-message", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                System.out.println("Messgae send===" + jsonObject);
                                getUserMessages(0);
                                ed_reply.setText("");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(ChatActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

    private void sendNotification() {
        BroadcastReceiver getChatBroadCast;
        getChatBroadCast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getUserMessages(1);
                    }
                });

            }
        };
        registerReceiver(getChatBroadCast, new IntentFilter(Utilclass.callChatBroadCast));


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
        bmap = null;
        Uri selectedImage = null;
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:

                    try {
                        bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        Uri uri = getImageUri(ChatActivity.this, bmap);
                        selectedPath = getRealPathFromURI(uri);
//                        img_profile.setImageBitmap(bmap);
                        uploadFile();

                    } catch (Exception e) {
                        e.printStackTrace();
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
//                                img_profile.setImageBitmap(bmap);

                                uploadFile();

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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("inside exception===" + ex.getMessage());

            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(ChatActivity.this,
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


    //save================>


    private void uploadFile()
    {
        Observable<ResponseBody> responseObservable = null;
        AddEventInterface contestService = ApiProduction.getInstance(this).provideService(AddEventInterface.class);

        RequestBody appointment_id = RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra(Utilclass.appointment_id));
        RequestBody receiver_id = RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra(Utilclass.receiver_id));
        RequestBody sender_id = RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra(Utilclass.sender_id));
        RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody sender_consultant_id = RequestBody.create(MediaType.parse("text/plain"),getIntent().getStringExtra(Utilclass.sender_consultant_id));
        RequestBody receiver_consultant_id = RequestBody.create(MediaType.parse("text/plain"),getIntent().getStringExtra(Utilclass.receiver_consultant_id));


        if(selectedPath.length() > 0)
        {
            File file = new File(selectedPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            responseObservable = contestService.chatWithImage(appointment_id, receiver_id, sender_id, msg,
                    sender_consultant_id, receiver_consultant_id, getRestParamsName(Utilclass.token)
                    , body, filename);

        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.show();

        RxAPICallHelper.call(responseObservable, new RxAPICallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody t) {
                progressDialog.dismiss();
                getUserMessages(1);
//                alertDialogs.alertDialog(ChatActivity.this, getResources().getString(R.string.Response), "Uploaded.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                    @Override
//                    public void getDialogEvent(String buttonPressed)
//                    {
//                        if(buttonPressed.equalsIgnoreCase("ok"))
//                        {
//
//                        }
//                    }
//                })
            }

            @Override
            public void onFailed(Throwable throwable)
            {
                progressDialog.dismiss();
                getUserMessages(1);
//                alertDialogs.alertDialog(ChatActivity.this, getResources().getString(R.string.Response), "Profile not updated.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                    @Override
//                    public void getDialogEvent(String buttonPressed) {
//                        if(buttonPressed.equalsIgnoreCase("ok"))
//                        {
//                        }
//                    }
//                });
            }
        });

    }
}

