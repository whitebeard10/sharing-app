package com.example.vit_share;

public class Request {
    private int id;
    private String title;
    private String description;
    private int userId;
    private int acceptedBy;

    public Request(int id, String title, String description, int userId, int acceptedBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.acceptedBy = acceptedBy;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(int acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    @Override
    public String toString() {
        return title + ": " + description;
    }
}
