package com.kwesiwelbred.getinspire;

import android.Manifest;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.kwesiwelbred.getinspire.models.Quote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class All_Inflater_Activity extends AppCompatActivity {


    TextView txtmaintext;
    ImageView imageView;
    private int currentPosition = 0;
    private static int TOTAL_TEXT;
    private Dialog loadingDialog;
    ViewPager viewPager;
    CustomPagerAdapter customPagerAdapter;
    ArrayList<Quote> arrayList;
    LinearLayout fabDownload, fabWhatsapp , fabShare;
    static final int PERMISION_REQUEST_CODE = 1000;
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alllist_inflater_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        root = findViewById(R.id.root);

        //loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.gradient_actionbar_round));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        arrayList = new ArrayList<>();

        viewPager = findViewById(R.id.view_pager_extended);
        customPagerAdapter = new CustomPagerAdapter(All_Inflater_Activity.this);

        Intent i = getIntent();
        currentPosition = i.getIntExtra("POSITION", 0);
        arrayList = (ArrayList<Quote>) i.getSerializableExtra("array");

        TOTAL_TEXT = (arrayList.size() - 1);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setCurrentItem(currentPosition, true);
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.setOffscreenPageLimit(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /* if (currentPosition == TOTAL_TEXT) {
                    positionToMoveTo = 0;
                }*/
                currentPosition = position;
                changePreviewText(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("scroll", "PageScrollStateChanged");
            }
        });

        //fab buttons
        fabDownload = findViewById(R.id.fabDownload);
        fabDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(All_Inflater_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISION_REQUEST_CODE);

                }else{
                    downloadBitmap();
                }
            }
        });

        fabShare = findViewById(R.id.fabShare);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWallpaper();
            }
        });
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.layout_viewpager, container, false);

            LinearLayout ll = itemView.findViewById(R.id.ll_viewpager);
            imageView = itemView.findViewById(R.id.iv_full);


            Glide.with(All_Inflater_Activity.this)
                    .load(arrayList.get(position).getUrl())
                    .into(imageView);

            container.addView(ll, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            return ll;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void changePreviewText(int position) {
        currentPosition = position;
        Log.d("Main", "Current position: " + position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareWallpaper() {

        Glide.with(this)
                .asBitmap()
                .load(arrayList.get(currentPosition).getUrl())
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                              Intent intent = new Intent(Intent.ACTION_SEND);
                              intent.setType("image/*");
                              String shareBodyText = "https://play.google.com/store/apps/details?id="+getPackageName();
                              intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
                              intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
                              intent.putExtra(Intent.EXTRA_TEXT,shareBodyText);

                              startActivity(Intent.createChooser(intent, "Quotes App"));
                          }
                      }
                );
    }

    private void shareWhatsapp() {

        Glide.with(this)
                .asBitmap()
                .load(arrayList.get(currentPosition).getUrl())
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                              Intent intent = new Intent(Intent.ACTION_SEND);
                              intent.setType("image/*");
                              intent.setPackage("com.whatsapp");
                              String shareBodyText = "https://play.google.com/store/apps/details?id="+getPackageName();
                              intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
                              intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
                              intent.putExtra(Intent.EXTRA_TEXT,shareBodyText);

                              startActivity(Intent.createChooser(intent, "Quotes App"));
                          }
                      }
                );
    }


    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "wallpaper" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void downloadBitmap() {

        loadingDialog.show();
        Glide.with(this)
                .asBitmap()
                .load(arrayList.get(currentPosition).getUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void saveBitmap(Bitmap bitmap) {
        String fileName  = UUID.randomUUID()+".jpg";
        String path = Environment.getExternalStorageDirectory().toString();
        File folder = new File(path+"/"+ getString(R.string.app_name));
        folder.mkdir();

        File file = new File(folder,fileName);
        if (file.exists())
            file.delete();

        try {

            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();

            //send pictures to gallery
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            Snackbar.make(root,"Downlaod Successfull",Snackbar.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadingDialog.dismiss();
    }
} 