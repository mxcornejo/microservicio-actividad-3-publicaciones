package com.publicaciones.publicaciones.models;

import java.time.LocalDate;

public class Post {
    private int id;
    private String title;
    private String content;
    private LocalDate createdAt;

    public Post() {}

    public Post(int id, String title, String content, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}