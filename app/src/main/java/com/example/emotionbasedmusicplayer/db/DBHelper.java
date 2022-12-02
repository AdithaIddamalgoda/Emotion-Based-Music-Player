package com.example.emotionbasedmusicplayer.db;

import static com.example.emotionbasedmusicplayer.db.DBHelper.ColumnNames.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.emotionbasedmusicplayer.Model.AudioModel;

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
}
