package com.example.carpe.ringmabell_store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor; // user 를 저장하기 위한
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";

    public static final String NO = "NO";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAil";
    public static final String PHONE = "PHONE";
//    public static final String PHOTO = "PHOTO";


    public SessionManager(Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();

    }

    // LoginActivity.java 에서 login.php 로 받은 값을 shared 에 담아 저장한다.
    public void createSession(String no, String id, String name, String email, String phone) {

        editor.putBoolean(LOGIN, true);
        editor.putString(NO, no);
        editor.putString(ID, id);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(PHONE, phone);
//        editor.putString(PHOTO, photo);

        editor.apply();

    }

    public boolean isLogin() {

        return sharedPreferences.getBoolean(LOGIN, false);
    }


    public void checkLogin() {

        // 로그인 상태가 아니면(isLogin() 값이 false 가 아니면), 회원가입 & 로그인 화면(InfoActivity.class)으로 이동
        if(!this.isLogin()) {
            Intent i = new Intent(context, InfoActivity.class);
            context.startActivity(i);
            ((MenuActivity) context).finish();
        }

    }



    public HashMap<String, String> getUserDetail() {

        HashMap<String, String> user = new HashMap<>();
        user.put(NO, sharedPreferences.getString(NO, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PHONE, sharedPreferences.getString(PHONE, null));
//        user.put(PHOTO, sharedPreferences.getString(PHOTO, null));


        return user;
    }

    // MenuActivity.java 에서 로그아웃 버튼을 선택했을 때 실행하는 메소드.
    // shared 에 저장된 값을 삭제하고, 회원가입 & 로그인 화면(InfoActivity.class) 으로 이동한다.
    public void logout() {

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, InfoActivity.class);
        context.startActivity(i);
        ((EditActivity) context).finish();

    }

}
