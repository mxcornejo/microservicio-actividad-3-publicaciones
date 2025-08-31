package com.publicaciones.publicaciones.models;

public class RatingSummary {
    private int postId;
    private double average;
    private int count;

    public RatingSummary() {}
    public RatingSummary(int postId, double average, int count) {
        this.postId = postId; this.average = average; this.count = count;
    }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public double getAverage() { return average; }
    public void setAverage(double average) { this.average = average; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
