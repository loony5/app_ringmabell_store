package com.example.carpe.ringmabell_store;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.carpe.ringmabell_store.API.API;
import com.example.carpe.ringmabell_store.API.RetrofitClient;
import com.example.carpe.ringmabell_store.MODEL.ImageUploadResponse;
import com.example.carpe.ringmabell_store.MODEL.ReadResponse;
import com.example.carpe.ringmabell_store.MODEL.RegisterResponse;
import com.example.carpe.ringmabell_store.MODEL.StoreUser;
import com.example.carpe.ringmabell_store.UTIL.Permission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = EditActivity.class.getSimpleName();

    private EditText edit_id, edit_name, edit_email, edit_phone;
    private Button btn_email_edit, btn_phone_edit, btn_logout;
    private TextView btn_image_edit;

    CircleImageView img_profile;
    Bitmap bitmap;

    SessionManager sessionManager;
    String getNO, getID, getName, getEmail, getPhone;

    EditText alert_edit;
    String strId, strName, strEmail, strPhone, strPhoto;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edit_id = findViewById(R.id.edit_id);
        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        edit_phone = findViewById(R.id.edit_phone);

        img_profile = findViewById(R.id.img_profile);
        btn_image_edit = findViewById(R.id.btn_image_edit);

        // sessionManager 에서 user 에 저장된 ID 를 가져온다.
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getNO = user.get(sessionManager.NO);
        getID = user.get(sessionManager.ID);
        getName = user.get(sessionManager.NAME);
        getEmail = user.get(sessionManager.EMAIL);
        Log.e("EditActivity", "getEmail" + getEmail);
        getPhone = user.get(sessionManager.PHONE);


        btn_email_edit = findViewById(R.id.btn_email_edit);
        btn_phone_edit = findViewById(R.id.btn_phone_edit);
        btn_logout = findViewById(R.id.btn_logout);

        btn_email_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailModify();

            }
        });

        btn_phone_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneModify();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logout();

            }
        });

        // 프로필 사진 선택하면, chooseFile() 메소드 호출
        btn_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

//        resetGlide();
        getUserDetail();
    }

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

                        strId = read.get(i).getId();
                        strName = read.get(i).getName();
                        strEmail = read.get(i).getEmail();
                        strPhone = read.get(i).getPhone();
                        strPhoto = read.get(i).getPhoto();

                        edit_id.setText(strId);
                        edit_name.setText(strName);
                        edit_email.setText(strEmail);
                        edit_phone.setText(strPhone);

                        Log.e("MenuActivity", "strPhoto : " + strPhoto);

                        if(strPhoto == null) {
                            Log.e("MenuActivity", "현재 이미지 없음");
                        } else {

                            Uri filePath = Uri.parse(strPhoto);
                            Log.e("MenuActivity.java", "로그인 된 회원의 현재 이미지는 " + filePath);


                            Glide.with(EditActivity.this).load(filePath)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)).into(img_profile);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ReadResponse> call, Throwable t) {

                Toast.makeText(EditActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    // 프로필 사진을 선택했을 때, 갤러리 호출
    private void chooseFile() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    // chooseFile 메소드에서 호출한 갤러리에서 사진을 선택하면,
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // 이미지뷰에 보여준다.
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(img_profile);
            Log.e("EditActivity.java", "imageUri : " + imageUri);

            UploadPicture(getNO, imageUri);


//            try {
//
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//
//                UploadPicture(getNO, getStringImage(bitmap));
////                img_profile.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }

    // 데이터베이스에 이미지를 업로드하는 메소드이다.
//    private void UploadPicture(final  String id, final String photo) {
//
//        Call<ImageUploadResponse> call = RetrofitClient
//                .getInstance()
//                .getAPI()
//                .imageUpload(id, photo);
//
//        call.enqueue(new Callback<ImageUploadResponse>() {
//            @Override
//            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
//
//                    ImageUploadResponse imageUploadResponse = response.body();
//                    String success = imageUploadResponse.getSuccess();
//
//                    if(success.equals("1")) {
//
//                        Log.e("EditActivity.java", "response 는 " + response);
//                        Toast.makeText(EditActivity.this, "프로필 사진이 변경되었습니다.", Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(EditActivity.this, MenuActivity.class);
////                        startActivity(intent);
//
//                    } else {
//                        Toast.makeText(EditActivity.this, "다시 시도하세요.", Toast.LENGTH_SHORT).show();
//                    }
//            }
//
//            @Override
//            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
//                Toast.makeText(EditActivity.this, "다시 시도하세요." + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void UploadPicture(final String no, final Uri uri) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
                .checkSelfPermission(EditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {

            Permission.requestPermissionStorage(EditActivity.this);

        } else {

            Cursor cursor = null;

            String[] proj = {MediaStore.Images.Media.DATA};

            assert uri != null;

            cursor = getContentResolver().query(uri, proj, null, null, null);

            assert cursor != null;

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            File file = new File(cursor.getString(column_index));

            // uri.toPath() 로 하면 /external/images/media/*** 이렇게 경로를 가져옴.
//            File file = new File(uri.toString());
            Log.e("EditActivity.java", "file : " + file);
//
            String fileSize = "";

            if(file.exists()) {

                long lFileSize = file.length();
                fileSize = Long.toString(lFileSize) + ".bytes";
                Log.e("EditActivity.java", "fileSize : " + fileSize);
            } else {

                fileSize = "File not exist";
                Log.e("EditActivity.java", "fileSize : " + fileSize);
            }

            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("image/*"), file);

            // 파일명으로 하려면 no -> file.getName() 으로 해준다.
            MultipartBody.Part photo =
                    MultipartBody.Part.createFormData("photo", no + ".jpeg", requestBody);
            Log.e("EditActivity.java", "photo : " + photo);
            Log.e("EditActivity.java", "file.getName : " + file.getName());

            Call<ImageUploadResponse> call = RetrofitClient
                    .getInstance()
                    .getAPI()
                    .imageUpload(no, photo);

            Log.e("EditActivity.java", "id : " + no);

            call.enqueue(new Callback<ImageUploadResponse>() {
                @Override
                public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {

                    ImageUploadResponse imageUploadResponse = response.body();
                    String success = imageUploadResponse.getSuccess();
                    String message = imageUploadResponse.getMessage();

                    if (success.equals("1")) {

                        Log.e("EditActivity.java", "response 는 " + response);
                        Toast.makeText(EditActivity.this, "프로필 사진이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditActivity.this, "다시 시도하세요.", Toast.LENGTH_SHORT).show();
                        Log.e("EditActivity.java", "success : " + success);
                        Log.e("EditActivity.java", "message : " + message);

                    }

                }

                @Override
                public void onFailure(Call<ImageUploadResponse> call, Throwable t) {

                    Toast.makeText(EditActivity.this, "다시 시도하세요." + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("EditActivity.java", "onFailure Message : " + t.getMessage());

                }
            });
        }
    }



    // 갤러리에서 선택한 이미지의 용량을 줄이고 결과 값을 돌려준다.
//    public String getStringImage(Bitmap bitmap) {
//
//        // ByteArrayOutputStream 은 바이트 배열을 출력해주는 스트림이다.
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        // bitmap.compress -> 이미지 용량을 압축한다.
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//
//        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
//        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
//
//        return encodedImage;
//    }

    private void emailModify() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);

        final View view = LayoutInflater.from(EditActivity.this)
                .inflate(R.layout.alert_edit_modify, null, false);

        builder.setView(view);
        builder.setTitle("이메일");

        alert_edit = view.findViewById(R.id.alert_edit);
        alert_edit.setText(strEmail);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = alert_edit.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Toast.makeText(EditActivity.this, "이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

                } else if(email.equals(strEmail)) {

                    Toast.makeText(EditActivity.this, "기존에 등록된 이메일입니다.", Toast.LENGTH_SHORT).show();

                } else {

                    emailUpload(getNO, email);

                }
            }
        });

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

    private void emailUpload(final String no, final String email) {

        Call<RegisterResponse> call = RetrofitClient
                .getInstance()
                .getAPI()
                .store_modify(no, email);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                boolean success = response.body().isSuccess();
                String message = response.body().getMessage();


                if(success == true) {

                    getUserDetail();
                    sessionManager.createSession(getNO, getID, getName, alert_edit.getText().toString(), getPhone);
                    Toast.makeText(EditActivity.this, "이메일이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                } else {

                    if(message.equals("exist")) {

                        Toast.makeText(EditActivity.this, "이미 등록된 이메일입니다.", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(EditActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

            }
        });



    }

    private void phoneModify() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);

        final View view = LayoutInflater.from(EditActivity.this)
                .inflate(R.layout.alert_edit_modify, null, false);

        builder.setView(view);
        builder.setTitle("연락처");

        alert_edit = view.findViewById(R.id.alert_edit);
        alert_edit.setText(strPhone);

        // 연락처 입력시 자동으로 하이픈("-")을 넣어준다.
        alert_edit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String phone = alert_edit.getText().toString();

                if(!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phone)) {

                    Toast.makeText(EditActivity.this, "연락처를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

                } else if(phone.equals(strPhone)) {

                    Toast.makeText(EditActivity.this, "기존에 등록된 연락처입니다.", Toast.LENGTH_SHORT).show();

                } else {

                    phoneUpload(getNO, phone);

                }
            }
        });

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

    private void phoneUpload(final String no, final String phone) {

        Call<RegisterResponse> call = RetrofitClient
                .getInstance()
                .getAPI()
                .store_modify_phone(no, phone);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                boolean success = response.body().isSuccess();
                String message = response.body().getMessage();


                if(success == true) {

                    getUserDetail();
                    sessionManager.createSession(getNO, getID, getName, alert_edit.getText().toString(), getPhone);
                    Toast.makeText(EditActivity.this, "연락처가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                } else {

                    if(message.equals("exist")) {

                        Toast.makeText(EditActivity.this, "이미 등록된 연락처가 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(EditActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        getUserDetail();
//    }


    // 뒤로가기를 선택했을때, 이미지 업로드
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if(event.getAction() == KeyEvent.ACTION_DOWN) {
//            if(keyCode == KeyEvent.KEYCODE_BACK) {
//
//                UploadPicture(getNO, getStringImage(bitmap));
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//
//    }
}
