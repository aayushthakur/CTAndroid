package com.example.clevertapintegrationsample.homescreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clevertapintegrationsample.R;

import java.util.List;

public class HorizontalItemsAdapter extends RecyclerView.Adapter<HorizontalItemsAdapter.holder> {
    // List that holds every item to be displayed in RecyclerView
    List<HorizontalListItemsModel> modelList;
    Context mContext;
    public View.OnClickListener onItemClickListener;


    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public HorizontalItemsAdapter(List<HorizontalListItemsModel> models, Context context) {
        this.modelList = models;
        this.mContext = context;
    }

    @NonNull
    @Override
    // This function inflated the list_item and fits it into the Recycler View Widget
    public HorizontalItemsAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, parent, false);
        return new holder(view);
    }

    @Override
    // This funciton binds the Content with the components of the Recycler View.
    public void onBindViewHolder(@NonNull HorizontalItemsAdapter.holder holder, int position) {
        holder.text.setText(modelList.get(position).imageText);
        Glide.with(mContext).load(modelList.get(position).imageURL).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    // This class holds the components of the Recycler View
    public class holder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView imageView;

        public holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.itemTitle);
            imageView = itemView.findViewById(R.id.itemImage);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);

        }
    }
}
