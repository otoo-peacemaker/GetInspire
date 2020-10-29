package com.kwesiwelbred.getinspire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwesiwelbred.getinspire.adapters.PhotosAdapter;
import com.kwesiwelbred.getinspire.models.Quote;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    List<Quote> wallpaperList;
    List<Quote> favList;
    RecyclerView recyclerView;
    PhotosAdapter adapter;
    Context context;
    DatabaseReference dbWallpapers, dbFavs;
    ProgressBar progressBar;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("category");
        setSupportActionBar(toolbar);
        toolbar.setTitle(category);
        setTitle(category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        loadBannerAds();

        favList = new ArrayList<>();
        wallpaperList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotosAdapter(this, wallpaperList);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showInterAd(position);
            }
        }));

        progressBar = findViewById(R.id.progressbar);

        dbWallpapers = FirebaseDatabase.getInstance().getReference("photos");


        fetchWallpapers();

    }

    private void showInterAd(int position) {

        final Intent intshow = new Intent(this, All_Inflater_Activity.class);
        intshow.putExtra("POSITION", position);
        intshow.putExtra("array", (Serializable) wallpaperList);
        startActivity(intshow);
    }

    private void fetchWallpapers() {
        progressBar.setVisibility(View.VISIBLE);
        dbWallpapers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapshot : dataSnapshot.getChildren()) {

                        Quote quote = wallpaperSnapshot.getValue(Quote.class);


                        favList.add(quote);
                    }
                    for (int i = 0; i < favList.size(); i++) {
                        if (favList.get(i).getCategory().equals(category)) {
                            wallpaperList.add(favList.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadBannerAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}