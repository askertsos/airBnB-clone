// review.js
import React, { useEffect, useState } from "react";

import { useNavigate, useParams } from "react-router-dom";

import "../../css/tenant/review.css"

function Review() {

    const routeParams = useParams();
    const navigate = useNavigate();
    const [reviewText, setReviewText] = useState(null);
    const [stars, setStars] = useState(null);
    const rentalId = routeParams.id;

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "get"
		};
		fetch("https://localhost:8080/rentals/auth", fetchOptions)
        .then((response) => {
            if (response.status !== 200){
                navigate("/unauthorized/user");
                return;
            }
        })
        .catch((message) => {
            console.log(message);
            navigate("/");
        })
	});

    const SubmitReview = () => {

        if (stars === null) {
            alert("Select stars to submit your review");
            return;
        }

        const reqBody = {
            text : reviewText,
            stars : stars
        };
        console.log(reqBody);
        const fetchOptions = {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer "  + localStorage.getItem("jwt"),
            },
            method: "post",
            body: JSON.stringify(reqBody),
        };
        fetch("https://localhost:8080/rentals/" + rentalId + "/review", fetchOptions)
        .then(response => {
            if (response.status === 200){
                navigate("/tenant/review/complete");
                return;
            }
            else {
                navigate("/unauthorized/user");
                return;
            }
        })
        .catch((message) => {
            console.log(message);
            navigate("/");
        })
    };

	return (
		<>
			<div className="search-details-bg">
                <div className="review-box">
                    <button className="button submit-review" onClick={() => SubmitReview()}>
                        Submit review
                    </button>
                    <a href="https://localhost:3000/home">
                        <button className="button submit-review">
                            Homepage
                        </button>
                    </a>
                    <div className="stars-header">
                        Stars
                    </div>
                    <select className="stars-input" name="stars" id="stars" value={stars} onChange={(event) => setStars(event.target.value)}>
                        <option value = {null}></option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>
                    <div className="review-header">
                        Review
                    </div>
                    <input
                        className="review-input"
                        id="reviewText"
                        name="reviewText"
                        type="text"
                        placeholder="reviewText"
                        onChange={(event) => setReviewText(event.target.value)}
                        value={reviewText}
                    />
                </div>
			</div>
		</>
	);
}

export default Review;
