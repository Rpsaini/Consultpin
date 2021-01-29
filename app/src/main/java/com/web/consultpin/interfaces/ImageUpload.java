package com.web.consultpin.interfaces;

import com.web.consultpin.Utilclass;
import com.web.consultpin.consultant.AccountInformation;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageUpload {

    @Multipart
    @POST("addcounsultant")
    Observable<String> uploadImage(
                                           @Part("tax_office") RequestBody tax_office,
                                           @Part("company") RequestBody company,
                                           @Part("account_type") RequestBody account_type,
                                           @Part("identity") RequestBody identity,
                                           @Part("bank") RequestBody bank,
                                           @Part("iban") RequestBody iban,
                                           @Part("title") RequestBody title,
                                           @Part("experience") RequestBody experience,
                                           @Part("specialties") RequestBody specialties,
                                           @Part("category_id") RequestBody category_id,
                                           @Part("sub_category_id") RequestBody sub_category_id,
                                           @Part("rate") RequestBody rate,
                                           @Part("city") RequestBody city,
                                           @Part("provience") RequestBody provience,
                                           @Part("postal_code") RequestBody postal_code,
                                           @Part("user_id") RequestBody user_id,
                                           @Header("Authorization") String authHeader,

                                           @Part MultipartBody.Part image

                                          );





//                    obj.put("Authorization", getRestParamsName(Utilclass.token));
//
//                    System.out.println("Account===="+map);
//
//                    serverHandler.sendToServer(AccountInformation .this, getApiUrl() + "addcounsultant"

}
