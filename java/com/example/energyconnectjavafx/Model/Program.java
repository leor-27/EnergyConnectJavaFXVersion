package com.example.energyconnectjavafx.Model;

public class Program {
    private int programId;
    private String title;
    private String type;
    private String startTime;
    private String endTime;
    private String description;
    private int admin_id;
    private String hosts;
    private String days;

    // Constructor
    public Program(int programId,
                   String title,
                   String type,
                   String startTime,
                   String endTime,
                   String description,
                   int adminId,
                   String hosts,
                   String days) {

        this.programId = programId;
        this.title = title;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.admin_id = adminId;
        this.hosts = hosts;
        this.days = days;
    }

    // Getters and Setters
    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAdminId() {
        return admin_id;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
