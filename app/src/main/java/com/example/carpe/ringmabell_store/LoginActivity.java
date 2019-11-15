package com.example.carpe.ringmabell_store;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carpe.ringmabell_store.API.API;
import com.example.carpe.ringmabell_store.API.RetrofitClient;
import com.example.carpe.ringmabell_store.MODEL.LoginResponse;
import com.example.carpe.ringmabell_store.MODEL.StoreUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_ID, edit_password;
    private Button btn_login_success;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        edit_ID = findViewById(R.id.edit_ID);
        edit_password = findViewById(R.id.edit_password);
        btn_login_success = findViewById(R.id.btn_login_success);

        btn_login_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeLogin();
            }
        });

    }

    private void storeLogin() {

        String str_email = edit_ID.getText().toString().trim();
        String str_pass = edit_password.getText().toString().trim();

        // 모두 입력했을 때,
        if(!str_email.isEmpty() && !str_pass.isEmpty()) {

            // 미리 정의해둔 class, Retrofit build, 인스턴스, create, storeLogin 으로 데이터 보냄
            Call<LoginResponse> call = RetrofitClient
                    .getInstance()
                    .getAPI()
                    .storeLogin(str_email, str_pass);

            // 서버의 응답값 받기 response
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    // PHP 의 응답 값(response.body())를 loginResponse 로 담는다.
                    LoginResponse loginResponse = response.body();

                    // loginResponse 의 isSuccess() 메소드로 boolean 값 가져오기
                    boolean success = loginResponse.isSuccess();

                    // loginResponse 의 getMessage() 메소드로 message 값 가져오기
                    String message = loginResponse.getMessage();

                    // PHP 에서 array 로 담은 login (key 값) 을 List 로 가져오기
                    List<StoreUser> detail = loginResponse.getDetail();

                    if(success == true) {

                        for(int i = 0; i < detail.size(); i++) {

                            String no = detail.get(i).getNo();
                            String id = detail.get(i).getId();
                            String name = detail.get(i).getName();
                            String email = detail.get(i).getEmail();
                            String phone = detail.get(i).getPhone();
                            String photo = detail.get(i).getPhoto();

                            Log.e("LoginActivity", "Detail : " + no + id + name + email + phone + photo + id);
                            sessionManager.createSession(no, id, name, email, phone);

                            Toast.makeText(LoginActivity.this, "'"+ id + "'님 환영합니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        }

                    } else {

                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            Toast.makeText(this, "알맞게 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

}
