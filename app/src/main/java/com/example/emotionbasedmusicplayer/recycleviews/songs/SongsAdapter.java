package com.example.emotionbasedmusicplayer.recycleviews.songs;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionbasedmusicplayer.Model.AudioModel;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongRecycleItem> {

    private List<AudioModel> mPer;
    private List<String> mKey;
    private AppCompatActivity mContext;
    private SongRecycleItem.OnSongSelected onSongSelected;

    public SongsAdapter(List<AudioModel> mPer, List<String> mKey, AppCompatActivity mContext) {
        this.mPer = mPer;
        this.mKey = mKey;
        this.mContext = mContext;
    }

    public SongsAdapter(List<AudioModel> mPer, List<String> mKey, SongRecycleItem.OnSongSelected onSongSelected) {
        this.mPer = mPer;
        this.mKey = mKey;
        this.onSongSelected = onSongSelected;
    }

    @NonNull
    @Override
    public SongRecycleItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext != null) {
            return new SongRecycleItem(parent, mContext);
        }
        return new SongRecycleItem(parent, onSongSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull SongRecycleItem holder, int position) {
        holder.bind(mPer.get(position), mKey.get(position));
    }

    @Override
    public int getItemCount() {
        return mPer.size();
    }
}
