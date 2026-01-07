package com.example.energyconnectjavafx.Model;

public class DJ {
    public int id;
    public String realName;
    public String stageName;
    public String imagePath;

    public DJ() {}

    public DJ(int id, String realName, String stageName, String imagePath) {
        this.id = id;
        this.realName = realName;
        this.stageName = stageName;
        this.imagePath = imagePath;
    }
}