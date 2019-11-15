package com.example.carpe.ringmabell_store.ADAPTER;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carpe.ringmabell_store.MODEL.Picture;
import com.example.carpe.ringmabell_store.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GalleyItemAdapter extends RecyclerView.Adapter<GalleyItemAdapter.GalleyItemViewHolder> {

    private List<Picture> pictures;
    private Context context;
    private List<Picture> picturesSelected;
    int count = 0;


    public GalleyItemAdapter(Context context, List<Picture> pictures) {

        this.context = context;
        this.pictures = pictures;
        this.picturesSelected = new ArrayList<>();

    }


    // 아이템을 생성하여 뷰홀더에 넣는 역할
    @NonNull
    @Override
    public GalleyItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_gallery_picture, viewGroup, false);

        return new GalleyItemViewHolder(itemView);

    }


    // onCreateViewHolder 에서 리턴한 뷰홀더에 데이터를 세팅해주는 역할
    @Override
    public void onBindViewHolder(@NonNull GalleyItemViewHolder galleyItemViewHolder, int position) {

        galleyItemViewHolder.bind(pictures.get(position), position);

    }

    // 아이템의 총 갯수 반환
    @Override
    public int getItemCount() {

        return pictures.size();

    }


    // 아이템 뷰홀더 정의
    public class GalleyItemViewHolder extends RecyclerView.ViewHolder{

        ImageView img_PictureItem;
        TextView txt_selectCount;
        ConstraintLayout item_gallery;

        public GalleyItemViewHolder(@NonNull View itemView) {

            super(itemView);
            img_PictureItem = itemView.findViewById(R.id.img_PictureItem);
            txt_selectCount = itemView.findViewById(R.id.txt_selectCount);
            item_gallery = itemView.findViewById(R.id.item_gallery);

        }


        // 뷰에 데이터 set
        public void bind(final Picture picture, final int position) {

            // Glide option 지정
            RequestOptions options = new RequestOptions()
                    .fitCenter();
//                    .skipMemoryCache(true)
//                    .override(200,200)
//                    .placeholder(R.drawable.ic_launcher_background);


            Glide.with(context).load(picture.getPath())
                    .apply(options)
                    .into(img_PictureItem);

            // 이미지를 선택했을때,
            if(picture.getSelectCount() > 0) {

                // 선택한 값을 텍스트에 set
                txt_selectCount.setText(picture.getSelectCount() + "");
                txt_selectCount.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_selected_count));

            // 이미지를 선택하지 않았을때,
            } else {

                txt_selectCount.setText("");
                txt_selectCount.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_not_selected_count));

            }

            // 뷰를 선택했을때,
            item_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 선택한 포지션의 int 값을 picture position 에 set
                    picture.setPosition(position);

                    // picture 에서 가져온 selectCount 값이 0보다 크면,
                    // 선택한 포지션이 있으면, -> 이미 선택했으면,
                    if(picture.getSelectCount() > 0) {

                        // count 를 뺀다.
                        count--;
                        txt_selectCount.setText("");
                        txt_selectCount.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_not_selected_count));

                        // picture 에서 제거한다.
                        picturesSelected.remove(picture);

                        // pictures 의 값을 모두 돌리는 foreach 문
                        for (Picture pictureUpdate : pictures) {

                            if(pictureUpdate.getSelectCount() > picture.getSelectCount()) {

                                pictureUpdate.setSelectCount(pictureUpdate.getSelectCount() -1);
                                notifyItemChanged(pictureUpdate.getPosition());

                            }
                        }

                        picture.setSelectCount(0);

                    } else {

                        count++;
                        picture.setSelectCount(count);
                        txt_selectCount.setText(picture.getSelectCount() + "");
                        txt_selectCount.setBackground(ContextCompat
                                .getDrawable(context, R.drawable.bg_selected_count));
                        picturesSelected.add(picture);

                    }
                }
            });

        }
    }

    public ArrayList<Picture> getAllPictureSelect() {

        // ArrayList 정렬
        Collections.sort(picturesSelected, new Comparator<Picture>() {
            @Override
            public int compare(Picture o1, Picture o2) {
                return o1.getSelectCount() >= o2.getSelectCount() ?1:-1;
            }
        });

        return (ArrayList<Picture>) picturesSelected;

    }
}
