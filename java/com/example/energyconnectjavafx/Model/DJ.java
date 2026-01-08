package com.example.energyconnectjavafx.Model;

public class DJ {

    private int id;
    private String realName;
    private String stageName;
    private String imagePath;

    public DJ() {
    }

    public DJ(int id, String realName, String stageName, String imagePath) {
        this.id = id;
        this.realName = realName;
        this.stageName = stageName;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
