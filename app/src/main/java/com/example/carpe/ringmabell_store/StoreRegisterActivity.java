package com.example.carpe.ringmabell_store;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.carpe.ringmabell_store.ADAPTER.SelectedImageAdapter;
import com.example.carpe.ringmabell_store.API.RetrofitClient;
import com.example.carpe.ringmabell_store.MODEL.Picture;
import com.example.carpe.ringmabell_store.MODEL.StoreInfo;
import com.example.carpe.ringmabell_store.MODEL.StoreInfoReadResponse;
import com.example.carpe.ringmabell_store.MODEL.StoreRegisterResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreRegisterActivity extends AppCompatActivity {

    private TextView txt_registerImage, txt_storeEdit, txt_startTime, txt_finishTime, txt_personalDay, txt_address;
    private EditText edit_storeName, edit_introduce;

    private ImageView store_image;

    private int myHour, myMinute;

    private Calendar calendar = Calendar.getInstance();

    SessionManager sessionManager;
    String getNO;

    private RecyclerView recyclerViewSelectedImage;
    SelectedImageAdapter adapter;

    ArrayList<Picture> selectedPictures;

//    String strNo, strName, strStartTime, strFinishTime, strPesonalDay, strAddress, strIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_register);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getNO = user.get(sessionManager.NO);

        txt_storeEdit = findViewById(R.id.txt_storeEdit);

        edit_storeName = findViewById(R.id.edit_storeName);
        txt_startTime = findViewById(R.id.txt_startTime);
        txt_finishTime = findViewById(R.id.txt_finishTime);
        txt_personalDay = findViewById(R.id.txt_personalDay);
        txt_address = findViewById(R.id.txt_address);
        edit_introduce = findViewById(R.id.edit_storeIntroduce);

//        store_image = findViewById(R.id.store_image);
        txt_registerImage = findViewById(R.id.txt_registerImage);

        myHour = calendar.get(Calendar.HOUR_OF_DAY);
        myMinute = calendar.get(Calendar.MINUTE);

        recyclerViewSelectedImage = findViewById(R.id.recyclerView_storeImageList);

        txt_registerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StoreRegisterActivity.this, GalleryActivity.class);
                startActivityForResult(intent, 1000);

            }
        });



        // 영업 시작시간 선택했을때,
        txt_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new TimePickerDialog(StoreRegisterActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar, startTimeSetListener, myHour, myMinute, false);
                dialog.show();
            }
        });

        // 영업 종료시간 선택했을때,
        txt_finishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new TimePickerDialog(StoreRegisterActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar, finishTimeSetListener, myHour, myMinute, false);
                dialog.show();

            }
        });


        // 휴무일 선택하기
        txt_personalDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Dialog_personalDay();

            }
        });

        // 주소 선택했을 때,
        txt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StoreRegisterActivity.this, StoreAddressActivity.class);
                startActivityForResult(intent, 3000);

            }
        });

        // 완료 버튼 클릭했을때, 서버로 데이터 보내기

            txt_storeEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String storeId = getNO;
                    String storeName = edit_storeName.getText().toString();
                    String startTime = txt_startTime.getText().toString();
                    String finishTime = txt_finishTime.getText().toString();
                    String personalDay = txt_personalDay.getText().toString();
                    String address = txt_address.getText().toString();
                    String introduce = edit_introduce.getText().toString();


                    // 빈 칸이 있으면 토스트메시지
                    if(storeName.equals("") || startTime.equals("") || finishTime.equals("") || personalDay.equals("") || address.equals("") || introduce.equals("")) {

                        Toast.makeText(StoreRegisterActivity.this, "모두 입력해주세요.", Toast.LENGTH_SHORT).show();

                        // 모두 잘 입력했을 때, 서버로 데이터 보내기
                    } else {

                        Log.e("StoreRegisterActivity", "storeName : " + storeName);

                        RequestBody r_storeId = RequestBody.create(MediaType.parse("text/plain"), storeId);
                        RequestBody r_storeName = RequestBody.create(MediaType.parse("text/plain"), storeName);
                        RequestBody r_startTime = RequestBody.create(MediaType.parse("text/plain"), startTime);
                        RequestBody r_finishTime = RequestBody.create(MediaType.parse("text/plain"), finishTime);
                        RequestBody r_personalDay = RequestBody.create(MediaType.parse("text/plain"), personalDay);
                        RequestBody r_address = RequestBody.create(MediaType.parse("text/plain"), address);
                        RequestBody r_introduce = RequestBody.create(MediaType.parse("text/plain"), introduce);

                        Call<StoreRegisterResponse> call = RetrofitClient
                                .getInstance()
                                .getAPI()
                                .StoreInfoUpload(r_storeId, r_storeName, r_startTime, r_finishTime, r_personalDay, r_address, r_introduce);

                        call.enqueue(new Callback<StoreRegisterResponse>() {
                            @Override
                            public void onResponse(Call<StoreRegisterResponse> call, Response<StoreRegisterResponse> response) {

                                StoreRegisterResponse storeRegisterResponse = response.body();

                                String success = storeRegisterResponse.getSuccess();
                                String message = storeRegisterResponse.getMessage();

                                if(success.equals("2")) {

                                    Toast.makeText(StoreRegisterActivity.this, "이미 등록된 업체 정보가 있습니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("StoreRegisterActivity", "message : " + message);

                                } else if(success.equals("1")) {

                                    Toast.makeText(StoreRegisterActivity.this, "업체 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("StoreRegisterActivity", "message : " + message);

                                    Intent intent = new Intent(StoreRegisterActivity.this, MenuActivity.class);
                                    startActivity(intent);

                                } else {

                                    Toast.makeText(StoreRegisterActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                    Log.e("StoreRegisterActivity", "message : " + message);
                                    Log.e("StoreRegisterActivity", "success : " + success);
                                }

                            }

                            @Override
                            public void onFailure(Call<StoreRegisterResponse> call, Throwable t) {

                                Toast.makeText(StoreRegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 3000) {

            txt_address.setText(data.getStringExtra("address"));

        } else if(resultCode == RESULT_OK && requestCode == 1000) {

            Log.e("StoreRegister", "requestCode" + requestCode);

            selectedPictures = data.getParcelableArrayListExtra("selectPicture");
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewSelectedImage.setLayoutManager(layoutManager);

            adapter = new SelectedImageAdapter(this, selectedPictures);
            recyclerViewSelectedImage.setAdapter(adapter);

            Log.e("StoreRegisterActivity.java", "selectedPictures : " + selectedPictures.size());
        }
    }

    // 영업 시작시간 타임피커
    private TimePickerDialog.OnTimeSetListener startTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            // timeSet 메소드에서 리턴값 가져와서 보여주기
            String time = timeSet(hourOfDay, minute);
            txt_startTime.setText(time);
        }
    };

    // 영업 종료시간 타임피커
    private TimePickerDialog.OnTimeSetListener finishTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            // timeSet 메소드에서 리턴값 가져와서 보여주기
            String time = timeSet(hourOfDay, minute);
            txt_finishTime.setText(time);

        }
    };

    // 시간 표시 메소드
    public String timeSet(int hourOfDay, int minute) {

        String AM_PM, hour, minute02;

        // AM, PM 설정
        if(hourOfDay < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }

        // 시, 분이 한자리일 경우, 1 : 5 이렇게 나오는데, 01 : 05 이렇게 나오게 해주기
        if(String.valueOf(hourOfDay).length() == 1) {
            hour = "0" + String.valueOf(hourOfDay);
        } else {
            hour = String.valueOf(hourOfDay);
        }

        if(String.valueOf(minute).length() == 1) {
            minute02 = "0" + String .valueOf(minute);
        } else {
            minute02 = String.valueOf(minute);
        }

        // PM 일 경우 24시간 표기로 나오는 데, 12시간 표기로 나오게 해주자.
        if(AM_PM.equals("PM")) {

            int int_hour = Integer.parseInt(hour);
            if(int_hour > 12) {

                int f_hour = int_hour - 12;

                if(String.valueOf(f_hour).length() == 1) {

                    hour = "0" + String.valueOf(f_hour);
                } else {

                    hour = String.valueOf(f_hour);
                }
            }
        }

        // 시간 표기 ex) AM 09 : 30 이렇게!
        String time = AM_PM + " " + hour + " : "
                + minute02;

        return time;
    }

    // 휴무일 입력 다이얼로그 보여주기
    public void Dialog_personalDay() {

        // builder 에 AlertDialog.Builder 인스턴스
        AlertDialog.Builder builder = new AlertDialog.Builder(StoreRegisterActivity.this);

        // view 에 만들어 놓은 다이얼로그 레이아웃을 담는다.
        final View view = LayoutInflater.from(StoreRegisterActivity.this)
                .inflate(R.layout.alert_personalday_set, null, false);

        // builder 에 view 를 set
        builder.setView(view);

        // builder 에 확인 버튼 set -> 선택했을 때, 이벤트.
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RadioGroup radioGroup = view.findViewById(R.id.radioGroup);

                int checked = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = radioGroup.findViewById(checked);
                String chx_personalDay = radioButton.getText().toString();

                if(radioButton.isChecked()) {

                    // radioButton 을 직접 입력을 선택했을 경우, EditText 값이 비어 있으면 Toast, 아니면 set
                    if(chx_personalDay.equals("직접 입력")) {

                        EditText edit_personalDay = view.findViewById(R.id.edit_personalDay);
                        String str_personalDay = edit_personalDay.getText().toString();

                        if(str_personalDay.getBytes().length >0) {

                            txt_personalDay.setText(str_personalDay);
                        } else {

                            // StoreRegisterActivity.this 로 하면 다이얼로그를 닫고 액티비티에서 토스트메시지를 보여준다.
                            Toast.makeText(getBaseContext(), "직접 입력칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // radioButton 이 "직접 입력" 이 아닐경우 선택한 내용으로 set
                        txt_personalDay.setText(chx_personalDay);
                    }

                }

            }
        });

        // builder 에 취소 버튼 set -> 취소버튼 선택했을 때, 이벤트
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // dialog 에 설정해놓은 builder 생성
        AlertDialog dialog = builder.create();

        // 다이얼로그 밖을 선택했을때, 꺼지지 않게 하기
        dialog.setCanceledOnTouchOutside(false);

        // dialog 보여주기
        dialog.show();

    }


    // 업체정보 가져오기
    private void getStoreInfo() {

        Call<StoreInfoReadResponse> call = RetrofitClient
                .getInstance()
                .getAPI()
                .storeInfoRead(getNO);

        call.enqueue(new Callback<StoreInfoReadResponse>() {
            @Override
            public void onResponse(Call<StoreInfoReadResponse> call, Response<StoreInfoReadResponse> response) {
                Log.e("StoreRegisterActivity", "onResponse");

                StoreInfoReadResponse storeInfoReadResponse = response.body();
                String success = storeInfoReadResponse.getSuccess();

                List<StoreInfo> read = storeInfoReadResponse.getRead();

                if(success.equals("1")) {

//                    txt_storeEdit.setText("수정하기");

                    for(int i = 0; i < read.size(); i++) {

                        edit_storeName.setText(read.get(i).getStoreName());
                        txt_startTime.setText(read.get(i).getStartTime());
                        txt_finishTime.setText(read.get(i).getFinishTime());
                        txt_personalDay.setText(read.get(i).getPersonalDay());
                        txt_address.setText(read.get(i).getAddress());
                        edit_introduce.setText(read.get(i).getIntroduce());

                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfoReadResponse> call, Throwable t) {
                Log.e("StoreRegisterActivity", t.getMessage());
//                Toast.makeText(StoreRegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    // 화면이 보이지 않을때, 업제정보를 다시 가져오자
    @Override
    protected void onResume() {
        super.onResume();

        getStoreInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter != null) {

            recyclerViewSelectedImage = null;
            adapter = null;
            Runtime.getRuntime().gc();

        }
    }
}
