package com.example.myapplication;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ShowWallpaper extends AppCompatActivity {
    ArrayList<String> arrayList=new ArrayList<>();
    static int i=0;
    Bitmap bitmap;
    RelativeLayout relativeLayout;
    Button button;
    Intent it;
    String category;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wallpaper);
        imageView=findViewById(R.id.img);
        button=findViewById(R.id.outlined_button);
        relativeLayout=findViewById(R.id.layoutt);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        it=getIntent();
        Bundle b=it.getExtras();
        if(b!=null){
            category=b.getString("url");
            if(!category.isEmpty()){
                loadimg2(category,1,100);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    applyBack();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if(arrayList.size()>0){
            Picasso.get().load(arrayList.get(i)).into(imageView);

        }
        imageView.setOnTouchListener(new OnSwipeTouchListener(ShowWallpaper.this){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                i++;
                Picasso.get().load(arrayList.get(i)).into(imageView);
                Drawable dr = new BitmapDrawable(bitmap);
                relativeLayout.setBackground(dr);
                if(i>7){
                    loadimg2(category,1,10);
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                if(i!=0){
                    i--;
                }Picasso.get().load(arrayList.get(i)).into(imageView);
                Drawable dr = new BitmapDrawable(bitmap);
                relativeLayout.setBackground(dr);
            }
        });
    }
    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public GetImageFromUrl(ImageView img){
            this.imageView = img;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            Drawable dr = new BitmapDrawable(bitmap);
            relativeLayout.setBackground(dr);
        }
    }
    private void showToast(String mess){
        Toast.makeText(ShowWallpaper.this, mess, Toast.LENGTH_SHORT).show();
    }
    private void applyBack() throws IOException {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        wallpaperManager.setBitmap(bitmap);
        showToast("Wallpaper Updated");
    }
    private void loadimg2(String cat,int p,int limit) {
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://api.unsplash.com/")
                .addConverterFactory(ScalarsConverterFactory.create()).build();
        MainInterface mainInerface=retrofit.create(MainInterface.class);
        retrofit2.Call<String> call=mainInerface.STRING_CALL2(cat,p,limit);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
                if(response.isSuccessful() && response.body()!=null){
                    try {
                        JSONObject jsonObject=new JSONObject(response.body());
                        JSONArray jsonArray=jsonObject.optJSONArray("results");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject js=jsonArray.getJSONObject(i);
                            Log.d("TAG",js.toString());
                            JSONObject js2=js.getJSONObject("urls");
                            String url =js2.getString("regular");
                            Log.d("TAG","message"+url);
                            arrayList.add(url);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new GetImageFromUrl(imageView).execute(arrayList.get(0));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {

            }
        });
    }
}