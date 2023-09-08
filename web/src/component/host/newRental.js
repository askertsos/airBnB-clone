// newRental.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import  { DatePicker } from "react-datepicker";

function NewRental() {

    const [title,setTitle] = useState();
    const [basePrice,setBasePrice] = useState();
    const [chargePerPerson,setChargePerPerson] = useState();
    const [availableDates,setAvailableDates] = useState();
    const [maxGuests,setMaxGuests] = useState();
    const [beds,setBeds] = useState();
    const [bedrooms,setBedrooms] = useState();
    const [bathrooms,setBathrooms] = useState();
    const [type,seTtype] = useState();
    const [hasLivingRoom,setHasLivingRoom] = useState();
    const [surfaceArea,setSurfaceArea] = useState();
    const [description,setDescription] = useState();
    const [allowSmoking,setAllowSmoking] = useState();
    const [allowPets,setAllowPets] = useState();
    const [allowEvents,setAllowEvents] = useState();
    const [minDays,setMinDays] = useState();
    const [city,setCity] = useState();
    const [neighbourhood,setNeighbourhood] = useState();
    const [street,setStreet] = useState();
    const [streetNumber,setStreetNumber] = useState();
    const [floorNo,setFloorNo] = useState();
    const [publicTransport,setPublicTransport] = useState();
    const [hasWiFi,setHasWiFi] = useState();
    const [hasAC,setHasAC] = useState();
    const [hasHeating,setHasHeating] = useState();
    const [hasKitchen,setHasKitchen] = useState();
    const [hasTV,setHasTV] = useState();
    const [hasParking,setHasParking] = useState();
    const [hasElevator,setHasElevator] = useState();

	const [loading, setLoading] = useState(true);
	const navigate = useNavigate();

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "get",
		};
		fetch("https://localhost:8080/host/auth", fetchOptions)
		.then((response) => {
			if (response.status !== 200) {
				navigate("/unauthorized/user");
			}
			setLoading(false);
		})
		.catch((message) => console.log(message));
	}, [navigate]);


	const onSubmit = (e) => {

		const reqBody = {
            title :  title,
            basePrice :  basePrice,
            chargePerPerson :  chargePerPerson,
            availableDates :  availableDates,
            maxGuests :  maxGuests,
            beds :  beds,
            bedrooms :  bedrooms,
            bathrooms :  bathrooms,
            type :  type,
            hasLivingRoom :  hasLivingRoom,
            surfaceArea :  surfaceArea,
            description :  description,
            allowSmoking :  allowSmoking,
            allowPets :  allowPets,
            allowEvents :  allowEvents,
            minDays :  minDays,
            address :  {
                            city : city ,
                            neighbourhood : neighbourhood ,
                            street : street ,
                            streetNumber : streetNumber ,
                            floorNo : floorNo 
                       },
            publicTransport :  publicTransport,
            hasWiFi :  hasWiFi,
            hasAC :  hasAC,
            hasHeating :  hasHeating,
            hasKitchen :  hasKitchen,
            hasTV :  hasTV,
            hasParking :  hasParking,
            hasElevator :  hasElevator
		};
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
			},
			method: "post",
			body: JSON.stringify(reqBody),
		};
		fetch("https://localhost:8080/host/new", fetchOptions)
			.then((response) => {
				console.log(response);
				if (response.status === 200) {
                    console.log(response);
                }
			})
			.catch((message) => {
				alert(message);
			});
	};

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
            <h2>Basics :</h2>
			<div>
				<label>
					Title:
					<input
						id="title"
						name="title"
						type="text"
						placeholder="title"
						onChange={(event) => setTitle(event.target.value)}
						value={title}
					/>
				</label>
			</div>
            <div>
				<label>
					Base price:
					<input
						id="basePrice"
						name="basePrice"
						type="number"
						placeholder="basePrice"
						onChange={(event) => setBasePrice(event.target.value)}
						value={basePrice}
					/>
				</label>
			</div>
            <div>
				<label>
					Charge per person:
					<input
						id="chargePerPerson"
						name="chargePerPerson"
						type="number"
						placeholder="chargePerPerson"
						onChange={(event) => setChargePerPerson(event.target.value)}
						value={chargePerPerson}
					/>
				</label>
			</div>
            <div>
				<label>
					Max guests:
					<input
						id="maxGuests"
						name="maxGuests"
						type="number"
						placeholder="maxGuests"
						onChange={(event) => setMaxGuests(event.target.value)}
						value={maxGuests}
					/>
				</label>
			</div>
            <div>
				<label>
					Available dates:
					<input
						id="availableDates"
						name="availableDates"
						type="date"
						placeholder="availableDates"
						onChange={(event) => setAvailableDates(event.target.value)}
						value={availableDates}
					/>
				</label>
			</div>
            <h2>Space :</h2>
            <div>
				<label>
					Beds:
					<input
						id="beds"
						name="beds"
						type="number"
						placeholder="beds"
						onChange={(event) => setBeds(event.target.value)}
						value={beds}
					/>
				</label>
			</div>
            <div>
				<label>
					Bedrooms:
					<input
						id="bedrooms"
						name="bedrooms"
						type="number"
						placeholder="bedrooms"
						onChange={(event) => setBedrooms(event.target.value)}
						value={bedrooms}
					/>
				</label>
			</div>
			<div>
				<label>
					Bathrooms:
					<input
						id="bathrooms"
						name="bathrooms"
						type="number"
						placeholder="bathrooms"
						onChange={(event) => setBathrooms(event.target.value)}
						value={bathrooms}
					/>
				</label>
			</div>
            <div>
                <label for="type" >
                    Type 
                    </label>
                    <select name="type" id="type" value={type} onChange={(event) => seTtype(event.target.value)}>
                        <option></option>
                        <option value="oom">room</option>
                        <option value="publicRoom">Public Room</option>
                        <option value="house">House</option>
                    </select>
			</div>
            <div>
				<label>
					Living Room:
					<input
						id="hasLivingRoom"
						name="hasLivingRoom"
						type="checkbox"
						placeholder="hasLivingRoom"
						onChange={(event) => setHasLivingRoom(event.target.value)}
						value={hasLivingRoom}
					/>
				</label>
			</div>
            <div>
				<label>
					Surface area:
					<input
						id="surfaceArea"
						name="surfaceArea"
						type="number"
						placeholder="surfaceArea"
						onChange={(event) => setSurfaceArea(event.target.value)}
						value={surfaceArea}
					/>
				</label>
			</div>
            <h2>Description :</h2>
            <div>
				<label>
					Description:
					<input
						id="description"
						name="description"
						type="text"
						placeholder="description"
						onChange={(event) => setDescription(event.target.value)}
						value={description}
					/>
				</label>
			</div>
            <h2>Rules :</h2>
            <div>
				<label>
					Allow Smoking:
					<input
						id="allowSmoking"
						name="allowSmoking"
						type="checkbox"
						placeholder="allowSmoking"
						onChange={(event) => setAllowSmoking(event.target.value)}
						value={allowSmoking}
					/>
				</label>
			</div>
            <div>
				<label>
					Allow pets:
					<input
						id="allowPets"
						name="allowPets"
						type="checkbox"
						placeholder="allowPets"
						onChange={(event) => setAllowPets(event.target.value)}
						value={allowPets}
					/>
				</label>
			</div>
            <div>
				<label>
					Allow events:
					<input
						id="allowEvents"
						name="allowEvents"
						type="checkbox"
						placeholder="allowEvents"
						onChange={(event) => setAllowEvents(event.target.value)}
						value={allowEvents}
					/>
				</label>
			</div>
            <div>
				<label>
					Minimum days per booking:
					<input
						id="minDays"
						name="minDays"
						type="number"
						placeholder="minDays"
						onChange={(event) => setMinDays(event.target.value)}
						value={minDays}
					/>
				</label>
			</div>
            <h2>Location :</h2>
            <div>
				<label>
					Floor No:
					<input
						id="floorNo"
						name="floorNo"
						type="number"
						placeholder="floorNo"
						onChange={(event) => setFloorNo(event.target.value)}
						value={floorNo}
					/>
				</label>
			</div>
            <div>
				<label>
					Street Number:
					<input
						id="streetNumber"
						name="streetNumber"
						type="number"
						placeholder="streetNumber"
						onChange={(event) => setStreetNumber(event.target.value)}
						value={streetNumber}
					/>
				</label>
			</div>
            <div>
				<label>
					Street:
					<input
						id="street"
						name="street"
						type="text"
						placeholder="street"
						onChange={(event) => setStreet(event.target.value)}
						value={street}
					/>
				</label>
			</div>
            <div>
				<label>
					Neighbourhood:
					<input
						id="neighbourhood"
						name="neighbourhood"
						type="text"
						placeholder="neighbourhood"
						onChange={(event) => setNeighbourhood(event.target.value)}
						value={neighbourhood}
					/>
				</label>
			</div>
            <div>
				<label>
					City:
					<input
						id="city"
						name="city"
						type="text"
						placeholder="city"
						onChange={(event) => setCity(event.target.value)}
						value={city}
					/>
				</label>
			</div>
            <h2>Map :</h2>
            <div>
				<label>
					Public Transport:
					<input
						id="publicTransport"
						name="publicTransport"
						type="text"
						placeholder="publicTransport"
						onChange={(event) => setPublicTransport(event.target.value)}
						value={publicTransport}
					/>
				</label>
			</div>
            <h2>Amenities :</h2>
            <div>
				<label>
					WiFi:
					<input
						id="hasWiFi"
						name="hasWiFi"
						type="checkbox"
						placeholder="hasWiFi"
						onChange={(event) => setHasWiFi(event.target.value)}
						value={hasWiFi}
					/>
				</label>
			</div>
            <div>
				<label>
					AC:
					<input
						id="hasAC"
						name="hasAC"
						type="checkbox"
						placeholder="hasAC"
						onChange={(event) => setHasAC(event.target.value)}
						value={hasAC}
					/>
				</label>
			</div>
            <div>
				<label>
					Heating:
					<input
						id="hasHeating"
						name="hasHeating"
						type="checkbox"
						placeholder="hasHeating"
						onChange={(event) => setHasHeating(event.target.value)}
						value={hasHeating}
					/>
				</label>
			</div>
            <div>
				<label>
					Kitchen:
					<input
						id="hasKitchen"
						name="hasKitchen"
						type="checkbox"
						placeholder="hasKitchen"
						onChange={(event) => setHasKitchen(event.target.value)}
						value={hasKitchen}
					/>
				</label>
			</div>
            <div>
				<label>
					Television:
					<input
						id="hasTV"
						name="hasTV"
						type="checkbox"
						placeholder="hasTV"
						onChange={(event) => setHasTV(event.target.value)}
						value={hasTV}
					/>
				</label>
			</div>
            <div>
				<label>
					Parking:
					<input
						id="hasParking"
						name="hasParking"
						type="checkbox"
						placeholder="hasParking"
						onChange={(event) => setHasParking(event.target.value)}
						value={hasParking}
					/>
				</label>
			</div>
            <div>
				<label>
					Elevator:
					<input
						id="hasElevator"
						name="hasElevator"
						type="checkbox"
						placeholder="hasElevator"
						onChange={(event) => setHasElevator(event.target.value)}
						value={hasElevator}
					/>
				</label>
			</div>
			<button id="submit" type="button" onClick={() => onSubmit()}>
				Add rental
			</button>
		</>
	);
}

export default NewRental;