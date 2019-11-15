package com.example.carpe.ringmabell_store.UTIL;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.carpe.ringmabell_store.BuildConfig;

public class Permission {

    public static void requestPermissionStorage (final Activity context) {

        if(ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            AlertDialog.Builder builderExplain = new AlertDialog.Builder(context);
            builderExplain.setCancelable(false);
            builderExplain.setMessage("이 기능을 사용하려면 갤러리에 접근해야합니다.");
            builderExplain.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    
                    ActivityCompat.requestPermissions(context, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE);
                }
            });
            builderExplain.show();
        } else {
            
            ActivityCompat.requestPermissions(context, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE);
        }
    }
    
    public static void requestPermissionStorageDeny (final Activity context) {
        
        if(ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(context, "이 기능을 사용하려면 갤러리 접근을 허용해야합니다.", Toast.LENGTH_SHORT).show();
        } else {

            AlertDialog.Builder builderExplain = new AlertDialog.Builder(context);
            builderExplain.setCancelable(false);
            builderExplain.setMessage("갤러리 접근을 허용하시겠습니까?");
            builderExplain.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    context.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package : " + BuildConfig.APPLICATION_ID)));
                }
            });

            builderExplain.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderExplain.show();
        }
    }
}
