package com.example.emotionbasedmusicplayer.recycleviews.songs;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionbasedmusicplayer.AllSongs;
import com.example.emotionbasedmusicplayer.Model.AudioModel;
import com.example.emotionbasedmusicplayer.R;

import java.io.Serializable;


public class SongRecycleItem extends RecyclerView.ViewHolder {


    private final TextView txtSongTitle;
    private final TextView txtSongType;
    private String key;
    private AudioModel audioModel;

    public SongRecycleItem(ViewGroup parent, AppCompatActivity mContext) {
        super(LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, false));

        txtSongTitle = itemView.findViewById(R.id.txtSongTitle);
        txtSongType = itemView.findViewById(R.id.txtSongType);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllSongs allSongs = (AllSongs)mContext;
                allSongs.viewSongDetails(audioModel);
            }
        });
    }

    public void bind(AudioModel audioModel, String key) {
        txtSongTitle.setText(audioModel.getTitle());
        txtSongType.setText(audioModel.getDefaultMood());
        this.audioModel = audioModel;
        this.key = key;
    }
}
