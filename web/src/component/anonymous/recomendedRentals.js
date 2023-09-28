// recomendedRentals.js
import React, { useState, useEffect } from "react";
import { BaseUrl, ServerPort, ClientPort } from "../../constants";

function RecomendedRentals() {

    const [recommendedRentals,setRecommendedRentals] = useState([]);

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "get",
		};
		fetch(BaseUrl + ServerPort + "/user/recommended_rentals", fetchOptions)
			.then((response) => response.json())
			.then((response) => {
				console.log(response);
				setRecommendedRentals(response);
			})
			.catch((message) => console.log(message));
	}, []);

    return(
        <>
            <div className="recomended-list-bg">
                 <a href = {BaseUrl + ClientPort + "/home"}> 
                    <button className="button recomend-home-btn" id="submit" type="button">
                            HomePage
                    </button>
                </a>
                <div>
                    <ul>
                        {recommendedRentals.map((data) => (
                            <p>
                                <a href={BaseUrl + ClientPort + "/search/" + data.id + "/details"}>
                                    <button className="recomended-rental ">
                                        {data.photos.length === 0 &&  <img className="rentalPic" src={require("../rental_photos/default.jpg")} alt="rentalPic"/>}
                                        {data.photos.length > 0 && <img className="rentalPic" src={require("../rental_photos/rental_" + data.id + "/" + data.photos[0].name)} alt="rentalPic"/>}
                                        <p className="rental-field1"> Title : {data.title} </p>
                                        <p className="rental-field1"> Charge per person : {data.chargePerPerson} </p>
                                        <p className="rental-field1"> Type : {data.type} </p>
                                        <p className="rental-field2"> Number of beds : {data.beds} </p>
                                        <p className="rental-field2"> Number of reviews : {data.reviews.length} </p>
                                        <p className="rental-field2"> Rating : {data.rating} </p>
                                    </button>
                                </a>
                            </p>
                    ))}
                    </ul>
                </div>
            </div>
        </>
    );
}

export default RecomendedRentals;