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
    const [type,setType] = useState(null);
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
         <div className="host-new-rental-bg">
            <div className="new-rental-header">Add new rental</div>
            <button className = "button submit-new-rental"  id="submit" type="button" onClick={() => onSubmit()}>
                Submit new rental
            </button>
            <a href="https://localhost:3000/host/hostHome">
                <button className = "button submit-new-rental">
                    Homepage
                </button>
            </a>
            <div className="host-details-box">
                <div className="details-section1">
                    <h2>Basic : </h2>
                    <p>
                        <label>
                            Title 
                            <input
                                id="new title"
                                name="new title"
                                type="text"
                                placeholder="new title"
                                onChange={(event) => {
                                    if(event.target.value === "") setTitle(null);
                                    else setTitle(event.target.value);
                                }}
                                value={title}
                            />
                        </label>
                    </p>
                    <p>
                        <label>
                            Base price 
                            <input
                                id="newBasePrice"
                                name="newBasePrice"
                                type="number"
                                placeholder="newBasePrice"
                                onChange={(event) => {
                                    if(event.target.value === "") setBasePrice(null);
                                    else setBasePrice(event.target.value);
                                }}
                                value={basePrice}
                            />
                        </label>
                    </p>
                    <p>
                        <label>
                            Charge per person 
                            <input
                                id="NewChargePerPerson"
                                name="NewChargePerPerson"
                                type="number"
                                placeholder="NewChargePerPerson"
                                onChange={(event) => {
                                    if(event.target.value === "") setChargePerPerson(null);
                                    else setChargePerPerson(event.target.value);
                                }}
                                value={chargePerPerson}
                            />
                        </label>
                    </p>
                    <p>
                        <label>
                            Max guests 
                            <input
                                id="NewMaxGuests"
                                name="NewMaxGuests"
                                type="number"
                                placeholder="NewMaxGuests"
                                onChange={(event) => {
                                    if(event.target.value === "") setMaxGuests(null);
                                    else setMaxGuests(event.target.value);
                                }}
                                value={maxGuests}
                            />
                        </label>
                    </p>
                    <p>Available dates 
                        <MultipleDatePicker
                            value={availableDates}
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
                            minDate={new Date()}
                        />
                    </p>
                </div>

                <div className="details-section1">
                    <h2>Space :</h2>
                    <p>
                        <label>
                            Beds 
                            <input
                                id="NewBeds"
                                name="NewBeds"
                                type="number"
                                placeholder="NewBeds"
                                onChange={(event) => {
                                    if(event.target.value === "") setBeds(null);
                                    else setBeds(event.target.value);
                                }}
                                value={beds}
                            />
                        </label>
                    </p>
                    <p>
                        <label>
                            Bedrooms:
                            <input
                                id="Bedrooms"
                                name="Bedrooms"
                                type="number"
                                placeholder="Bedrooms"
                                onChange={(event) => {
                                    if(event.target.value === "") setBedrooms(null);
                                    else setBedrooms(event.target.value);
                                }}
                                value={bedrooms}
                            />
                        </label>
                    </p>
                    <p>
                        <label>
                            Bathrooms:
                            <input
                                id="Bathrooms"
                                name="Bathrooms"
                                type="number"
                                placeholder="Bathrooms"
                                onChange={(event) => {
                                    if(event.target.value === "") setBathrooms(null);
                                    else setBathrooms(event.target.value);
                                }}
                                value={bathrooms}
                            />
                        </label>
                    </p>
                    <p>
                        <label for="type" >
                        Type 
                        </label>
                        <select name="type" id="type" value={type} onChange={(event) => setType(event.target.value)}>
                            <option value = {null}></option>
                            <option value="privateRoom">privateRoom</option>
                            <option value="publicRoom">Public Room</option>
                            <option value="house">House</option>
                        </select>
                    </p>
                    <p>
                        <label for="hasLivingRoom" >
                        Has Living Room  
                        </label>
						<input
                                id="hasLivingRoom"
                                name="hasLivingRoom"
                                type="checkbox"
                                placeholder="hasLivingRoom"
                                onChange={(event) => {handleCheckbox(hasLivingRoom, setHasLivingRoom)}}
                                value={surfaceArea}
                            />
                    </p>
                    <p>
                        <label>
                            Surface area:
                            <input
                                id="SurfaceArea"
                                name="SurfaceArea"
                                type="number"
                                placeholder="SurfaceArea"
                                onChange={(event) => {
                                    if(event.target.value === "") setSurfaceArea(null);
                                    else setSurfaceArea(event.target.value);
                                }}
                                value={surfaceArea}
                            />
                        </label>
                    </p>
                </div>

                <div className="details-section2">
                    <h2>Description : </h2>
                    <p>
                        <label>
                            Description:
                            <input
                                id="Description"
                                name="Description"
                                type="text"
                                placeholder="Description"
                                onChange={(event) => {
                                    setDescription(event.target.value);
                                }}
                                value={description}
                            />
                        </label>
                    </p>
                </div>

                <div className="details-section2">
                    <h2>Rules :</h2>
                    <p>
                        <label for="allowSmoking" >
                        Allow smoking 
                        </label>
						<input
                                id="AllowSmoking"
                                name="AllowSmoking"
                                type="checkbox"
                                placeholder="AllowSmoking"
                                onChange={(event) => {handleCheckbox(allowSmoking, setAllowSmoking)}}
                                value={surfaceArea}
                            />
                    </p>
                    <p>
                        <label for="allowPets" >
                        Allow pets 
                        </label>
						<input
                                id="AllowPets"
                                name="AllowPets"
                                type="checkbox"
                                placeholder="AllowPets"
                                onChange={(event) => {handleCheckbox(allowPets, setAllowPets)}}
                                value={surfaceArea}
                            />
                    </p>
                    <p>
                        <label for="allowEvents" >
                        Allow events 
                        </label>
						<input
                                id="AllowEvents"
                                name="AllowEvents"
                                type="checkbox"
                                placeholder="AllowEvents"
                                onChange={(event) => {handleCheckbox(allowEvents, setAllowEvents)}}
                                value={surfaceArea}
                            />
                    </p>
                    <p>
                        <label>
                            Minimum days per booking 
                            <input
                                id="MinDays"
                                name="MinDays"
                                type="number"
                                placeholder="MinDays"
                                onChange={(event) => {
                                    if(event.target.value === "") setMinDays(null);
                                    else setMinDays(event.target.value);
                                }}
                                value={minDays}
                            />
                        </label>
                    </p>
                </div>

                <div className="details-section3">
                    <h2>Location :</h2>
                        <p>
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
                        </p>
                        <p>
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
                        </p>
                        <p>
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
                        </p>
                        <p>
                            <label>
                                Street Number:
                                <input
                                    id="StreetNumber"
                                    name="StreetNumber"
                                    type="number"
                                    placeholder="StreetNumber"
                                    onChange={(event) => {
                                        if(event.target.value === "") setStreetNumber(null);
                                        else setStreetNumber(event.target.value);
                                    }}
                                    value={streetNumber}
                                />
                            </label>
                        </p>
                        <p>
                            <label>
                                Floor No:
                                <input
                                    id="FloorNo"
                                    name="FloorNo"
                                    type="number"
                                    placeholder="FloorNo"
                                    onChange={(event) => {
                                        if(event.target.value === "") setFloorNo(null);
                                        else setFloorNo(event.target.value);
                                    }}
                                    value={floorNo}
                                />
                            </label>
                        </p>
                </div>
                
                <div className="details-section3">
                    <h2>Map :</h2>
                    <p>
						<label>
							Bus stop nearby
						</label>
						<input
							id="bus"
							name="bus"
							type="checkbox"
							placeholder="bus"
							onChange={(event) => {handleCheckbox(bus, setBus)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Train stop nearby
						</label>
						<input
							id="train"
							name="train"
							type="checkbox"
							placeholder="train"
							onChange={(event) => {handleCheckbox(train, setTrain)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Tram stop nearby
						</label>
						<input
							id="tram"
							name="tram"
							type="checkbox"
							placeholder="tram"
							onChange={(event) => {handleCheckbox(tram, setTram)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Subway stop nearby
						</label>
						<input
							id="subway"
							name="subway"
							type="checkbox"
							placeholder="subway"
							onChange={(event) => {handleCheckbox(subway, setSubway)}}
							value={surfaceArea}
						/>
					</p>
                </div>

                <div className="details-section4">
                    <h2>Photos </h2>
                </div>

                <div className="details-section4">
                    <h2>Amenities :</h2>
					<p>
						<label>
							Has WiFi
						</label>
						<input
							id=" hasWiFi"
							name=" hasWiFi"
							type="checkbox"
							placeholder=" hasWiFi"
							onChange={(event) => {handleCheckbox( hasWiFi, setHasWiFi)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Has AC
						</label>
						<input
							id=" hasAC"
							name=" hasAC"
							type="checkbox"
							placeholder=" hasAC"
							onChange={(event) => {handleCheckbox( hasAC, setHasAC)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Has Heating
						</label>
						<input
							id=" hasHeating"
							name=" hasHeating"
							type="checkbox"
							placeholder=" hasHeating"
							onChange={(event) => {handleCheckbox( hasHeating, setHasHeating)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Has Kitchen
						</label>
						<input
							id=" hasKitchen"
							name=" hasKitchen"
							type="checkbox"
							placeholder=" hasKitchen"
							onChange={(event) => {handleCheckbox( hasKitchen, setHasKitchen)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Has TV
						</label>
						<input
							id=" hasTV"
							name=" hasTV"
							type="checkbox"
							placeholder=" hasTV"
							onChange={(event) => {handleCheckbox( hasTV, setHasTV)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Has Parking
						</label>
						<input
							id=" hasParking"
							name=" hasParking"
							type="checkbox"
							placeholder=" hasParking"
							onChange={(event) => {handleCheckbox( hasParking, setHasParking)}}
							value={surfaceArea}
						/>
					</p>
					<p>
						<label>
							Has Elevator
						</label>
						<input
							id=" hasElevator"
							name=" hasElevator"
							type="checkbox"
							placeholder=" hasElevator"
							onChange={(event) => {handleCheckbox( hasElevator, setHasElevator)}}
							value={surfaceArea}
						/>
					</p>
                </div>
                    
            </div>
        </div>
		</>
	);
}

export default NewRental;