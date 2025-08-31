package com.publicaciones.publicaciones.models;

public class Rating {
    private int id;
    private int postId;
    private int value; // 1..5

    public Rating() {}

    public Rating(int id, int postId, int value) {
        this.id = id;
        this.postId = postId;
        this.value = value;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
}