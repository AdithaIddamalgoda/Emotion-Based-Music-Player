package com.example.emotionbasedmusicplayer.db;

import static com.example.emotionbasedmusicplayer.db.DBHelper.ColumnNames.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.emotionbasedmusicplayer.Model.AudioModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "songsDb.db";
    private static final String SONGS_TABLE = "all_songs";
    private static final int version = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, version);
    }

    public static class ColumnNames {
        public static final String SONG_NAME = "song_name";
        public static final String DURATION = "duration";
        public static final String PATH = "path";
        public static final String ANGRY = "angry";
        public static final String DISGUST = "disgust";
        public static final String FEAR = "fear";
        public static final String HAPPY = "happy";
        public static final String NEUTRAL = "neutral";
        public static final String SAD = "sad";
        public static final String SURPRISE = "surprise";
        public static final String ENABLED = "enabled";
        public static final String DEFAULT_MOOD = "default_mood";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + SONGS_TABLE + " (" +
                "\"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                SONG_NAME + " TEXT," +
                DURATION + " INTEGER," +
                PATH + " TEXT," +
                ANGRY + " INTEGER," +
                DISGUST + " INTEGER," +
                FEAR + " INTEGER," +
                HAPPY + " INTEGER," +
                NEUTRAL + " INTEGER," +
                SAD + " INTEGER," +
                SURPRISE + " INTEGER," +
                ENABLED + " INTEGER DEFAULT 1," +
                DEFAULT_MOOD + " TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void replaceSongsList(List<AudioModel> audioModelList) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + SONGS_TABLE);
        audioModelList.forEach(audioModel -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SONG_NAME, audioModel.getTitle());
            contentValues.put(PATH, audioModel.getPath());
            contentValues.put(DURATION, audioModel.getDuration());
            sqLiteDatabase.insert(SONGS_TABLE, null, contentValues);
        });
    }

    public List<AudioModel> getAllSongs() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(SONGS_TABLE, null, null, null, null, null, "id ASC");
        List<AudioModel> audioModelList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex(SONG_NAME);
            int durationIndex = cursor.getColumnIndex(DURATION);
            int pathIndex = cursor.getColumnIndex(PATH);
            int angryIndex = cursor.getColumnIndex(ANGRY);
            int disgustIndex = cursor.getColumnIndex(DISGUST);
            int fearIndex = cursor.getColumnIndex(FEAR);
            int happyIndex = cursor.getColumnIndex(HAPPY);
            int neutralIndex = cursor.getColumnIndex(NEUTRAL);
            int sadIndex = cursor.getColumnIndex(SAD);
            int supIndex = cursor.getColumnIndex(SURPRISE);
            int enabledIndex = cursor.getColumnIndex(ENABLED);
            int defMoodIndex = cursor.getColumnIndex(DEFAULT_MOOD);
            do {
                AudioModel audioModel = new AudioModel();
                audioModel.setId(cursor.getInt(idIndex));
                audioModel.setSongName(cursor.getString(nameIndex));
                audioModel.setDuration(cursor.getString(durationIndex));
                audioModel.setPath(cursor.getString(pathIndex));
                audioModel.setAngry(cursor.getInt(angryIndex));
                audioModel.setDisgust(cursor.getInt(disgustIndex));
                audioModel.setFear(cursor.getInt(fearIndex));
                audioModel.setHappy(cursor.getInt(happyIndex));
                audioModel.setNeutral(cursor.getInt(neutralIndex));
                audioModel.setSad(cursor.getInt(sadIndex));
                audioModel.setSurprise(cursor.getInt(supIndex));
                audioModel.setEnabled(cursor.getInt(enabledIndex));
                audioModel.setDefaultMood(cursor.getString(defMoodIndex));
                audioModelList.add(audioModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return audioModelList;
    }

    public void updateSongDetails(AudioModel audioModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANGRY, audioModel.getAngry());
        contentValues.put(DISGUST, audioModel.getDisgust());
        contentValues.put(FEAR, audioModel.getFear());
        contentValues.put(HAPPY, audioModel.getHappy());
        contentValues.put(NEUTRAL, audioModel.getNeutral());
        contentValues.put(SAD, audioModel.getSad());
        contentValues.put(SURPRISE, audioModel.getSurprise());
        contentValues.put(DEFAULT_MOOD, audioModel.getDefaultMood());
        String[] st = new String[]{String.valueOf(audioModel.getId())};
        database.update(SONGS_TABLE, contentValues, "id=" + audioModel.getId(), null);
    }

    public List<AudioModel> getSongsByDefaultMood(String defaultMood) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(SONGS_TABLE, null, DEFAULT_MOOD + "='" + defaultMood+ "'", null, null, null, "id ASC");
        List<AudioModel> audioModelList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex(SONG_NAME);
            int durationIndex = cursor.getColumnIndex(DURATION);
            int pathIndex = cursor.getColumnIndex(PATH);
            int angryIndex = cursor.getColumnIndex(ANGRY);
            int disgustIndex = cursor.getColumnIndex(DISGUST);
            int fearIndex = cursor.getColumnIndex(FEAR);
            int happyIndex = cursor.getColumnIndex(HAPPY);
            int neutralIndex = cursor.getColumnIndex(NEUTRAL);
            int sadIndex = cursor.getColumnIndex(SAD);
            int supIndex = cursor.getColumnIndex(SURPRISE);
            int enabledIndex = cursor.getColumnIndex(ENABLED);
            int defMoodIndex = cursor.getColumnIndex(DEFAULT_MOOD);
            do {
                AudioModel audioModel = new AudioModel();
                audioModel.setId(cursor.getInt(idIndex));
                audioModel.setSongName(cursor.getString(nameIndex));
                audioModel.setDuration(cursor.getString(durationIndex));
                audioModel.setPath(cursor.getString(pathIndex));
                audioModel.setAngry(cursor.getInt(angryIndex));
                audioModel.setDisgust(cursor.getInt(disgustIndex));
                audioModel.setFear(cursor.getInt(fearIndex));
                audioModel.setHappy(cursor.getInt(happyIndex));
                audioModel.setNeutral(cursor.getInt(neutralIndex));
                audioModel.setSad(cursor.getInt(sadIndex));
                audioModel.setSurprise(cursor.getInt(supIndex));
                audioModel.setEnabled(cursor.getInt(enabledIndex));
                audioModel.setDefaultMood(cursor.getString(defMoodIndex));
                audioModelList.add(audioModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return audioModelList;
    }
}
