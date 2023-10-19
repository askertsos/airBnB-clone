// SearchResults.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BaseUrl, ServerPort, ClientPort } from "../../constants.js";

import "../../css/tenant/searchResults.css"

function SearchResults() {

    const navigate = useNavigate();
	const [loading, setLoading] = useState(true);
    const [rentals, setRentals] = useState([]);
    const [maxPage, setMaxPage] = useState();
    const [pageNum, setPageNum] = useState(0);

    const [type, setType] = useState(null);
    const [maxCost, setMaxCost] = useState(null);
    const [hasWiFi,setHasWiFi] = useState(null);
    const [hasAC,setHasAC] = useState(null);
    const [hasHeating,setHasHeating] = useState(null);
    const [hasKitchen,setHasKitchen] = useState(null);
    const [hasTV,setHasTV] = useState(null);
    const [hasParking,setHasParking] = useState(null);
    const [hasElevator,setHasElevator] = useState(null);
    const [newSearch,setNewSearch] = useState(true);

    const dates = localStorage.getItem("search_dates");
    const country = localStorage.getItem("search_country");
    const city = localStorage.getItem("search_city");
    const neighbourhood = localStorage.getItem("search_neighbourhood");
    const peopleCount = localStorage.getItem("search_peopleCount");

	useEffect(() => {
        if (newSearch === true){
            if (dates === null || peopleCount === null) navigate("/home");

            let specList = [];

            if (country !== null && country !== "null")
                specList.push(
                    {
                        value: country,
                        operation: "JOIN",
                        joinTable : "address",
                        column : "country"
                    }
                );

            if (city !== null && city !== "null")
                specList.push(
                    {
                        value: city,
                        operation: "JOIN",
                        joinTable : "address",
                        column : "city"
                    }
                );

            if (neighbourhood !== null && neighbourhood !== "null")
                specList.push(
                    {
                        value: neighbourhood,
                        operation: "JOIN",
                        joinTable : "address",
                        column : "neighbourhood"
                    }
                );

            if (hasWiFi !== null)
                specList.push(
                    {
                        value: hasWiFi,
                        operation: "AMENITIES",
                        column : "hasWiFi"
                    }
                );

            if (hasAC !== null)
                specList.push(
                    {
                        value: hasAC,
                        operation: "AMENITIES",
                        column : "hasAC"
                    }
                );

            if (hasElevator !== null)
                specList.push(
                    {
                        value: hasElevator,
                        operation: "AMENITIES",
                        column : "hasElevator"
                    }
                );

            if (hasHeating !== null)
                specList.push(
                    {
                        value: hasHeating,
                        operation: "AMENITIES",
                        column : "hasHeating"
                    }
                );

            if (hasTV !== null)
                specList.push(
                    {
                        value: hasTV,
                        operation: "AMENITIES",
                        column : "hasTV"
                    }
                );

            if (hasParking !== null)
                specList.push(
                    {
                        value: hasParking,
                        operation: "AMENITIES",
                        column : "hasParking"
                    }
                );

            if (hasKitchen !== null)
                specList.push(
                    {
                        value: hasKitchen,
                        operation: "AMENITIES",
                        column : "hasKitchen"
                    }
                );

            specList.push(
                {
                    value: peopleCount,
                    operation: "GREATER_OR_EQUAL",
                    column : "maxGuests"
                }
            );

            let numDays = dates.split(",").length;
            specList.push(
                {
                    value: numDays,
                    operation: "LESS_OR_EQUAL",
                    column : "minDays"
                }
            );

            specList.push(
                {
                    value: dates,
                    operation : "DATES"
                }
            );

            const reqBody = {
                specificationList: specList,
                globalOperator : "AND",
                pageRequestDTO : {
                    pageNo : pageNum,
                    pageSize : 10,
                    sort : "ASC",
                    sortByColumn : "basePrice"
                }
            };

            const fetchOptions = {
                headers: {
                    "Content-Type": "application/json"
                },
                method: "post",
                body: JSON.stringify(reqBody),
            };
			if (
				localStorage.getItem("jwt") !== "null" &&
				localStorage.getItem("jwt") !== null
			)
			fetchOptions.headers.Authorization = "Bearer " + localStorage.getItem("jwt");
			fetch(BaseUrl + ServerPort + "/search/", fetchOptions)
			.then(response => response.json())
			.then((response) => {
				let tempList = [];
				response.content.map((rental) => {
					let passesMaxCostCheck = true;
					let passesTypeCheck = true;
					if (
						maxCost !== null &&
						(rental.chargePerPerson *
							parseInt(peopleCount, 10) +
							rental.basePrice) *
							dates.split(",").length >
							maxCost
					)
						passesMaxCostCheck = false;
					if (type !== null && rental.type !== type)
						passesTypeCheck = false;
					if (passesMaxCostCheck && passesTypeCheck)
						tempList.push(rental);
				});

				setMaxPage(response.totalPages);
				setRentals(tempList);
				setLoading(false);
				setNewSearch(false);
			})
			.catch((message) => {
				console.log(message);
				navigate("/");
			});
        }
	}, [pageNum, hasAC, hasWiFi, hasElevator, hasHeating, hasKitchen, hasParking, hasTV, type, maxCost, newSearch]);

    const nextPage = () => {
        if (pageNum + 1 < maxPage) setPageNum(pageNum + 1);
		setNewSearch(true);
    };

    const previousPage = () => {
        if (pageNum > 0) setPageNum(pageNum - 1);
		setNewSearch(true);
    };

    const firstPage = () => {
        setPageNum(0);
		setNewSearch(true);
    };

    const lastPage = () => {
        setPageNum(maxPage - 1);
		setNewSearch(true);
    };

    const handleCheckbox = (boolVar, setBoolVar) => {
        if (boolVar === null) setBoolVar(true);
		else setBoolVar(null);
	}

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<div className="searchList-bg">
				<h2>Current page : {pageNum + 1}</h2>
				<button
					className="button"
					id="submit"
					type="button"
					onClick={() => nextPage()}
				>
					Next Page
				</button>
				<button
					className="button"
					id="submit"
					type="button"
					onClick={() => previousPage()}
				>
					Previous Page
				</button>
				{pageNum > 0 && (
					<>
						<button
							className="button"
							id="submit"
							type="button"
							onClick={() => firstPage()}
						>
							First Page
						</button>
					</>
				)}
				{pageNum < maxPage - 1 && (
					<>
						<button
							className="button"
							id="submit"
							type="button"
							onClick={() => lastPage()}
						>
							Last Page
						</button>
					</>
				)}

				<a href={BaseUrl + ClientPort + "/home"}>
					<button className="button" id="submit" type="button">
						HomePage
					</button>
				</a>

				<div className="filter-bar">
					<h2>Add more filters</h2>
					<div>
						<label htmlFor="type">Type</label>
						<select
							name="type"
							id="type"
							value={type}
							onChange={(event) => {
								if (event.target.value !== "Any type")
									setType(event.target.value);
								else setType(null);
							}}
						>
							<option value={null}>Any type</option>
							<option value="privateRoom">Private Room</option>
							<option value="publicRoom">Public Room</option>
							<option value="house">House</option>
						</select>
					</div>
					<div>
						<label htmlFor="maxCost">Maximum cost</label>
						<input
							id="maxCost"
							name="maxCost"
							type="number"
							placeholder="maxCost"
							onChange={(event) => {
								if (event.target.value !== "")
									setMaxCost(event.target.value);
								else setMaxCost(null);
							}}
							value={maxCost}
						/>
					</div>
					<div>
						<label>
							WiFi:
							<input
								id="hasWiFi"
								name="hasWiFi"
								type="checkbox"
								placeholder="hasWiFi"
								onChange={(event) =>
									handleCheckbox(hasWiFi, setHasWiFi)
								}
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
								onChange={(event) =>
									handleCheckbox(hasAC, setHasAC)
								}
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
								onChange={(event) =>
									handleCheckbox(hasHeating, setHasHeating)
								}
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
								onChange={(event) =>
									handleCheckbox(hasKitchen, setHasKitchen)
								}
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
								onChange={(event) =>
									handleCheckbox(hasTV, setHasTV)
								}
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
								onChange={(event) =>
									handleCheckbox(hasParking, setHasParking)
								}
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
								onChange={(event) =>
									handleCheckbox(hasElevator, setHasElevator)
								}
								value={hasElevator}
							/>
						</label>
					</div>
					<button
						className="button"
						onClick={() => setNewSearch(true)}
					>
						Submit filters
					</button>
				</div>

				<div>
					<ul>
						{rentals.map((data) => (
							<p>
								<a href={BaseUrl + ClientPort + "/search/" + data.id + "/details"}>
									<button className="rental ">
										{data.photos.length === 0 && (
											<img
												className="rentalPic"
												src={require("../rental_photos/default.jpg")}
												alt="rentalPic"
											/>
										)}
										{data.photos.length > 0 && (
											<img
												className="rentalPic"
												src={require("../rental_photos/rental_" +
													data.id +
													"/" +
													data.photos[0].name)}
												alt="rentalPic"
											/>
										)}
										<p className="rental-field1">
											Title : {data.title}
										</p>
										<p className="rental-field1">
											Price :
											{(data.chargePerPerson *
												parseInt(peopleCount, 10) +
												data.basePrice) *
												dates.split(",").length}
										</p>
										<p className="rental-field1">
											Type : {data.type}
										</p>
										<p className="rental-field2">
											Number of beds : {data.beds}
										</p>
										<p className="rental-field2">
											Number of reviews :
											{data.reviews.length}
										</p>
										<p className="rental-field2">
											Rating : {data.rating}
										</p>
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

export default SearchResults;