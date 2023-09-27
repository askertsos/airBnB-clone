// rentalDetails.js
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import MultipleDatePicker from "react-multi-date-picker";

function RentalDetails() {

    const [title,setTitle] = useState(null);
    const [basePrice,setBasePrice] = useState(null);
    const [chargePerPerson,setChargePerPerson] = useState(null);
    const [availableDates,setAvailableDates] = useState(null);
    const [maxGuests,setMaxGuests] = useState(null);
    const [beds,setBeds] = useState(null);
    const [bedrooms,setBedrooms] = useState(null);
    const [bathrooms,setBathrooms] = useState(null);
    const [type,setType] = useState(null);
    const [hasLivingRoom,setHasLivingRoom] = useState(null);
    const [surfaceArea,setSurfaceArea] = useState(null);
    const [description,setDescription] = useState(null);
    const [allowSmoking,setAllowSmoking] = useState(null);
    const [allowPets,setAllowPets] = useState(null);
    const [allowEvents,setAllowEvents] = useState(null);
    const [minDays,setMinDays] = useState(null);
    const [city,setCity] = useState(null);
    const [neighbourhood,setNeighbourhood] = useState(null);
    const [street,setStreet] = useState(null);
    const [streetNumber,setStreetNumber] = useState(null);
    const [floorNo,setFloorNo] = useState(null);
    const [hasWiFi,setHasWiFi] = useState(null);
    const [hasAC,setHasAC] = useState(null);
    const [hasHeating,setHasHeating] = useState(null);
    const [hasKitchen,setHasKitchen] = useState(null);
    const [hasTV,setHasTV] = useState(null);
    const [hasParking,setHasParking] = useState(null);
    const [hasElevator,setHasElevator] = useState(null);
    const [newPhotos,setNewPhotos] = useState([]);

    const [photos, setPhotos] = useState(null);
    const [photosIndex, setPhotosIndex] = useState(null);

	const [bus,setBus] = useState(null);
	const [train,setTrain] = useState(null);
	const [subway,setSubway] = useState(null);
	const [tram,setTram] = useState(null);

    const routeParams = useParams();
    const navigate = useNavigate();
    const [loading ,setLoading] = useState(true);
    const [rental, setRental] = useState([]);
    const [selectedDates, setSelectedDates] = useState([]);
    const rentalId = routeParams.id;

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "get"
		};
		fetch("https://localhost:8080/host/" + rentalId + "/info", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setPhotos(response.Rental.photos);
            setPhotosIndex(0);
            setRental(response.Rental);
            setSelectedDates(response.Rental.availableDates);
            setLoading(false);
        })
        .catch((message) => {
            console.log(message);
            navigate("/");
        })
	}, [rentalId,navigate]);

    const updateRental = () => {

		let tempPublicTransport = rental.publicTransport;
		if (bus === "true" && !tempPublicTransport.includes("bus")) tempPublicTransport.push("bus");
        else if (bus === "false") tempPublicTransport = tempPublicTransport.filter((item) => item !== "bus");
		if (train === "true" && !tempPublicTransport.includes("train")) tempPublicTransport.push("train");
        else if (train === "false") tempPublicTransport = tempPublicTransport.filter((item) => item !== "train");
		if (tram === "true" && !tempPublicTransport.includes("tram")) tempPublicTransport.push("tram");
        else if (tram === "false") tempPublicTransport = tempPublicTransport.filter((item) => item !== "tram");
		if (subway === "true" && !tempPublicTransport.includes("subway")) tempPublicTransport.push("subway");
        else if (subway === "false") tempPublicTransport = tempPublicTransport.filter((item) => item !== "subway");

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
        console.log(reqBody);
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt")
			},
			method: "post",
			body: JSON.stringify(reqBody),
		};
		fetch("https://localhost:8080/host/rental/" + rentalId + "/update", fetchOptions)
			.then((response) => {
				if (response.status === 200) {
                    window.location.reload(false);
					return;
                }
				else {
					alert("Failed to update rental.");
				}
			})
			.catch((message) => {
				alert(message);
			});
	};

    const submitPhotos = () => {
		if (newPhotos.length > 0 ) {
            for(let i = 0; i < newPhotos.length; i++){
                const reqBody = new FormData();
                reqBody.append("image", newPhotos[i]);
                console.log(reqBody);
                const fetchOptions = {
                    headers: {
                        "Authorization": "Bearer "  + localStorage.getItem("jwt")
                    },
                    method: "post",
                    body: reqBody
                };
                console.log(fetchOptions);
                fetch("https://localhost:8080/host/rental/" + rentalId + "/add_photo", fetchOptions)
            }
            window.location.reload(false);
		}
    };

    const deletePhoto = () => {
		if (photos.length > 0 ) {
                const fetchOptions = {
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer "  + localStorage.getItem("jwt")
                    },
                    method: "post",
                    body: JSON.stringify([photos[photosIndex].name])
                };
                console.log(fetchOptions);
                fetch("https://localhost:8080/host/rental/" + rentalId + "/remove_photos", fetchOptions)
                .then((response) => {
                    console.log(response);
                    if (response.status !== 200) {
                        alert("Failed to remove rental picture.");
                    }
                    else window.location.reload(false);
                })
                .catch((message) => {
                    alert(message);
                });
            }
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
         <div className="host-details-bg">
            <div className="details-header view-header">View rental info</div>
            <a href="https://localhost:3000/host/rental/list">
                <button className="button backToListHost">
                    Back to rental list
                </button>
            </a>
            <a href={"https://localhost:3000/host/rental/" + rentalId + "/messages"}>
                <button className="button backToListHost">
                    View messages
                </button>
            </a>
            <div className="host-details-box">
                <div className="details-section1">
                    <h2>Basic : </h2>
                    <p>Title : {rental.title}</p>
                    <p>Base price : {rental.basePrice}</p>
                    <p>Charge per person : {rental.chargePerPerson}</p>
                    <p>Max guests : {rental.maxGuests}</p>
                    <p>View Available dates 
                        <MultipleDatePicker
                            value={rental.availableDates}
                            minDate={new Date()}
                        />
                    </p>
                </div>

                <div className="details-section1">
                    <h2>Space :</h2>
                        <p>Beds : {rental.beds}</p>
                        <p>Bedrooms : {rental.bedrooms}</p>
                        <p>Bathrooms : {rental.bathrooms}</p>
                        <p>Type : {rental.type === "publicRoom" && <>Public room</>} {rental.type === "privateRoom" && <>Private room</>} {rental.type === "house" && <>House</>}</p>
                        <p>Has Living Room : {rental.hasLivingRoom === true && <>True</>} {rental.hasLivingRoom === false && <>False</>}</p>
                        <p>Surface area : {rental.surfaceArea}</p>
                </div>

                <div className="details-section2">
                    <h2>Description : </h2>
                    <p>{rental.description}</p>
                </div>

                <div className="details-section2">
                    <h2>Rules :</h2>
                        <p>Allow smoking : {rental.allowSmoking === true && <>True</>} {rental.allowSmoking === false && <>False</>}</p>
                        <p>Allow pets : {rental.allowPets === true && <>True</>} {rental.allowPets === false && <>False</>}</p>
                        <p>Allow events : {rental.allowEvents === true && <>True</>} {rental.allowEvents === false && <>False</>}</p>
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
                        <p>Public transport : {rental.publicTransport.map((item) => (<> {item} </>))}</p>
                </div>

                <div className="details-section4">
                    <h2>Photos :</h2>
                        {photos.length > 0 &&
                            <>
                                <button className="button" onClick={() => nextPhoto()}>
                                    Next photo
                                </button>
                                <button className="button" onClick={() => previousPhoto()}>
                                    Previous photo
                                </button>
                                <img className="rental-photo-details" src={require("../rental_photos/rental_" + rental.id + "/" + photos[photosIndex].name)} alt="rentalPic"/>
                                <button className="button" onClick={() => deletePhoto()}>
                                    Delete photo
                                </button>
                            </>
                        }
                </div>

                <div className="details-section4">
                    <h2>Amenities :</h2>
                        <p>Has WiFi : {rental.hasWiFi === true && <>True</>} {rental.hasWiFi === false && <>False</>}</p>
                        <p>Has AC : {rental.hasAC === true && <>True</>} {rental.hasAC === false && <>False</>}</p>
                        <p>Has heating : {rental.hasHeating === true && <>True</>} {rental.hasHeating === false && <>False</>}</p>
                        <p>Has kitchen : {rental.hasKitchen === true && <>True</>} {rental.hasKitchen === false && <>False</>}</p>
                        <p>Has TV : {rental.hasTV === true && <>True</>} {rental.hasTV === false && <>False</>}</p>
                        <p>Has parking : {rental.hasParking === true && <>True</>} {rental.hasParking === false && <>False</>}</p>
                        <p>Has elevator : {rental.hasElevator === true && <>True</>} {rental.hasElevator === false && <>False</>}</p>
                </div>
                    
            </div>
            <div className="change-header">Change rental info</div>
            <button className = "button submit-host-changes"  id="submit" type="button" onClick={() => updateRental()}>
                Submit changes
            </button>
            <div className="host-details-box2">
                <div className="details-section1">
                    <h2>Basic : </h2>
                    <p>
                        <label>
                            Change title 
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
                            Change base price 
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
                            Change charge per person 
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
                            Change max guests 
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
                    <p>Change Available dates 
                        <MultipleDatePicker
                            value={selectedDates}
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
                            Change beds 
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
                            Change bedrooms:
                            <input
                                id="NewBedrooms"
                                name="NewBedrooms"
                                type="number"
                                placeholder="NewBedrooms"
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
                            Change bathrooms:
                            <input
                                id="NewBathrooms"
                                name="NewBathrooms"
                                type="number"
                                placeholder="NewBathrooms"
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
                        Change type 
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
                        Change Has Living Room  
                        </label>
                        <select name="hasLivingRoom" id="hasLivingRoom" value={hasLivingRoom} onChange={(event) => setHasLivingRoom(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>Doesn't have</option>
                            <option value={true}>Has</option>
                        </select>
                    </p>
                    <p>
                        <label>
                            Change surface area:
                            <input
                                id="NewSurfaceArea"
                                name="NewSurfaceArea"
                                type="number"
                                placeholder="NewSurfaceArea"
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
                            Change description:
                            <input
                                id="NewDescription"
                                name="NewDescription"
                                type="text"
                                placeholder="NewDescription"
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
                        Change allow smoking 
                        </label>
                        <select name="allowSmoking" id="allowSmoking" value={allowSmoking} onChange={(event) => setAllowSmoking(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>Not Allowed</option>
                            <option value={true}>Allowed</option>
                        </select>
                    </p>
                    <p>
                        <label for="allowPets" >
                        Change allow pets 
                        </label>
                        <select name="allowPets" id="allowPets" value={allowPets} onChange={(event) => setAllowPets(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>Not Allowed</option>
                            <option value={true}>Allowed</option>
                        </select>
                    </p>
                    <p>
                        <label for="allowEvents" >
                        Change allow events 
                        </label>
                        <select name="allowEvents" id="allowEvents" value={allowEvents} onChange={(event) => setAllowEvents(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>Not Allowed</option>
                            <option value={true}>Allowed</option>
                        </select>
                    </p>
                    <p>
                        <label>
                            Change minimum days per booking 
                            <input
                                id="NewMinDays"
                                name="NewMinDays"
                                type="number"
                                placeholder="NewMinDays"
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
                                Change city:
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
                                Change neighbourhood:
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
                                Change street:
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
                                Change street Number:
                                <input
                                    id="NewStreetNumber"
                                    name="NewStreetNumber"
                                    type="number"
                                    placeholder="NewStreetNumber"
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
                                Change floor No:
                                <input
                                    id="NewFloorNo"
                                    name="NewFloorNo"
                                    type="number"
                                    placeholder="NewFloorNo"
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
                            <label for="bus" >
                            Add/Remove bus from available public transport 
                            </label>
                            <select name="bus" id="bus" onChange={(event) => setBus(event.target.value)}>
                                <option value = {null}></option>
                                <option value={false}>Remove</option>
                                <option value={true}>Add</option>
                            </select>
                        </p>
                        <p>
                            <label for="train" >
                            Add/Remove train from available public transport 
                            </label>
                            <select name="train" id="train" value={train} onChange={(event) => setTrain(event.target.value)}>
                                <option value = {null}></option>
                                <option value={false}>Remove</option>
                                <option value={true}>Add</option>
                            </select>
                        </p>
                        <p>
                            <label for="tram" >
                            Add/Remove tram from available public transport 
                            </label>
                            <select name="tram" id="tram" value={tram} onChange={(event) => setTram(event.target.value)}>
                                <option value = {null}></option>
                                <option value={false}>Remove</option>
                                <option value={true}>Add</option>
                            </select>
                        </p>
                        <p>
                            <label for="subway" >
                            Add/Remove subway from available public transport 
                            </label>
                            <select name="subway" id="subway" value={subway} onChange={(event) => setSubway(event.target.value)}>
                                <option value = {null}></option>
                                <option value={false}>Remove</option>
                                <option value={true}>Add</option>
                            </select>
                        </p>
                </div>

                <div className="details-section4">
                    <h2>Photos </h2>
                        <p>
                            <label>
                                Add rental photos : (Control + click for multiple photos)
                                <input
                                    multiple
                                    id="new profile picture"
                                    name="new profile picture"
                                    type="file"
                                    placeholder="new profile picture"
                                    onChange={(event) => {
                                        if(event.target.value === "") setNewPhotos([]);
                                        else setNewPhotos(event.target.files);
                                    }}
                                />
                            </label>
                        </p>
                        <p>
                            <button className="button" onClick={() => submitPhotos()}>
                                Submit photos
                            </button>
                        </p>
                </div>

                <div className="details-section4">
                    <h2>Amenities :</h2>
                    <p>
                        <label for="hasWifi" >
                        Change has WiFi
                        </label>
                        <select name="hasWiFi" id="hasWiFi" value={hasWiFi} onChange={(event) => setHasWiFi(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>No</option>
                            <option value={true}>Yes</option>
                        </select>
                    </p>
                    <p>
                        <label for="hasAC" >
                        Change has AC
                        </label>
                        <select name="hasAC" id="hasAC" value={hasAC} onChange={(event) => setHasAC(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>No</option>
                            <option value={true}>Yes</option>
                        </select>
                    </p>
                    <p>
                        <label for="hasHeating" >
                        Change has Heating
                        </label>
                        <select name="hasHeating" id="hasHeating" value={hasHeating} onChange={(event) => setHasHeating(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>No</option>
                            <option value={true}>Yes</option>
                        </select>
                    </p>
                    <p>
                        <label for="hasKitchen" >
                        Change has Kitchen
                        </label>
                        <select name="hasKitchen" id="hasKitchen" value={hasKitchen} onChange={(event) => setHasKitchen(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>No</option>
                            <option value={true}>Yes</option>
                        </select>
                    </p>
                    <p>
                        <label for="hasTV" >
                        Change has TV
                        </label>
                        <select name="hasTV" id="hasTV" value={hasTV} onChange={(event) => setHasTV(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>No</option>
                            <option value={true}>Yes</option>
                        </select>
                    </p>
                    <p>
                        <label for="hasParking" >
                        Change has Parking
                        </label>
                        <select name="hasParking" id="hasParking" value={hasParking} onChange={(event) => setHasParking(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>No</option>
                            <option value={true}>Yes</option>
                        </select>
                    </p>
                    <p>
                        <label for="hasElevator" >
                        Change has Elevator
                        </label>
                        <select name="hasElevator" id="hasElevator" value={hasElevator} onChange={(event) => setHasElevator(event.target.value)}>
                            <option value = {null}></option>
                            <option value={false}>No</option>
                            <option value={true}>Yes</option>
                        </select>
                    </p>
                </div>
                    
            </div>
        </div>
        </>
    )

}

export default RentalDetails;