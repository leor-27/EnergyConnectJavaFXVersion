package com.example.energyconnectjavafx.Model;

public class StreamItem {
    private int id;
    private String title;
    private String date;
    private String time;
    private String audioPath;

    // constructor
    public StreamItem(int id, String title, String date, String time, String audioPath) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.audioPath = audioPath;
    }

    // getters setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
