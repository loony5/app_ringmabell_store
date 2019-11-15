package com.example.carpe.ringmabell_store.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://ec2-52-78-18-104.ap-northeast-2.compute.amazonaws.com/ringmabell_store/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit; // 레트로핏 객체를 저장할 레트로핏 타입의 변수를 선언.


    private RetrofitClient() {

        // 정리
        // Retrofit.Builder() : 레트로핏에서 제공하는 Builder 객체를 이용해서 몇가지 설정을 해준다.
        // baseUrl 메소드를 이용해서 어떤 서버로 네트워크 통신을 요청할 것인지 설정을 해준다.
        // 통신이 완료된 후, 어떤 Converter 를 이용해서 데이터를 파싱할 것인지에 대한 설정을 해준다. (나는 GsonConverter 를 이용한다고 해줌.)
        // 이 외에 추가적인 설정을 더 지정할 수 있지만, 우선 이것만으로도 통신이 가능.
        // 마지막으로 build() 메소드를 통해 Retrofit.Builder 객체에 설정한 정보를 이용하여 Retrofit (현재 객체 : retrofit) 의 객체에 반환해준다.

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    // 인스턴스화
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    // 레트로핏 생성
    public API getAPI() {
        return retrofit.create(API.class);
    }

}
