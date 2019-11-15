package com.example.carpe.ringmabell_store;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.carpe.ringmabell_store.API.RetrofitClient;
import com.example.carpe.ringmabell_store.MODEL.ReadResponse;
import com.example.carpe.ringmabell_store.MODEL.StoreUser;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = MenuActivity.class.getSimpleName();

    private TextView txt_name, txt_phone, txt_store_register;
    CircleImageView img_profile;
    private ImageView img_close;

    SessionManager sessionManager;

    String getNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phone);
        img_profile = findViewById(R.id.img_profile);

        img_close = findViewById(R.id.img_close);

        // SessionManager.java 에서 checkLogin() 메소드로 로그인 여부를 확인해서
        // 로그인 상태가 아니면 MenuActivity.java 를 종료 후, 회원가입 & 로그인 화면(IntoActivity.java)로 이동한다.
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        // 로그인 상태이면, SessionManager.java 의 getUserDetail() 메소드에 HashMap 으로 저장한
        // id 값을 가져와서 getId 변수에 담는다.
        HashMap<String, String> user = sessionManager.getUserDetail();
        getNO = user.get(sessionManager.NO);
        Log.e("MenuActivity", "getId : " +getNO);

        txt_store_register = findViewById(R.id.txt_store_register);


        // 프로필 사진이 보여지는 이미지 뷰 img_profile 을 선택하면 chooseFile() 메소드 실행.
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, EditActivity.class);
                startActivity(intent);

            }
        });

//        getUserDetail();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, MainActivity.class);

                // 새로운 task 를 만들어서 root activity 가 된다.
                // 화면을 이동하면서 이전 화면 기록 삭제
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        txt_store_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 업체 정보 확인 및 등록 화면으로 이동
                Intent intent = new Intent(MenuActivity.this, StoreListActivity.class);
                startActivity(intent);

            }
        });

    }

    // 로그인 상태일 때, 회원 정보를 불러오는 메소드
    private void getUserDetail() {

        Call<ReadResponse> call = RetrofitClient
                .getInstance()
                .getAPI()
                .storeReadDetail(getNO);

        call.enqueue(new Callback<ReadResponse>() {
            @Override
            public void onResponse(Call<ReadResponse> call, Response<ReadResponse> response) {

                ReadResponse readResponse = response.body();
                List<StoreUser> read = readResponse.getRead();

                String success = readResponse.getSuccess();

                if (success.equals("1")) {

                    Log.e("MenuActivity", "success : " + success);
                    for(int i = 0; i < read.size(); i++) {

                        String strName = read.get(i).getName();
                        String strEmail = read.get(i).getEmail();
                        String strPhone = read.get(i).getPhone();
                        String strPhoto = read.get(i).getPhoto();

                        txt_name.setText(strName);
                        txt_phone.setText(strPhone);
                        Log.e("MenuActivity", "strPhoto : " + strPhoto);

                        if(strPhoto == null) {
                            Log.e("MenuActivity", "현재 이미지 없음");
                        } else {

                            Uri filePath = Uri.parse(strPhoto);
                            Log.e("MenuActivity.java", "로그인 된 회원의 현재 이미지는 " + filePath);


                            Glide.with(MenuActivity.this).load(filePath)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)).into(img_profile);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ReadResponse> call, Throwable t) {

                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // onResume 상태일 때, geUserDetail() 메소드를 실행한다.
    @Override
    protected  void onResume() {
        super.onResume();
        getUserDetail();
    }

}
