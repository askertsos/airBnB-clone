// hostHome.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BaseUrl, ServerPort, ClientPort } from "../../constants.js";

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
		fetch(BaseUrl + ServerPort + "/host/auth", fetchOptions)
		.then((response) => response.json())
		.then((response) => {
			let host = false;
			let tenant = false;
			console.log(response);
			if (response.isAuthenticatedHost === false) {
				navigate("/auth/login/unauthenticatedHostLogin");
				return;
			}
			response.Roles.map( (auth) => {
				if (auth.authority === "HOST") host = true;
				if (auth.authority === "TENANT") tenant = true;
			})
			setIsBoth(host && tenant);
			setLoading(false);
		})
		.catch((message) => {
            console.log(message);
            navigate("/");
        })
	}, [isBoth, navigate]);

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<div className="home-bg">
				{/* Navigation bar */}
				<div className="main-bar">
					<a href = {BaseUrl + ClientPort + "/user/profile"}>
						<button className="bar-button small-button">Profile</button>
					</a>
					<a href = {BaseUrl + ClientPort + "/auth/logout"}>
						<button className="bar-button small-button">Logout</button>
					</a>
					{isBoth === true &&
						<a href = {BaseUrl + ClientPort + "/home"}>
							<button className="bar-button big-button">
								Tenant interface
							</button>
						</a>
					}
				</div>

				<a href = {BaseUrl + ClientPort + "/host/newRental"}>
					<button className="huge-button tenant-home-btn">
						Add new rental
					</button>
				</a>
				<a href = {BaseUrl + ClientPort + "/host/rental/list"}>
					<button className="huge-button tenant-home-btn">
						List of your rentals
					</button>
				</a>
			</div>
		</>
	);
}

export default HostHome;