package com.web.consultpin.interfaces;

import com.web.consultpin.Utilclass;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AddEventInterface {

    @Multipart
    @POST("create-event-request")
    Observable<ResponseBody> saveEvent(
            @Part("consultant_id") RequestBody consultant_id,
            @Part("event_name") RequestBody event_name,
            @Part("description") RequestBody description,
            @Part("start_date") RequestBody event_date,
            @Part("end_date") RequestBody event_time,
            @Part("is_paid") RequestBody is_paid,
            @Part("number_of_participants") RequestBody number_of_participants,
            @Part("category_id") RequestBody category_id,
            @Part("event_fee") RequestBody event_fee,
            @Header("token") String authHeader,
            @Part MultipartBody.Part image,
            @Part("banner") RequestBody name

    );

    @Multipart
    @POST("create-event-request")
    Observable<ResponseBody> saveEventWithotimage(
            @Part("consultant_id") RequestBody consultant_id,
            @Part("event_name") RequestBody event_name,
            @Part("description") RequestBody description,
            @Part("start_date") RequestBody event_date,
            @Part("end_date") RequestBody event_time,
            @Part("is_paid") RequestBody is_paid,
            @Part("number_of_participants") RequestBody number_of_participants,
            @Part("category_id") RequestBody category_id,
            @Part("event_fee") RequestBody event_fee,
            @Header("token") String authHeader


    );


    @Multipart
    @POST("updateprofile")
    Observable<ResponseBody> saveProfileWithImage(
            @Part("consultant_id") RequestBody consultant_id,
            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody description,
            @Part("surname") RequestBody event_date,
            @Part("fee") RequestBody event_time,
            @Part("type") RequestBody type,
            @Header("token") String authHeader,
            @Part MultipartBody.Part image,
            @Part("profile_pic") RequestBody name

    );
    @Multipart
    @POST("updateprofile")
    Observable<ResponseBody> saveProfileWithoutImage(
            @Part("consultant_id") RequestBody consultant_id,
            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody description,
            @Part("surname") RequestBody event_date,
            @Part("fee") RequestBody event_time,
            @Part("type") RequestBody type,
            @Header("token") String authHeader
        );




}
