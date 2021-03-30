package com.web.consultpin.consultant;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.cloudmessaging.CloudMessagingReceiver;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.interfaces.AddEventInterface;
import com.web.consultpin.interfaces.ApiProduction;
import com.web.consultpin.interfaces.RxAPICallHelper;
import com.web.consultpin.interfaces.RxAPICallback;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.registration.LoginActivity;
import com.web.consultpin.registration.ResetPassword;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EditProfileConsultant extends BaseActivity {

    private ImageView img_profile;
    private EditText ed_firstname, ed_lastname, ed_fee;
    TextView tv_update,txt_resetpassword;
    private String selectedPath = "";
    private String id = "", usertype = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initiateObj();
        removeActionBar();
        changestatusBarColorBlue();
        init();

    }


    private void init() {
        ImageView profile_back = findViewById(R.id.profile_back);
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_profile = findViewById(R.id.img_profile);
        ed_firstname = findViewById(R.id.ed_firstname);
        ed_lastname = findViewById(R.id.ed_lastname);
        ed_fee = findViewById(R.id.ed_fee);
        tv_update = findViewById(R.id.tv_update);
        txt_resetpassword = findViewById(R.id.txt_resetpassword);

        ed_firstname.setText(getRestParamsName("first_name"));
        ed_lastname.setText(getRestParamsName("last_name"));

        showImage(getRestParamsName("profile_pic"), img_profile);
        TextView txt_fee_lable = findViewById(R.id.txt_fee_lable);


        if (Utilclass.isConsultantModeOn) {
            txt_fee_lable.setVisibility(View.VISIBLE);
            ed_fee.setVisibility(View.VISIBLE);
            id = getRestParamsName(Utilclass.consultant_id);
            usertype = "1";
            try {
                JSONObject consltantData = new JSONObject(savePreferences.reterivePreference(this, Utilclass.consultant_data).toString());
                ed_fee.setText(consltantData.getString("rate"));
               }
               catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            txt_fee_lable.setVisibility(View.GONE);
            ed_fee.setVisibility(View.GONE);
            id = getRestParamsName(Utilclass.user_id);
            usertype = "0";
        }




        txt_resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditProfileConsultant.this, ResetPassword.class);
                startActivity(intent);
            }
        });

        update();
    }


    private void update() {
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.choose), "Choose Image either from camera or from gallary?", "Camera", "Gallary", new DialogCallBacks() {
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


        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationRule.checkEmptyString(ed_firstname) == 0) {
                    alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_firstname), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(ed_lastname) == 0) {
                    alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_lastname), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }



                if (Utilclass.isConsultantModeOn) {
                    if (validationRule.checkEmptyString(ed_fee) == 0) {
                        alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.enter_fee), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {

                            }
                        });
                        return;
                    }
                }

                updateProfile();

            }
        });
    }

//choose proile


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
//                 if(imageReturnedIntent != null)
                {
                    try {
                        bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        Uri uri = getImageUri(EditProfileConsultant.this, bmap);
                        selectedPath = getRealPathFromURI(uri);
                        img_profile.setImageBitmap(bmap);

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
                                img_profile.setImageBitmap(bmap);


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
                photoURI = FileProvider.getUriForFile(EditProfileConsultant.this,
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


    private void updateProfile() {
        Observable<ResponseBody> responseObservable = null;
        AddEventInterface contestService = ApiProduction.getInstance(this).provideService(AddEventInterface.class);
        RequestBody consultant_id = RequestBody.create(MediaType.parse("text/plain"), getRestParamsName(Utilclass.consultant_id));
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), getRestParamsName(Utilclass.user_id));
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), ed_firstname.getText().toString());
        RequestBody surname = RequestBody.create(MediaType.parse("text/plain"), ed_lastname.getText().toString());
        RequestBody fee = RequestBody.create(MediaType.parse("text/plain"), ed_fee.getText().toString());
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), usertype);
        // RequestBody token = RequestBody.create(MediaType.parse("text/plain"),getRestParamsName(Utilclass.token));
        if (selectedPath.length() > 0) {
            File file = new File(selectedPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            responseObservable = contestService.saveProfileWithImage(consultant_id, user_id, name, surname,
                    fee, type, getRestParamsName(Utilclass.token)
                    , body, filename);
            System.out.println("Params===" + consultant_id + "==" + name + "==" + surname + "===" + fee + "===" + type + "===" + getRestParamsName(Utilclass.token));

        } else {
            responseObservable = contestService.saveProfileWithoutImage(consultant_id, user_id, name, surname,
                    fee, type, getRestParamsName(Utilclass.token));

        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.show();


        RxAPICallHelper.call(responseObservable, new RxAPICallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody t) {
                progressDialog.dismiss();
                alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Response), "Profile saved successfully.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                        if (buttonPressed.equalsIgnoreCase("ok")) {

                        }
                    }
                });
            }

            @Override
            public void onFailed(Throwable throwable) {
                progressDialog.dismiss();
                alertDialogs.alertDialog(EditProfileConsultant.this, getResources().getString(R.string.Response), "Profile not updated.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                        if (buttonPressed.equalsIgnoreCase("ok")) {
                        }
                    }
                });
            }
        });

    }

    private void showImage(final String url, final ImageView header_img) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(EditProfileConsultant.this)
                        .load(url)
                        .placeholder(R.drawable.profileavtar)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                        .into(header_img);
            }
        });
    }





}