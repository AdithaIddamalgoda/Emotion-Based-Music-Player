package com.example.emotionbasedmusicplayer.Model;

import java.io.Serializable;

public class AudioModel implements Serializable {
    private String path;
    private String songName;
    private String duration;
    private int angry;
    private int disgust;
    private int fear;
    private int happy;
    private int neutral;
    private int sad;
    private int surprise;
    private int enabled;
    private String defaultMood;

    public AudioModel() {
    }

    public AudioModel(String path, String title, String duration) {
        this.path = path;
        this.songName = title;
        this.duration = duration;
    }

    public AudioModel(String path, String songName, String duration, int angry, int disgust, int fear, int happy, int neutral, int sad, int surprise, int enabled, String defaultMood) {
        this.path = path;
        this.songName = songName;
        this.duration = duration;
        this.angry = angry;
        this.disgust = disgust;
        this.fear = fear;
        this.happy = happy;
        this.neutral = neutral;
        this.sad = sad;
        this.surprise = surprise;
        this.enabled = enabled;
        this.defaultMood = defaultMood;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return songName;
    }

    public void setTitle(String title) {
        this.songName = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getAngry() {
        return angry;
    }

    public void setAngry(int angry) {
        this.angry = angry;
    }

    public int getDisgust() {
        return disgust;
    }

    public void setDisgust(int disgust) {
        this.disgust = disgust;
    }

    public int getFear() {
        return fear;
    }

    public void setFear(int fear) {
        this.fear = fear;
    }

    public int getHappy() {
        return happy;
    }

    public void setHappy(int happy) {
        this.happy = happy;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }

    public int getSad() {
        return sad;
    }

    public void setSad(int sad) {
        this.sad = sad;
    }

    public int getSurprise() {
        return surprise;
    }

    public void setSurprise(int surprise) {
        this.surprise = surprise;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getDefaultMood() {
        return defaultMood;
    }

    public void setDefaultMood(String defaultMood) {
        this.defaultMood = defaultMood;
    }
}
