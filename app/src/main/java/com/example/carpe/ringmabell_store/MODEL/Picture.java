package com.example.carpe.ringmabell_store.MODEL;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Picture implements Parcelable {

    private String path;
    private int selectCount;
    private int position;

    public Picture() {

    }

    protected Picture(Parcel in) {

        path = in.readString();
        selectCount = in.readInt();
        position = in.readInt();

    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel source) {
            return new Picture(source);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
//        return super.equals(obj);
        return this.getSelectCount() == ((Picture)obj).getSelectCount();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(path);
        dest.writeInt(selectCount);
        dest.writeInt(position);

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static ArrayList<Picture> getGalleryPhotos(Context context) {

        ArrayList<Picture> pictures = new ArrayList<>();

        // ContentProvider 는 앱 내에서 사용할 수 있는 데이터를 공유하기 위한 컴포넌트(구성요소)
        // 오디오/비디오/이미지/주소록 등 정보를 제공받을 수 있다.
        // 데이터를 받기 위해서는 권한이 필요 -> Manifest 에 정의해주자.
        // 현재는 android.provider.MediaStore 의 정보를 받음

        // 외부저장소의 경로 정의
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Log.e("Picture.java", "uri : " + uri);

        // 결과로 수집할 칼럼들을 정의한다. 여기서 _ID 는 자동으로 유지하는 기본키 이다. DB 의 기본 키 개념
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

        // 기본키 _ID 를 변수로 정의
        String orderBy = MediaStore.Images.Media._ID;

        // ContentProvider 에 접근할 때에는 ContentResolver 를 통해서 접근할 수 있다.
        // query 를 이용해서 데이터를 검색하고 Cursor 객체로 반환한다. -> Cursor 로 반환하니 않으면 Exception 발생.
        // query 의 인수값은 Uri, projection, selection, selectionArgs, sortOrder
        // Uri 는 제공자 table 이라 할 수 있고, projection 은 검색된 각 행에 포함되어야하는 열, selection 은 행을 선택하는 기준
        // sortOrder 는 Cursor 내에 행이 나타나는 순서 -> 현재는 기본키로 순서를 나타낸다고 해준상태.
        Cursor cursorPhotos = context.getContentResolver().query(uri, columns, null, null, orderBy);
        Log.e("Picture.java", "cursorPhotos : " + cursorPhotos);

        if(cursorPhotos != null && cursorPhotos.getCount() > 0) {

            // Cursor 를 다음 행으로 이동시키면서 ArrayList pictures 에 추가
            while (cursorPhotos.moveToNext()) {

                Picture picture = new Picture();

                int indexPath = cursorPhotos.getColumnIndex(MediaStore.MediaColumns.DATA);
                picture.setPath(cursorPhotos.getString(indexPath));
                Log.e("Picture.java", "setPath : " + indexPath);

                pictures.add(picture);
                Log.e("Picture.java", "cursorPhotos : " + picture);
            }
        }

        // List 를 역으로 정렬 -> 내림차순으로
        Collections.reverse(pictures);
        return pictures;

    }

}
