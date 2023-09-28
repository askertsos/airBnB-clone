package com.WebAppTechnologies.AirBnbClone.dto;

public class MassReviewDTO {

    private Long reviewerId;

    private Long rentalId;

    private Integer stars;

    public MassReviewDTO() {
    }

    public MassReviewDTO(Long reviewerId, Long rentalId, Integer stars) {
        this.reviewerId = reviewerId;
        this.rentalId = rentalId;
        this.stars = stars;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }
}
