package com.example.carpe.ringmabell_store;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class StoreAddressActivity extends AppCompatActivity {

    private WebView daum_webView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_address);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();

    }

    public void init_webView() {

        daum_webView = findViewById(R.id.daum_webView);

        // JavaScript 의 window.open 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);

        //JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript 의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript 이벤트에 대응할 함수를 정의한 클래스를 보여줌
        daum_webView.addJavascriptInterface(new AndroidBridge(), "Store_Address");

        // web client 를 chrome 으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());

        // webView url load.php 파일 주소
        daum_webView.loadUrl("http://ec2-52-78-18-104.ap-northeast-2.compute.amazonaws.com/ringmabell_store/store_address.php");
    }

    private class AndroidBridge {

        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent();
                    intent.putExtra("address", String.format("(%s) %s %s", arg1, arg2, arg3));
                    setResult(RESULT_OK, intent);
                    finish();

                    // WebView 를 초기화 하지 않으면 재사용 할 수 없음.
                    init_webView();

                }
            });
        }
    }
}
