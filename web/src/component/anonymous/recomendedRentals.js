// recomendedRentals.js
import React, { useState } from "react";

function RecomendedRentals() {

    const [recommendedRentals,setRecommendedRentals] = useState([]);

	const Recommend = () => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "get",
		};
		fetch("https://localhost:8080/user/recommended_rentals", fetchOptions)
		.then((response) => response.json())
		.then((response) => {
			console.log(response);
			setRecommendedRentals(response);
		})
		.catch((message) => console.log(message));
	};

    return(
        <>
            <div className="searchList-bg">
                 <a href = 'https://localhost:3000/home'>
                    <button className="button" id="submit" type="button">
                            HomePage
                    </button>
                </a>
                <div>
                    <ul>
                        {recommendedRentals.map((data) => (
                            <p>
                                <a href={"https://localhost:3000/search/" + data.id + "/details"}>
                                    <button className="rental ">
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