// hostHome.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function HostHome() {

	const [isBoth, setIsBoth] = useState(false);
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
		.then((response) => response.json())
		.then((response) => {
			let host = false;
			let tenant = false;
			response.map( (auth) => {
				if (auth.authority === "HOST") host = true;
				if (auth.authority === "TENANT") tenant = true;
			})
			setIsBoth(host && tenant);
			setLoading(false);
		})
		.catch((message) => console.log(message));
	}, [isBoth, navigate]);

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<h1> Welcome to the host interface! </h1>
			<div> <a href = 'https://localhost:3000/host/newRental'>Add new rental</a> </div>
			<div> <a href = 'https://localhost:3000/host/rental/list'>List of your rentals</a> </div>
			<div> <a href = 'https://localhost:3000/user/profile'>View your profile</a> </div>
			{isBoth === true && <div> <a href = 'https://localhost:3000/home'>Tenant interface</a> </div>}
			<div> <a href = 'https://localhost:3000/auth/logout'>Logout</a> </div>

		</>
	);
}

export default HostHome;