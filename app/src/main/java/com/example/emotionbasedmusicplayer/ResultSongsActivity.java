package com.example.emotionbasedmusicplayer;

import static com.example.emotionbasedmusicplayer.AllSongs.EM_HAPPY;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionbasedmusicplayer.Model.AudioModel;
import com.example.emotionbasedmusicplayer.db.DBHelper;
import com.example.emotionbasedmusicplayer.recycleviews.RecycleViewConfig;
import com.example.emotionbasedmusicplayer.recycleviews.songs.SongsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResultSongsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private List<AudioModel> songsList;
    private List<String> songsIdList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_songs);
        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.rec_view_result_songs);
        songsList = new ArrayList<>();
        songsIdList = new ArrayList<>();
        loadAllSongs();
    }

    private void loadAllSongs() {
        //TODO remove comment
        //String mood = getIntent().getStringExtra("mood");
        // must use moods defined in AllSongs
        String mood = EM_HAPPY;
        if (mood == null) {
            return;
        }
        songsList = dbHelper.getSongsByDefaultMood(mood);
        songsList.forEach(audioModel -> songsIdList.add(String.valueOf(audioModel.getId())));
        if (songsList.isEmpty()) {
            Toast.makeText(this, "No Records found", Toast.LENGTH_SHORT).show();
        } else {
            SongsAdapter songsAdapter = new SongsAdapter(songsList, songsIdList, this::songSelectedFromRecView);
            new RecycleViewConfig().setConfig(recyclerView, this, songsAdapter);
        }
    }

    private void songSelectedFromRecView(AudioModel audioModel) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(audioModel.getPath()), "audio/*");
        startActivity(intent);
    }
}