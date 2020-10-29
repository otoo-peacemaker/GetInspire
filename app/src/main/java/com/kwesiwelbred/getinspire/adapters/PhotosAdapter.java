package com.kwesiwelbred.getinspire.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kwesiwelbred.getinspire.R;
import com.kwesiwelbred.getinspire.models.Quote;

import java.util.List;


public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.WallpaperViewHolder> {

    private Context mCtx;
    private List<Quote> wallpaperList;



    public PhotosAdapter(Context mCtx, List<Quote> wallpaperList) {
        this.mCtx = mCtx;
        this.wallpaperList = wallpaperList;
    }

    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_photos, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallpaperViewHolder holder, int position) {
        final Quote w = wallpaperList.get(position);

        //holder.textView.setText(w.getTitle());

        Glide.with(mCtx)
                .load(w.getUrl())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;


        public WallpaperViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_images);

        }
    }
}
