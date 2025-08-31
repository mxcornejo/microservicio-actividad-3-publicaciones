package com.publicaciones.publicaciones.models;

import java.time.LocalDate;

public class Comment {
    private int id;
    private int postId;
    private String author;
    private String text;
    private LocalDate createdAt;

    public Comment() {}

    public Comment(int id, int postId, String author, String text, LocalDate createdAt) {
        this.id = id;
        this.postId = postId;
        this.author = author;
        this.text = text;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}