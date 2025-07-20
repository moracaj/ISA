package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mediaPath;

    @Column(columnDefinition = "TEXT")
    private String caption;

    @Column(name = "likes_count")
    private Integer likeCounter = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "post-user")
    private User creator;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "post-comments")
    private List<Comment> feedbackList = new ArrayList<>();

    @Lob
    private byte[] imageData;

    @Column(nullable = true)
    private Double lat;

    @Column(nullable = true)
    private Double lng;

    @Column(columnDefinition = "TEXT")
    private String geoLabel;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @PrePersist
    protected void markCreationTime() {
        this.creationTime = LocalDateTime.now();
    }

    // --- Getteri i setteri ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(Integer likeCounter) {
        this.likeCounter = likeCounter;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Comment> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<Comment> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getGeoLabel() {
        return geoLabel;
    }

    public void setGeoLabel(String geoLabel) {
        this.geoLabel = geoLabel;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public int getFeedbackCount() {
        return feedbackList.size();
    }
}
