package com.example.clevertapintegrationsample.appinbox;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clevertapintegrationsample.R;

import java.util.ArrayList;

public class AppInboxRecyclerviewAdapter extends RecyclerView.Adapter {

    Context mContext;
    int total_types =2;
    private final ArrayList<AppInboxModel> dataSet;
//    MediaPlayer mPlayer;
//    private boolean fabStateVolume = false;

    public AppInboxRecyclerviewAdapter(ArrayList<AppInboxModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
//        total_types = dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case AppInboxModel.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_text_layout, parent, false);
                return new TextTypeViewHolder(view);
            case AppInboxModel.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_text_image_layout, parent, false);
                return new ImageTypeViewHolder(view);
            /*case AppInboxModel.AUDIO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_type, parent, false);
                return new AudioTypeViewHolder(view);*/
        }
        return null;
    }

    /*public static class AudioTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        FloatingActionButton fab;

        public AudioTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.fab = (FloatingActionButton) itemView.findViewById(R.id.fab);
        }
    }*/

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).getType()) {
            case 0:
                return AppInboxModel.TEXT_TYPE;
            case 1:
                return AppInboxModel.IMAGE_TYPE;
            /*case 2:
                return AppInboxModel.AUDIO_TYPE;*/
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        AppInboxModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.getType()) {
                case AppInboxModel.TEXT_TYPE:
                    ((TextTypeViewHolder) holder).txtTitle.setText(object.title);
                    ((TextTypeViewHolder) holder).txtMessage.setText(object.message);
                    ((TextTypeViewHolder) holder).txtDate.setText(DateUtils.getRelativeTimeSpanString(object.receivedDate*1000,System.currentTimeMillis(),
                            DateUtils.MINUTE_IN_MILLIS));

                    break;
                case AppInboxModel.IMAGE_TYPE:
                    ((ImageTypeViewHolder) holder).txtTitle.setText(object.title);
                    ((ImageTypeViewHolder) holder).txtMessage.setText(object.message);
                    ((ImageTypeViewHolder) holder).txtDate.setText(DateUtils.getRelativeTimeSpanString(object.receivedDate*1000,System.currentTimeMillis(),
                            DateUtils.MINUTE_IN_MILLIS));
                    Glide.with(mContext).load(object.imageUrl).into(((ImageTypeViewHolder) holder).image);
                    break;
                /*case AppInboxModel.AUDIO_TYPE:

                    ((AudioTypeViewHolder) holder).txtType.setText(object.text);

                    ((AudioTypeViewHolder) holder).fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (fabStateVolume) {
                                if (mPlayer.isPlaying()) {
                                    mPlayer.stop();

                                }
                                ((AudioTypeViewHolder) holder).fab.setImageResource(R.drawable.volume);
                                fabStateVolume = false;

                            } else {
                                mPlayer = MediaPlayer.create(mContext, R.raw.sound);
                                mPlayer.setLooping(true);
                                mPlayer.start();
                                ((AudioTypeViewHolder) holder).fab.setImageResource(R.drawable.mute);
                                fabStateVolume = true;

                            }
                        }
                    });
                    break;*/
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle,txtMessage, txtDate;

        public TextTypeViewHolder(View itemView) {
            super(itemView);

            this.txtTitle = itemView.findViewById(R.id.inboxTitle);
            this.txtMessage = itemView.findViewById(R.id.inboxMessage);
            this.txtDate = itemView.findViewById(R.id.receivedDate);
        }
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle,txtMessage,txtDate;
        ImageView image;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtTitle = itemView.findViewById(R.id.inboxTitle);
            this.txtMessage = itemView.findViewById(R.id.inboxMessage);
            this.txtDate = itemView.findViewById(R.id.receivedDate);
            this.image = itemView.findViewById(R.id.inboxImage);
        }
    }
}