// newRental.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import MultipleDatePicker from "react-multi-date-picker";

function NewRental() {

    const [title,setTitle] = useState(null);
    const [basePrice,setBasePrice] = useState(null);
    const [chargePerPerson,setChargePerPerson] = useState(null);
    const [availableDates,setAvailableDates] = useState(null);
    const [maxGuests,setMaxGuests] = useState(null);
    const [beds,setBeds] = useState(null);
    const [bedrooms,setBedrooms] = useState(null);
    const [bathrooms,setBathrooms] = useState(null);
    const [type,seTtype] = useState(null);
    const [hasLivingRoom,setHasLivingRoom] = useState(false);
    const [surfaceArea,setSurfaceArea] = useState(null);
    const [description,setDescription] = useState("");
    const [allowSmoking,setAllowSmoking] = useState(false);
    const [allowPets,setAllowPets] = useState(false);
    const [allowEvents,setAllowEvents] = useState(false);
    const [minDays,setMinDays] = useState(null);
    const [city,setCity] = useState(null);
    const [neighbourhood,setNeighbourhood] = useState(null);
    const [street,setStreet] = useState(null);
    const [streetNumber,setStreetNumber] = useState(null);
    const [floorNo,setFloorNo] = useState(null);
    const [hasWiFi,setHasWiFi] = useState(false);
    const [hasAC,setHasAC] = useState(false);
    const [hasHeating,setHasHeating] = useState(false);
    const [hasKitchen,setHasKitchen] = useState(false);
    const [hasTV,setHasTV] = useState(false);
    const [hasParking,setHasParking] = useState(false);
    const [hasElevator,setHasElevator] = useState(false);

	const [bus,setBus] = useState(false);
	const [train,setTrain] = useState(false);
	const [subway,setSubway] = useState(false);
	const [tram,setTram] = useState(false);

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

		if (title === null){
			alert("Title cannot be empty");
			return;
		}
		else if (basePrice === null){
			alert("Base price cannot be empty");
			return;
		}
		else if (chargePerPerson === null){
			alert("Charge per person cannot be empty");
			return;
		}
		else if (availableDates === null){
			alert("Available dates cannot be empty");
			return;
		}
		else if (maxGuests === null){
			alert("Max guests cannot be empty");
			return;
		}
		else if (beds === null){
			alert("Beds cannot be empty");
			return;
		}
		else if (bedrooms === null){
			alert("Bedrooms cannot be empty");
			return;
		}
		else if (bathrooms === null){
			alert("Bathrooms cannot be empty");
			return;
		}
		else if (type === "null"){
			alert("Type cannot be empty");
			return;
		}
		else if (surfaceArea === null){
			alert("Surface area cannot be empty");
			return;
		}
		else if (minDays === null){
			alert("Min days cannot be empty");
			return;
		}
		else if (city === null){
			alert("City cannot be empty");
			return;
		}
		else if (neighbourhood === null){
			alert("Neighbourhood cannot be empty");
			return;
		}
		else if (street === null){
			alert("Street cannot be empty");
			return;
		}
		else if (streetNumber === null){
			alert("Street number cannot be empty");
			return;
		}
		else if (floorNo === null){
			alert("Floor No cannot be empty");
			return;
		}

		var tempPublicTransport = [];
		if (bus === true) tempPublicTransport.push("bus");
		if (train === true) tempPublicTransport.push("train");
		if (tram === true) tempPublicTransport.push("tram");
		if (subway === true) tempPublicTransport.push("subway");

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
                            number : streetNumber ,
                            floorNo : floorNo 
                       },
            publicTransport :  tempPublicTransport,
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
				"Authorization": "Bearer "  + localStorage.getItem("jwt")
			},
			method: "post",
			body: JSON.stringify(reqBody),
		};
		fetch("https://localhost:8080/host/rental/new", fetchOptions)
			.then((response) => {
				if (response.status === 200) {
					setHasLivingRoom(false);
					setHasWiFi(false);
					setHasAC(false);
					setHasHeating(false);
					setHasKitchen(false);
					setHasTV(false);
					setHasParking(false);
					setHasElevator(false);
					navigate("/host/newRentalComplete");
					return;
                }
				else {
					alert("Failed to add new rental.");
				}
			})
			.catch((message) => {
				alert(message);
			});
	};

	const handleCheckbox = (boolVar, setBoolVar) => {
		if (boolVar === true) setBoolVar(false);
		else setBoolVar(true);
	}

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
						onChange={(event) => {
							if(event.target.value === "") setTitle(null);
							else setTitle(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setBasePrice(null);
							else setBasePrice(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setChargePerPerson(null);
							else setChargePerPerson(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setMaxGuests(null);
							else setMaxGuests(event.target.value);
						}}
						value={maxGuests}
					/>
				</label>
			</div>
            <div>
				Available Dates :
				<MultipleDatePicker
					onChange={dates => {
						const tempDates = [];
						dates.forEach((date) => {
							let month = date.month;
							let day = date.day;
							if (parseInt(date.day) < 10) day = "0" + date.day;
							if (parseInt(date.month) < 10) month = "0" + date.month;
							tempDates.push(date.year + "-" + month + "-" + day);
						})
						setAvailableDates(tempDates);
					}}
					multiple
					minDate={new Date()} />
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
						onChange={(event) => {
							if(event.target.value === "") setBeds(null);
							else setBeds(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setBedrooms(null);
							else setBedrooms(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setBathrooms(null);
							else setBathrooms(event.target.value);
						}}
						value={bathrooms}
					/>
				</label>
			</div>
            <div>
                <label for="type" >
                    Type 
                    </label>
                    <select name="type" id="type" value={type} onChange={(event) => seTtype(event.target.value)}>
                        <option value = "null"></option>
                        <option value="privateRoom">privateRoom</option>
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
						onChange={(event) => handleCheckbox(hasLivingRoom, setHasLivingRoom)}
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
						onChange={(event) => {
							if(event.target.value === "") setSurfaceArea(null);
							else setSurfaceArea(event.target.value);
						}}
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
						onChange={(event) => {
							setDescription(event.target.value);
						}}
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
						onChange={(event) => handleCheckbox(allowSmoking, setAllowSmoking)}
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
						onChange={(event) => handleCheckbox(allowPets, setAllowPets)}
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
						onChange={(event) => handleCheckbox(allowEvents, setAllowEvents)}
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
						onChange={(event) => {
							if(event.target.value === "") setMinDays(null);
							else setMinDays(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setFloorNo(null);
							else setFloorNo(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setStreetNumber(null);
							else setStreetNumber(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setStreet(null);
							else setStreet(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setNeighbourhood(null);
							else setNeighbourhood(event.target.value);
						}}
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
						onChange={(event) => {
							if(event.target.value === "") setCity(null);
							else setCity(event.target.value);
						}}
						value={city}
					/>
				</label>
			</div>
            <h2>Public transport :</h2>
            <div>
				<label>
					Bus:
					<input
						id="bus"
						name="bus"
						type="checkbox"
						placeholder="bus"
						onChange={(event) => handleCheckbox(bus, setBus)}
						value={bus}
					/>
				</label>
			</div>
			<div>
				<label>
					Train:
					<input
						id="train"
						name="train"
						type="checkbox"
						placeholder="train"
						onChange={(event) => handleCheckbox(train, setTrain)}
						value={train}
					/>
				</label>
			</div>
			<div>
				<label>
					Tram:
					<input
						id="tram"
						name="tram"
						type="checkbox"
						placeholder="tram"
						onChange={(event) => handleCheckbox(tram, setTram)}
						value={tram}
					/>
				</label>
			</div>
			<div>
				<label>
					Subway:
					<input
						id="subway"
						name="subway"
						type="checkbox"
						placeholder="subway"
						onChange={(event) => handleCheckbox(subway, setSubway)}
						value={subway}
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
						onChange={(event) => handleCheckbox(hasWiFi, setHasWiFi)}
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
						onChange={(event) => handleCheckbox(hasAC, setHasAC)}
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
						onChange={(event) => handleCheckbox(hasHeating, setHasHeating)}
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
						onChange={(event) => handleCheckbox(hasKitchen, setHasKitchen)}
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
						onChange={(event) => handleCheckbox(hasTV, setHasTV)}
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
						onChange={(event) => handleCheckbox(hasParking, setHasParking)}
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
						onChange={(event) => handleCheckbox(hasElevator, setHasElevator)}
						value={hasElevator}
					/>
				</label>
			</div>
			<button id="submit" type="button" onClick={() => onSubmit()}>
				Add rental
			</button>
			<div> <a href = 'https://localhost:3000/host/hostHome'>Homepage</a> </div>
		</>
	);
}

export default NewRental;