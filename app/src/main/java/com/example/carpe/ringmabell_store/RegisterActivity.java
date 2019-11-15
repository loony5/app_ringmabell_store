package com.example.carpe.ringmabell_store;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carpe.ringmabell_store.ADAPTER.SelectedImageAdapter;
import com.example.carpe.ringmabell_store.API.RetrofitClient;
import com.example.carpe.ringmabell_store.MODEL.Picture;
import com.example.carpe.ringmabell_store.MODEL.RegisterResponse;
import com.example.carpe.ringmabell_store.MODEL.StoreUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText edit_ID, edit_name, edit_email, edit_password, edit_c_password, edit_phone;
    private Button btn_register_success;
    private CheckBox chx_all, chx_first, chx_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_ID = findViewById(R.id.edit_ID);
        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_c_password);
        edit_c_password = findViewById(R.id.edit_c_password);
        edit_phone = findViewById(R.id.edit_phone);

        // 연락처 입력시 자동으로 하이픈("-")을 넣어준다.
        edit_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // 이용 약관, 개인정보동의를 위한 체크박스
        chx_all = findViewById(R.id.chx_all);
        chx_first = findViewById(R.id.chx_first);
        chx_second = findViewById(R.id.chx_second);

        btn_register_success = findViewById(R.id.btn_register_success);

        chx_all.setOnClickListener(new CheckBox.OnClickListener(){

            @Override
            public void onClick(View v) {

                // toggle 은 현재 상태에서 반대로 체크를 하게 해준다.
                chx_first.toggle();
                chx_second.toggle();
            }
        });

        // 회원가입 버튼을 선택하면, storeRegister 메소드 호출
        btn_register_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeRegister();

            }
        });

    }


    public void storeRegister() {

        String str_ID = edit_ID.getText().toString().trim();
        String str_name = edit_name.getText().toString().trim();
        String str_email = edit_email.getText().toString().trim();
        String str_pass = edit_password.getText().toString().trim();
        String str_c_pass = edit_c_password.getText().toString().trim();
        String str_phone = edit_phone.getText().toString().trim();

        // 입력란이 하나라도 비어있는 경우 Toast 아니면, 비밀번호와 비밀번호 확인란이 같은 값인지 확인하고 Retrofit 통신.
        if(str_ID.isEmpty() || str_name.isEmpty() || str_email.isEmpty() || str_pass.isEmpty() || str_c_pass.isEmpty() || str_phone.isEmpty()) {

            Toast.makeText(RegisterActivity.this, "빈 칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();

        } else if ((!str_ID.isEmpty() && !str_name.isEmpty() && !str_email.isEmpty() && !str_pass.isEmpty() && !str_c_pass.isEmpty()
                && !str_phone.isEmpty()) && !chx_first.isChecked() || !chx_second.isChecked()) {

            Toast.makeText(RegisterActivity.this, "전체 동의를 해주세요.", Toast.LENGTH_SHORT).show();

            // 이메일 형식을 쉽게 확인할 수 있게 해준다.
        } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {

            Toast.makeText(RegisterActivity.this, "이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

            // 전화번호 형식 확인
        } else if(!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", str_phone)) {

            Toast.makeText(RegisterActivity.this, "연락처를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

        } else {

            if(!str_pass.equals(str_c_pass)){

                Toast.makeText(RegisterActivity.this, "비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT).show();
            } else {


                // 바르게 입력했을때, 미리 정의해둔 RetrofitClient.java 의 인스턴스, 객체 생성, 데이터 전달.
                Call<RegisterResponse> call = RetrofitClient
                        .getInstance()
                        .getAPI()
                        .storeRegister(str_ID, str_name, str_email, str_pass, str_phone);

                // 비동기방식으로 통식을 하려면 enqueue() 메소드를 사용한다.
                // 요청이 성공할 경우 onResponse, 요청이 실패한 경우 onFailure()
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                        RegisterResponse registerResponse = response.body();
                        boolean success = registerResponse.isSuccess();
                        String message = registerResponse.getMessage();

                        if(success == true) {

                            Toast.makeText(RegisterActivity.this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("RegisterActivity.java", "message : " + message);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {

                            if(message.equals("id exist")) {

                                Toast.makeText(RegisterActivity.this, "이미 가입된 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                            } else if(message.equals("email exist")) {

                                Toast.makeText(RegisterActivity.this, "이미 가입된 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                            } else if(message.equals("phone exist")) {

                                Toast.makeText(RegisterActivity.this, "이미 가입된 연락처가 있습니다.", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                Log.e("RegisterActivity.java", "message : " + message);

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {

                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

//                Call <ResponseBody> call = RetrofitClient
//                        .getInstance()
//                        .getAPI()
//                        .storeRegister(str_email,str_name, str_pass, str_phone);
//
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                        try {
//
//                            String s = response.body().string();
//                            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        }
    }

}
