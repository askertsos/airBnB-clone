// searchDetails.js
import React, { useEffect, useState } from "react";
import { resolvePath, useNavigate, useParams } from "react-router-dom";
import Calendar from "react-multi-date-picker";
import { BaseUrl, ServerPort, ClientPort } from "../../constants.js";

import "../../css/tenant/searchDetails.css"

function SearchDetails() {

    const routeParams = useParams();
    const navigate = useNavigate();
    const [loading ,setLoading] = useState(true);
    const [rental, setRental] = useState([]);
    const [host, setHost] = useState(null);
    const rentalId = routeParams.id;

    const [photos, setPhotos] = useState(null);
    const [photosIndex, setPhotosIndex] = useState(null);

    const dates = localStorage.getItem("search_dates");
    const peopleCount = localStorage.getItem("search_peopleCount");

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json"
			},
			method: "get"
		};
		fetch(BaseUrl + ServerPort + "/search/" + rentalId + "/details", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setPhotos(response.photos);
            setPhotosIndex(0);
            setHost(response.host);
            setRental(response);
            setLoading(false);
        })
        .catch((message) => {
            console.log(message);
            navigate("/");
        })
	}, [rentalId]);

    const Book = () => {

        if (localStorage.getItem("jwt") === null || localStorage.getItem("jwt") === "null") {
            navigate("/unauthorized/user");
            return;
        }

        let dates_list = dates.split(",");

        const reqBody = {
            startDate : dates_list[0],
            endDate : dates_list[dates_list.length - 1],
            guests : peopleCount,
            price :  (rental.chargePerPerson * parseInt(peopleCount, 10) + rental.basePrice) * dates.split(",").length
        };

        console.log(reqBody);
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
                "Authorization": "Bearer "  + localStorage.getItem("jwt")
			},
			method: "post",
            body: JSON.stringify(reqBody),
		};
		fetch(BaseUrl + ServerPort + "/rentals/" + rentalId + "/book/confirm", fetchOptions)
        .then((response) => {
            navigate("/tenant/book/complete");
            return;
        })
        .catch((message) => {
            console.log(message);
            navigate("/");
        })
    };

    const nextPhoto = () => {
        if (photosIndex < photos.length - 1) setPhotosIndex(photosIndex + 1);
    }

    const previousPhoto = () => {
        if (photosIndex > 0) setPhotosIndex(photosIndex - 1);
    }


    if(loading){
        return(<h1>Loading...</h1>)
    }

    return (
		<>
			<div className="search-details-bg">
				<div className="details-box">
					<div className="details-section1">
						<h2>Basic : </h2>
						<p>Title : {rental.title}</p>
						<p>Base price : {rental.basePrice}</p>
						<p>Charge per person : {rental.chargePerPerson}</p>
						<p>Max guests : {rental.maxGuests}</p>
						<p>
							View Available dates
							<Calendar value={rental.availableDates} />
						</p>
					</div>

					<div className="details-section1">
						<h2>Space :</h2>
						<p>Beds : {rental.beds}</p>
						<p>Bedrooms : {rental.bedrooms}</p>
						<p>Bathrooms : {rental.bathrooms}</p>
						<p>
							Type :{" "}
							{rental.type === "publicRoom" && <>Public room</>}{" "}
							{rental.type === "privateRoom" && <>Private room</>}{" "}
							{rental.type === "house" && <>House</>}
						</p>
						<p>
							Has Living Room :{" "}
							{rental.hasLivingRoom === true && <>True</>}{" "}
							{rental.hasLivingRoom === false && <>False</>}
						</p>
						<p>Surface area : {rental.surfaceArea}</p>
					</div>

					<div className="details-section2">
						<h2>Description : </h2>
						<p>{rental.description}</p>
					</div>

					<div className="details-section2">
						<h2>Rules :</h2>
						<p>
							Allow smoking :{" "}
							{rental.allowSmoking === true && <>True</>}{" "}
							{rental.allowSmoking === false && <>False</>}
						</p>
						<p>
							Allow pets :{" "}
							{rental.allowPets === true && <>True</>}{" "}
							{rental.allowPets === false && <>False</>}
						</p>
						<p>
							Allow events :{" "}
							{rental.allowEvents === true && <>True</>}{" "}
							{rental.allowEvents === false && <>False</>}
						</p>
						<p>Min days : {rental.minDays}</p>
					</div>

					<div className="details-section3">
						<h2>Location :</h2>
						<p>City : {rental.address.city}</p>
						<p>Neighbourhood : {rental.address.neighbourhood}</p>
						<p>Street : {rental.address.street}</p>
						<p>Street Number : {rental.address.number}</p>
						<p>Floor No : {rental.address.floorNo}</p>
					</div>

					<div className="details-section3">
						<h2>Map :</h2>
						<p>
							Public transport :{" "}
							{rental.publicTransport.map((item) => (
								<> {item} </>
							))}
						</p>
					</div>

					<div className="details-section4">
						<h2>Photos :</h2>
						{photos.length > 0 && (
							<>
								<button
									className="button"
									onClick={() => nextPhoto()}
								>
									Next photo
								</button>
								<button
									className="button"
									onClick={() => previousPhoto()}
								>
									Previous photo
								</button>
								<img
									className="rental-photo-details"
									src={require("../rental_photos/rental_" +
										rental.id +
										"/" +
										photos[photosIndex].name)}
									alt="rentalPic"
								/>
							</>
						)}
					</div>

					<div className="details-section4">
						<h2>Amenities :</h2>
						<p>
							Has WiFi : {rental.hasWiFi === true && <>True</>}{" "}
							{rental.hasWiFi === false && <>False</>}
						</p>
						<p>
							Has AC : {rental.hasAC === true && <>True</>}{" "}
							{rental.hasAC === false && <>False</>}
						</p>
						<p>
							Has heating :{" "}
							{rental.hasHeating === true && <>True</>}{" "}
							{rental.hasHeating === false && <>False</>}
						</p>
						<p>
							Has kitchen :{" "}
							{rental.hasKitchen === true && <>True</>}{" "}
							{rental.hasKitchen === false && <>False</>}
						</p>
						<p>
							Has TV : {rental.hasTV === true && <>True</>}{" "}
							{rental.hasTV === false && <>False</>}
						</p>
						<p>
							Has parking :{" "}
							{rental.hasParking === true && <>True</>}{" "}
							{rental.hasParking === false && <>False</>}
						</p>
						<p>
							Has elevator :{" "}
							{rental.hasElevator === true && <>True</>}{" "}
							{rental.hasElevator === false && <>False</>}
						</p>
					</div>
				</div>
				<button
					className="host-info-box"
					onClick={() => {
						navigate("/rental/" + rentalId + "/message/history");
					}}
				>
					<div className="host-info-header ">Host info </div>
					{host.profilePicture !== null && (
						<img
							className="host-info-pic"
							src={require("../profile_photos/user_" +
								host.id +
								"/" +
								host.profilePicture.name)}
							alt="profilePic"
						/>
					)}
					{host.profilePicture === null && (
						<img
							className="host-info-pic"
							src={require("../profile_photos/default.jpg")}
							alt="profilePic"
						/>
					)}
					<div className="host-info-name">
						Name : {host.first_name} {host.last_name}{" "}
					</div>
					<div className="host-info-review">
						Reviews : {rental.reviews.length}{" "}
					</div>
					{rental.rating === null && (
						<div className="host-info-stars"> Stars : - </div>
					)}
					{rental.rating !== null && (
						<div className="host-info-stars">
							{" "}
							Stars : {rental.rating}{" "}
						</div>
					)}
				</button>
				{localStorage.getItem("isRecomendation") === "false" && (
					<a href={BaseUrl + ClientPort + "/search/results"}>
						<button className="button bookRental">
							Back to rental list
						</button>
					</a>
				)}
				{localStorage.getItem("isRecomendation") === "true" && (
					<a href={BaseUrl + ClientPort + "/recomendations"}>
						<button className="button bookRental">
							Back to recomended rentals list
						</button>
					</a>
				)}
				<button className="button bookRental" onClick={() => Book()}>
					Book rental
				</button>
				<a href={BaseUrl + ClientPort + "/tenant/" + rental.id + "/review"}>
					<button className="button bookRental">Add review</button>
				</a>
			</div>
		</>
	);

}

export default SearchDetails;