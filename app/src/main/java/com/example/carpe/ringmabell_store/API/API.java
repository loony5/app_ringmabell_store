package com.example.carpe.ringmabell_store.API;

import android.util.Log;

import com.example.carpe.ringmabell_store.MODEL.ImageUploadResponse;
import com.example.carpe.ringmabell_store.MODEL.LoginResponse;
import com.example.carpe.ringmabell_store.MODEL.ReadResponse;
import com.example.carpe.ringmabell_store.MODEL.RegisterResponse;
import com.example.carpe.ringmabell_store.MODEL.StoreInfoReadResponse;
import com.example.carpe.ringmabell_store.MODEL.StoreRegisterResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface API {

    // @ 이가 붙은 키워드는 Java 에서 사용되는 Annotation 키워드이다. 자주 사용하는 override 도 마찬가지!
    // Retrofit 에는 REST API 통신에 필요한 Annotation 을 만들어 두었다.
    // 기본적인 GET, POST 를 포함한 것들...
    // @FormUrlEncoded 어노테이션을 메소드로 명시하면 form-encoded 데이터로 전송된다.
    // @POST() 는 HTTP POST 요청을 하기 위한 것으로, RetrofitClient.java 에서 만들어준 BASE_URL 에 이어
    // 실질적으로 요청할 API 의 URL 을 ()에 지정해준다.
    // Call <> 을 이용해서 미리 만들어둔 model 클래스로 데이터를 전달 받을 수 있도록 한다.
    // @Field 는 각 파라미터를 key 와 value 로 엮어주는 역할.
    // @Field ("key") 변수타입 변수명(value) 로 데이터를 전달받는다. Field 의 key 는 PHP 의 변수명으로 값을 받는다.

    @FormUrlEncoded
    @POST("store_register.php")
    Call<RegisterResponse> storeRegister (
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
    );

//    @FormUrlEncoded
//    @POST("store_register.php")
//    Call<ResponseBody> storeRegister(
//            @Field("email") String str_email,
//            @Field("name") String str_name,
//            @Field("password") String str_pass,
//            @Field("phone") String str_phone
//    );

    @FormUrlEncoded
    @POST("store_login.php")
    Call<LoginResponse> storeLogin(
            @Field("id") String id,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("store_read_detail.php")
    Call<ReadResponse> storeReadDetail(
            @Field("no") String no
    );

//    @FormUrlEncoded
//    @POST("store_upload_image.php")
//    Call<ImageUploadResponse> imageUpload(
//            @Field("no") String no,
//            @Field("photo") String photo
//    );

    @Multipart
    @POST("store_upload_image_2.php")
    Call<ImageUploadResponse> imageUpload (
            @Part("no") String no,
            @Part MultipartBody.Part photo
    );


    // multipart 로 변경하기!!!!!!!!!
//    @FormUrlEncoded
//    @POST("store_info_register.php")
//    Call<StoreRegisterResponse> StoreInfoUpload (
//            @Field("storeNo") String storeNo,
//            @Field("storeName") String storeName,
//            @Field("startTime") String startTime,
//            @Field("finishTime") String finishTime,
//            @Field("personalDay") String personalDay,
//            @Field("address") String address,
//            @Field("introduce") String introduce
//    );

    // 문자열을 보낼 때, String 으로 보내면 "" 큰따옴표로 넘어간다.
    // 그래서 php 에서 큰따옴표를 제거해준후 sql 문을 사용해야하는데
    // RequestBody 로 보내면 큰따옴표로 넘어가지 않기때문에 이렇게 써줘야함...
    @Multipart
    @POST("store_info_register.php")
    Call<StoreRegisterResponse> StoreInfoUpload (
            @Part("storeNo") RequestBody storeNo,
            @Part("storeName") RequestBody storeName,
            @Part("startTime") RequestBody startTime,
            @Part("finishTime") RequestBody finishTime,
            @Part("personalDay") RequestBody personalDay,
            @Part("address") RequestBody address,
            @Part("introduce") RequestBody introduce
    );



    @FormUrlEncoded
    @POST("store_email_modify.php")
    Call<RegisterResponse> store_modify (
            @Field("no") String no,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("store_phone_modify.php")
    Call<RegisterResponse> store_modify_phone (
            @Field("no") String no,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("store_info_read.php")
    Call<StoreInfoReadResponse> storeInfoRead (
            @Field("storeNo") String storeNo
    );



}
