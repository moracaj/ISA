package com.project.onlybuns.dto;

import org.springframework.web.multipart.MultipartFile;

public class PostDto {

    private String details;
    private String imagePath;

    private Double lat;
    private Double lon;
    private String geoTag;

    private MultipartFile uploadedImage;

    // --- Constructors ---
    public PostDto() {}

    public PostDto(String details, String imagePath, Double lat, Double lon, String geoTag, MultipartFile uploadedImage) {
        this.details = details;
        this.imagePath = imagePath;
        this.lat = lat;
        this.lon = lon;
        this.geoTag = geoTag;
        this.uploadedImage = uploadedImage;
    }

    // --- Getters and Setters ---
    public MultipartFile getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(MultipartFile uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getGeoTag() {
        return geoTag;
    }

    public void setGeoTag(String geoTag) {
        this.geoTag = geoTag;
    }
}
