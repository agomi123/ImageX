package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<ImageClass> arrayList;

   // OnBottomReachedListener onBottomReachedListener;
    public ImgAdapter(Context context, ArrayList<ImageClass> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }
//    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
//
//        this.onBottomReachedListener = onBottomReachedListener;
//    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutInflater;
            layoutInflater = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(layoutInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageClass headline = arrayList.get(position);
        if(headline.getImgUrl()==null){
            holder.imgurl.setImageResource(R.drawable.card2);
        }
        else
        Picasso.get().load(headline.getImgUrl()).into(holder.imgurl);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgurl;;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgurl = itemView.findViewById(R.id.imglink);
        }
    }
}
