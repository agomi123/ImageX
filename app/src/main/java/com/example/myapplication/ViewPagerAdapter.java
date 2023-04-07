package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.SliderViewHolder> {
    private List<ImageClass> imageClasses;
    private ViewPager2 viewPager2;

    public ViewPagerAdapter(List<ImageClass> imageClasses, ViewPager2 viewPager2) {
        this.imageClasses = imageClasses;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(imageClasses.get(position));
        if(position==imageClasses.size()-2)viewPager2.post(runnable);
    }

    @Override
    public int getItemCount() {
        return imageClasses.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView roundedImageView;

        public SliderViewHolder(@NonNull View view) {
            super(view);
            roundedImageView = view.findViewById(R.id.item);
        }

        void setImage(ImageClass image) {
            Picasso.get().load(image.getImgUrl()).into(roundedImageView);
        }
    }
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            imageClasses.addAll(imageClasses);
            notifyDataSetChanged();
        }
    };
}

