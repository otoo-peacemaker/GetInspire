package com.kwesiwelbred.getinspire.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.kwesiwelbred.getinspire.R;
import com.kwesiwelbred.getinspire.ViewPagerActivity;
import com.kwesiwelbred.getinspire.models.Category;

import java.util.List;


public class PhotoCategoriesAdapter extends RecyclerView.Adapter<PhotoCategoriesAdapter.CategoryViewHolder> {

    private Context mCtx;
    private List<Category> categoryList;

    private InterstitialAd mInterstitialAd;

    public PhotoCategoriesAdapter(Context mCtx, List<Category> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;

        mInterstitialAd = new InterstitialAd(mCtx);
        mInterstitialAd.setAdUnitId(mCtx.getString(R.string.interstitial_ads));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_photo_cat, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category c = categoryList.get(position);
        holder.textView.setText(c.name);
        Glide.with(mCtx)
                .load(c.thumb)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        ImageView imageView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_category);
            imageView = itemView.findViewById(R.id.iv_category);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//            } else {
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
            int p = getAdapterPosition();
            Category c = categoryList.get(p);

            Intent intent = new Intent(mCtx, ViewPagerActivity.class);
            intent.putExtra("category", c.name);

            mCtx.startActivity(intent);
        }
    }
}
