package com.publicaciones.publicaciones.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "RATING_SUMMARY")
public class RatingSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = "AVERAGE")
    private Double average;

    @Column(name = "COUNT")
    private Long count;

    public RatingSummary() {
    }

    // Constructor requerido por la expresión JPQL: new RatingSummary(avg(...),
    // count(...))
    public RatingSummary(Double average, Long count) {
        this.average = average;
        this.count = count;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}