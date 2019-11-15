package com.example.carpe.ringmabell_store;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.carpe.ringmabell_store.ADAPTER.GalleyItemAdapter;
import com.example.carpe.ringmabell_store.MODEL.Picture;
import com.example.carpe.ringmabell_store.UTIL.ConstantDataManager;
import com.example.carpe.ringmabell_store.UTIL.Permission;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGallery;
    private ArrayList<Picture> pictures;
    GalleyItemAdapter adapter;
    Handler handler;

    private TextView txt_selectSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        txt_selectSuccess = findViewById(R.id.txt_selectSuccess);

        txt_selectSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GalleryActivity.this, StoreRegisterActivity.class);

                ArrayList<Picture> picturesSelected = adapter.getAllPictureSelect();
                intent.putParcelableArrayListExtra("selectPicture", picturesSelected);
//                startActivity(intent);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        pictures = new ArrayList<>();
        recyclerViewGallery = findViewById(R.id.recyclerView_gallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new GalleyItemAdapter(this, pictures);
        recyclerViewGallery.setAdapter(adapter);

        handler = new Handler();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
                .checkSelfPermission(GalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            Permission.requestPermissionStorage(GalleryActivity.this);
        } else {

            new Thread() {

                @Override
                public void run() {
                    Looper.prepare();
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            pictures.clear();
                            pictures.addAll(Picture.getGalleryPhotos(GalleryActivity.this));
                            adapter.notifyDataSetChanged();
                        }
                    });
                    Looper.loop();
                }
            }.start();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE) {

            if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                new Thread(){

                    @Override
                    public void run() {
                        Looper.prepare();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                pictures.clear();
                                pictures.addAll(Picture.getGalleryPhotos(GalleryActivity.this));
                                adapter.notifyDataSetChanged();

                            }
                        });
                        Looper.loop();
                    }
                }.start();
            } else {

                Permission.requestPermissionStorageDeny(GalleryActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
