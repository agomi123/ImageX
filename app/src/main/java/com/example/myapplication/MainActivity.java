package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView courseRV,recyclerView2,recyclerView3;
    private ViewPager2 viewPager2;
    private EditText editText;
    CardView cardView1;
    private ArrayList<ImageClass> dataModalArrayList,arrayList2,sliderimage,arraylist3;
    private AdapterCard dataRVAdapter,dataRVAdapter2;
    private ImgAdapter adapter3;
    Handler handler=new Handler();
    boolean sele=true;
   // private DatabaseReference db,db2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        courseRV = findViewById(R.id.idRVItems);
        recyclerView2 = findViewById(R.id.recycler2);
        viewPager2=findViewById(R.id.viewpager);
        editText=findViewById(R.id.searchitem);
        cardView1 = findViewById(R.id.card1);
        sliderimage=new ArrayList<>();
        arraylist3=new ArrayList<>();
        recyclerView3=findViewById(R.id.recycler3);
        loadrecyclerViewData();
        loadrecyclerViewData2();
        if(editText.getText().toString().equals("")){
            loadimg2("random",1,10);
        }
        Intent it = new Intent(getApplicationContext(), ShowWallpaper.class);
        it.putExtra("url", "random");
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0) {
                    sele = false;
                    arraylist3.clear();
                    loadimg2("random", 1, 10);
                }
                else{
                    sele=true;
                    arraylist3.clear();
                    loadimg2(s.toString(),1,10);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(sliderrunnable);
                handler.postDelayed(sliderrunnable,5000);
            }
        });
       //db = FirebaseDatabase.getInstance().getReference();
       // db2 = FirebaseDatabase.getInstance().getReference();
        dataModalArrayList = new ArrayList<>();
       arrayList2 = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dataRVAdapter = new AdapterCard(dataModalArrayList, this);
        dataRVAdapter2 = new AdapterCard(arrayList2, this);
        recyclerView2.setAdapter(dataRVAdapter2);


    }
    private Runnable sliderrunnable=new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };
    private void loadrecyclerViewData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("topics");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataModalArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ImageClass modelCourses1 = dataSnapshot1.getValue(ImageClass.class);
                    dataModalArrayList.add(modelCourses1);
                    dataRVAdapter = new AdapterCard(dataModalArrayList, MainActivity.this);
                    courseRV.setAdapter(dataRVAdapter);
                  //  Toast.makeText(MainActivity.this,"donw2",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadrecyclerViewData2() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("topics2");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList2.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ImageClass modelCourses1 = dataSnapshot1.getValue(ImageClass.class);
                    arrayList2.add(modelCourses1);
                    sliderimage.add(modelCourses1);
                    dataRVAdapter2 = new AdapterCard(arrayList2, MainActivity.this);
                    recyclerView2.setAdapter(dataRVAdapter2);
                    viewPager2.setAdapter(new ViewPagerAdapter(sliderimage,viewPager2));
                    //Toast.makeText(MainActivity.this,"donw1",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }
    private void loadimg2(String cat,int p,int limit) {
        arraylist3.clear();
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
                            JSONObject js2=js.getJSONObject("urls");
                            String url =js2.getString("regular");
                            Log.d("TAG","message"+url);
                            ImageClass headline = new ImageClass(url);
                            arraylist3.add(0,headline);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter3 = new ImgAdapter(getApplicationContext(), arraylist3);
                    recyclerView3.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    recyclerView3.setAdapter(adapter3);

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {

            }
        });
    }
}
