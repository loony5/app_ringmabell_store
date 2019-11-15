package com.example.carpe.ringmabell_store;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private Button btn_register, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InfoActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

    }
}
