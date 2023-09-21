// searchDetails.js
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Calendar from "react-multi-date-picker";

import "../../css/tenant/searchDetails.css"

function SearchDetails() {

    const routeParams = useParams();
    const navigate = useNavigate();
    const [loading ,setLoading] = useState(true);
    const [rental, setRental] = useState([]);
    const rentalId = routeParams.id;

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "get"
		};
		fetch("https://localhost:8080/search/" + rentalId + "/details", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setRental(response);
            setLoading(false);
        })
	}, [rentalId]);

    if(loading){
        return(<h1>Loading...</h1>)
    }

    return (
        <>
            <div className="search-details-bg">
                <div className="details-box">
                    <div className="details-section ">
                        <h2>Basic : </h2>
                        <p>Title : {rental.title}</p>
                        <p>Base price : {rental.basePrice}</p>
                        <p>Charge per person : {rental.chargePerPerson}</p>
                        <p>Max guests : {rental.maxGuests}</p>
                        <p>View Available dates : 
                            <Calendar
                                value={rental.availableDates}
                            />
                        </p>
                    </div>

                    <div className="details-section ">
                        <h2>Space :</h2>
                            <p>Beds : {rental.beds}</p>
                            <p>Bedrooms : {rental.bedrooms}</p>
                            <p>Bathrooms : {rental.bathrooms}</p>
                            <p>Type : {rental.type === "publicRoom" && <>Public room</>} {rental.type === "privateRoom" && <>Private room</>} {rental.type === "house" && <>House</>}</p>
                            <p>Has Living Room : {rental.hasLivingRoom === true && <>True</>} {rental.hasLivingRoom === false && <>False</>}</p>
                            <p>Surface area : {rental.surfaceArea}</p>
                    </div>

                    <h2>Description : </h2>
                    <p>{rental.description}</p>

                    <h2>Rules :</h2>
                        <p>Allow smoking : {rental.allowSmoking === true && <>True</>} {rental.allowSmoking === false && <>False</>}</p>
                        <p>Allow pets : {rental.allowPets === true && <>True</>} {rental.allowPets === false && <>False</>}</p>
                        <p>Allow events : {rental.allowEvents === true && <>True</>} {rental.allowEvents === false && <>False</>}</p>
                        <p>Min days : {rental.minDays}</p>

                    <h2>Location :</h2>
                        <p>City : {rental.address.city}</p>
                        <p>Neighbourhood : {rental.address.neighbourhood}</p>
                        <p>Street : {rental.address.street}</p>
                        <p>Street Number : {rental.address.number}</p>
                        <p>Floor No : {rental.address.floorNo}</p>
                    
                    <h2>Map :</h2>
                        <p>Public transport : {rental.publicTransport.map((item) => (<> {item} </>))}</p>

                    <h2>Photos :</h2>
                        <p>{rental.photos.map((item) => (<> {item} </>))}</p>

                    <h2>Amenities :</h2>
                        <p>Has WiFi : {rental.hasWiFi === true && <>True</>} {rental.hasWiFi === false && <>False</>}</p>
                        <p>Has AC : {rental.hasAC === true && <>True</>} {rental.hasAC === false && <>False</>}</p>
                        <p>Has heating : {rental.hasHeating === true && <>True</>} {rental.hasHeating === false && <>False</>}</p>
                        <p>Has kitchen : {rental.hasKitchen === true && <>True</>} {rental.hasKitchen === false && <>False</>}</p>
                        <p>Has TV : {rental.hasTV === true && <>True</>} {rental.hasTV === false && <>False</>}</p>
                        <p>Has parking : {rental.hasParking === true && <>True</>} {rental.hasParking === false && <>False</>}</p>
                        <p>Has elevator : {rental.hasElevator === true && <>True</>} {rental.hasElevator === false && <>False</>}</p>
                </div>
                <a href="https://localhost:3000/search/results">Other results</a>
            </div>
        </>
    )

}

export default SearchDetails;