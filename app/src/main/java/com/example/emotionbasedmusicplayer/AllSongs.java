package com.example.emotionbasedmusicplayer;

import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emotionbasedmusicplayer.db.DBHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AllSongs extends AppCompatActivity {
    private Button btBrowse;
    private TextView tvSongsPath;
    private DBHelper dbHelper;

    ActivityResultLauncher<Intent> browseDirResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    getSongs(result.getData());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);
        btBrowse = findViewById(R.id.bt_browse_all_songs);
        tvSongsPath = findViewById(R.id.tv_songs_path);
        dbHelper = new DBHelper(this);
        btBrowse.setOnClickListener(view -> {
            Intent browseDirIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            browseDirResultLauncher.launch(browseDirIntent);
        });

    }

    private void getSongs(Intent intent) {
        Uri uri = intent.getData();
        DocumentFile directory = DocumentFile.fromTreeUri(this, uri);
        if (directory == null || !directory.canRead()) {
            Toast.makeText(this, "Error: Couldn't read directory", Toast.LENGTH_LONG).show();
            return;
        }
        List<AudioModel> audioModelList = new ArrayList<>();
        DocumentFile[] documentFiles = directory.listFiles();
        List<DocumentFile> audioFiles = Arrays.asList(documentFiles)
                .stream()
                .filter(documentFile -> documentFile.getType().contains("audio/"))
                .collect(Collectors.toList());

        audioFiles.forEach(audioFIle -> {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                mediaMetadataRetriever.setDataSource(this, audioFIle.getUri());
            } catch (IllegalArgumentException illegalArgumentException) {
                Toast.makeText(this, "Invalid Uri Received", Toast.LENGTH_SHORT).show();
                return;
            } catch (SecurityException securityException) {
                Toast.makeText(this, "Error: No enough Permission", Toast.LENGTH_SHORT).show();
                return;
            }

            String duration = mediaMetadataRetriever.extractMetadata(METADATA_KEY_DURATION);
            AudioModel audioModel = new AudioModel(audioFIle.getUri().toString(), audioFIle.getName(), duration);
            audioModelList.add(audioModel);
        });
        if (!audioModelList.isEmpty()){
            dbHelper.replaceSongsList(audioModelList);
        }
    }

    private void saveSongsToDB(List<AudioModel> audioModelList){

    }
}