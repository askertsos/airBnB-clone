// Home.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import MulitDatePicker, { getAllDatesInRange }  from "react-multi-date-picker";

import { BaseUrl, ServerPort, ClientPort } from "../../constants";

import "../../css//anonymous/home.css"

function Home() {

	const [country, setCountry] = useState(null);
	const [city, setCity] = useState(null);
	const [neighbourhood, setNeighbourhood] = useState(null);
	const [peopleCount, setPeopleCount] = useState(null);
	const [dates, setDates] = useState(null);
	const [recommendedRentals, setRecommendedRentals] = useState(null);

	const loggedIn = localStorage.getItem("jwt");

	const [isBoth, setIsBoth] = useState(false);
	const navigate = useNavigate();

	useEffect(() => {

		localStorage.setItem("search_dates", null);
		localStorage.setItem("search_country", null);
		localStorage.setItem("search_city", null);
		localStorage.setItem("search_neighbourhood", null);
		localStorage.setItem("search_peopleCount", null);

		if( loggedIn !== "null"){
			const fetchOptions = {
				headers: {
					"Content-Type": "application/json",
					"Authorization": "Bearer "  + loggedIn,
				},
				method: "get",
			};
			fetch(BaseUrl + ServerPort + "/user/auth", fetchOptions)
			.then((response) => response.json())
			.then((response) => {
				let host = false;
				let tenant = false;
				response.map( (auth) => {
					if (auth.authority === "HOST") host = true;
					if (auth.authority === "TENANT") tenant = true;
				})
				setIsBoth(host && tenant);
			})
			.catch((message) => console.log(message));
		}
	}, [isBoth, loggedIn]);

	const Search = () => {

		if (dates === null){
			alert("Select dates to proceed!");
			return;
		}

		if (peopleCount === null){
			alert("Select visitors amount to proceed!");
			return;
		}
		else if (peopleCount <= 0){
			alert("Selected visitors must be at least 1");
			return;
		}

		let reformated_dates = ""
		dates.map((date) => {
			reformated_dates = reformated_dates + date + ",";
		})
		reformated_dates = reformated_dates.slice(0,-1);

		localStorage.setItem("search_dates", reformated_dates);
		localStorage.setItem("search_country", country);
		localStorage.setItem("search_city", city);
		localStorage.setItem("search_neighbourhood", neighbourhood);
		localStorage.setItem("search_peopleCount", peopleCount);

		navigate("/search/results");
		localStorage.setItem("isRecomendation", false);
		return;
	};

	const Recommend = () => {
		navigate("/recomendations");
		localStorage.setItem("isRecomendation", true);
		return;
	};

	return (
		<>
			<div className="home-bg">

				{/* Navigation bar */}
				{loggedIn === "null" &&
					<div className="main-bar">
						<a href = {BaseUrl + ClientPort + "/auth/login"}> 
							<button className="bar-button small-button"> Login</button>
						</a>
						<a href = {BaseUrl + ClientPort + "/auth/register"}>
							<button className="bar-button small-button"> Register</button>
						</a>
					</div>
				}
				{loggedIn !== "null" &&
					<div className="main-bar">
						<a href = {BaseUrl + ClientPort + "/user/profile"}>
							<button className="bar-button small-button">Profile</button>
						</a>
						<a href = {BaseUrl + ClientPort + "/auth/logout"}>
							<button className="bar-button small-button">Logout</button>
						</a>
						<button className="bar-button" onClick = {() => Recommend()}>
							Recomended rentals for you!
						</button>
						{isBoth === true &&
							<a href = {BaseUrl + ClientPort + "/host/hostHome"}> 
								<button className="bar-button big-button">Host homepage</button>
							</a>
						}
					</div>
				}

				{/* Search bar */}
				<div className="search-bar">
					<div className="search-header-background main-background">
						<h2 className="search-h2 search-main-header"> Where are you going </h2>
					</div>
					<h2 className="search-h2 country-header"> Country: </h2>
					<input
						className="search-input country-input"
						id="country"
						name="country"
						type="text"
						placeholder="country"
						onChange={(event) => {if(event.target.value !== "") setCountry(event.target.value); else setCountry(null)}}
						value={country}
					/>
					<h2 className="search-h2 city-header"> City: </h2>
					<input
						className="search-input city-input"
						id="city"
						name="city"
						type="text"
						placeholder="city"
						onChange={(event) => {if(event.target.value !== "") setCity(event.target.value); else setCity(null)}}
						value={city}
					/>
					<h2 className="search-h2 neighbourhood-header"> Neighbourhood: </h2>
					<input
						className="search-input neighbourhood-input"
						id="neighbourhood"
						name="neighbourhood"
						type="text"
						placeholder="neighbourhood"
						onChange={(event) => {if(event.target.value !== "") setNeighbourhood(event.target.value); else setNeighbourhood(null)}}
						value={neighbourhood}
					/>
					<h2 className="search-h2 peopleCount-header"> How many visitors: </h2>
					<input
						className="search-input peopleCount-input"
						id="peopleCount"
						name="peopleCount"
						type="number"
						placeholder="peopleCount"
						onChange={(event) => {if(event.target.value !== "") setPeopleCount(event.target.value); else setPeopleCount(null)}}
						value={peopleCount}
					/>
					<div className="search-header-background dates-background" />
					<h2 className="search-h2 dates-header"> When are you going: </h2>
					<div className="dates-input">
						<MulitDatePicker
							onChange={dates => {
								const tempDates = [];
								getAllDatesInRange(dates).forEach((date) => {
									let month = date.month;	
									let day = date.day;
									if (parseInt(date.day) < 10) day = "0" + date.day;
									if (parseInt(date.month) < 10) month = "0" + date.month;
									tempDates.push(date.year + "-" + month + "-" + day);
								})
								setDates(tempDates);
							}}
							range
							rangeHover
							minDate={new Date()}
						/>
					</div>
					<button className="bar-button search-button small-button" id="submit" type="button" onClick={() => Search()}>
							Explore!
					</button>
				</div>
				
			</div>
		</>
	);
}

export default Home;
