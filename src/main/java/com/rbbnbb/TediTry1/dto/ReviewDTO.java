package com.rbbnbb.TediTry1.dto;

public class ReviewDTO {
    private String text;
    private Integer stars;

    public ReviewDTO(Long rentalId, String text, Integer stars) {
        this.text = text;
        this.stars = stars;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String toString() {
        return "Review: " + this.text + " Stars: " + this.stars;
    }
}
