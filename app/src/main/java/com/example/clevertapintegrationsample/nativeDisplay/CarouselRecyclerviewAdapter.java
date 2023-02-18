package com.example.clevertapintegrationsample.nativeDisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clevertapintegrationsample.R;

import java.util.ArrayList;

public class CarouselRecyclerviewAdapter extends RecyclerView.Adapter {

    private final ArrayList<CarouselModel> dataSet;
    Context mContext;

    public CarouselRecyclerviewAdapter(ArrayList<CarouselModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_layout, parent, false);
        return new ImageTypeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        CarouselModel object = dataSet.get(listPosition);
        if (object != null) {
            Glide.with(mContext).load(object.imageUrl).into(((ImageTypeViewHolder) holder).image);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.carouselImage);
        }
    }
}