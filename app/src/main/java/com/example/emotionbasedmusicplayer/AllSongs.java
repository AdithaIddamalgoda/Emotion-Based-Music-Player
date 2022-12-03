package com.example.emotionbasedmusicplayer;

import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emotionbasedmusicplayer.Model.AudioModel;
import com.example.emotionbasedmusicplayer.db.DBHelper;
import com.example.emotionbasedmusicplayer.recycleviews.RecycleViewConfig;
import com.example.emotionbasedmusicplayer.recycleviews.songs.SongsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AllSongs extends AppCompatActivity {
    private Button btBrowse;
    private TextView tvSongsPath;
    private DBHelper dbHelper;
    private AppCompatSpinner emotionSpinner;

    ActivityResultLauncher<Intent> browseDirResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    getSongs(result.getData());
                }
            }
    );
    private RecyclerView recyclerView;
    private List<AudioModel> songsList;
    private List<String> songsIdList;
    private Dialog myDialog;
    public static final String EM_HAPPY = "Happy";
    public static final String EM_SAD = "Sad";
    public static final String EM_ANGRY = "Angry";
    public static final String EM_NEUTRAL = "Neutral";
    public static final String EM_SURPRISED = "Surprised";

    private final String[] emotionList = {EM_HAPPY, EM_SAD, EM_ANGRY, EM_NEUTRAL, EM_SURPRISED};

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

        songsList = new ArrayList<>();
        songsIdList = new ArrayList<>();
        recyclerView = findViewById(R.id.rec_view_all_songs);
        myDialog = new Dialog(this);

        loadAllSongs();
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
        List<DocumentFile> audioFiles = Arrays.stream(documentFiles)
                .filter(documentFile -> {
                    if (documentFile.getType() == null) {
                        return false;
                    }
                    return documentFile.getType().contains("audio/");
                })
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
        if (!audioModelList.isEmpty()) {
            dbHelper.replaceSongsList(audioModelList);
        }
    }

    private void loadAllSongs() {

        songsList = dbHelper.getAllSongs();
        songsList.forEach(audioModel -> songsIdList.add(String.valueOf(audioModel.getId())));

        if (songsList.isEmpty()) {
            Toast.makeText(this, "No Records found", Toast.LENGTH_SHORT).show();
        } else {
            SongsAdapter songsAdapter = new SongsAdapter(songsList, songsIdList, this);
            new RecycleViewConfig().setConfig(recyclerView, this, songsAdapter);
        }
    }

    private void saveSongsToDB(List<AudioModel> audioModelList) {

    }

    public void viewSongDetails(AudioModel audioModel) {

        myDialog.setContentView(R.layout.custom_popup_view_song);
        Button btnPopupBtn = (Button) myDialog.findViewById(R.id.btnSubmit);
        TextView txtSongTitle = (TextView) myDialog.findViewById(R.id.txtSongTitle);
        EditText etAngry = myDialog.findViewById(R.id.et_angry);
        EditText etDisgust = myDialog.findViewById(R.id.et_disgust);
        EditText etFear = myDialog.findViewById(R.id.et_fear);
        EditText etHappy = myDialog.findViewById(R.id.et_happy);
        EditText etNeutral = myDialog.findViewById(R.id.et_neutral);
        EditText etSad = myDialog.findViewById(R.id.et_sad);
        EditText etSurprise = myDialog.findViewById(R.id.et_surprise);
        emotionSpinner = myDialog.findViewById(R.id.dropEmotions);

        txtSongTitle.setText(audioModel.getTitle());
        updateSpinnerData();
        btnPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etAngry.getText().toString()) &&
                        !TextUtils.isEmpty(etDisgust.getText().toString()) &&
                        !TextUtils.isEmpty(etFear.getText().toString()) &&
                        !TextUtils.isEmpty(etHappy.getText().toString()) &&
                        !TextUtils.isEmpty(etNeutral.getText().toString()) &&
                        !TextUtils.isEmpty(etSad.getText().toString()) &&
                        !TextUtils.isEmpty(etSurprise.getText().toString())) {
                    int angry = Integer.parseInt(etAngry.getText().toString());
                    int disgust = Integer.parseInt(etDisgust.getText().toString());
                    int fear = Integer.parseInt(etFear.getText().toString());
                    int happy = Integer.parseInt(etHappy.getText().toString());
                    int neutral = Integer.parseInt(etNeutral.getText().toString());
                    int sad = Integer.parseInt(etSad.getText().toString());
                    int surprise = Integer.parseInt(etSurprise.getText().toString());
                    if (angry <= 100 && disgust <= 100 && fear <= 100 && happy <= 100 &&
                            neutral <= 100 && sad <= 100 && surprise <= 100) {
                        String defaultMood = emotionSpinner.getSelectedItem().toString();
                        audioModel.setAngry(angry);
                        audioModel.setDisgust(disgust);
                        audioModel.setFear(fear);
                        audioModel.setHappy(happy);
                        audioModel.setNeutral(neutral);
                        audioModel.setSad(sad);
                        audioModel.setSurprise(surprise);
                        audioModel.setDefaultMood(defaultMood);
                        //TODO update to the DB
                        dbHelper.updateSongDetails(audioModel);
                        loadAllSongs();
                        myDialog.dismiss();
                    } else {
                        Toast.makeText(AllSongs.this, "Values must in between 0 and 100", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AllSongs.this, "Please fill All fields", Toast.LENGTH_LONG).show();
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void updateSpinnerData() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, emotionList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        emotionSpinner.setAdapter(dataAdapter);
    }
}