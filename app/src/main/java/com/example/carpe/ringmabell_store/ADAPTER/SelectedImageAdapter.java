package com.example.carpe.ringmabell_store.ADAPTER;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carpe.ringmabell_store.MODEL.Picture;
import com.example.carpe.ringmabell_store.R;

import java.util.List;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.ImageItemViewHolder> {

    private Context context;
    private List<Picture> pictures;

    public SelectedImageAdapter(Context context, List<Picture> pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public ImageItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_selected_image, viewGroup, false);
        return new ImageItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageItemViewHolder imageItemViewHolder, int i) {

        imageItemViewHolder.bind(pictures.get(i));

    }

    @Override
    public int getItemCount() {
        return pictures.size();

    }


    public class ImageItemViewHolder extends RecyclerView.ViewHolder{

        ImageView img_selected_image;

        public ImageItemViewHolder(View itemView) {
            super(itemView);
            img_selected_image = itemView.findViewById(R.id.img_selected_image);
        }

        public void bind(Picture picture) {

            RequestOptions options = new RequestOptions().fitCenter();
            Glide.with(context).load(picture.getPath()).apply(options).into(img_selected_image);
        }
    }
}
