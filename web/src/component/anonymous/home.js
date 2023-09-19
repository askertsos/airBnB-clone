// Home.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import "../../css/home.css"

function Home() {
	const loggedIn = localStorage.getItem("jwt");

	const [isBoth, setIsBoth] = useState(false);
	const [loading, setLoading] = useState(true);
	const navigate = useNavigate();

	useEffect(() => {
		if( loggedIn !== "null"){
			const fetchOptions = {
				headers: {
					"Content-Type": "application/json",
					"Authorization": "Bearer "  + loggedIn,
				},
				method: "get",
			};
			fetch("https://localhost:8080/user/auth", fetchOptions)
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
		}
		else setLoading(false);
	}, [isBoth, navigate]);

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<div className="home-bg">
				{loggedIn === "null" &&
					<div className="main-bar">
						<a href = 'https://localhost:3000/auth/login'>
							<button className="bar-button small-button"> Login</button>
						</a>
						<a href = 'https://localhost:3000/auth/register'>
							<button className="bar-button small-button"> Register</button>
						</a>
					</div>
				}
				{loggedIn !== "null" &&
					<div className="main-bar">
						<a href = 'https://localhost:3000/user/profile'>
							<button className="bar-button small-button">Profile</button>
						</a>
						<a href = 'https://localhost:3000/auth/logout'>
							<button className="bar-button small-button">Logout</button>
						</a>
						{isBoth === true &&
							<a href = 'https://localhost:3000/host/hostHome'>
								<button className="bar-button big-button">Host homepage</button>
							</a>
						}
					</div>
				}
				<h1>
					Welcome to rBBnBB!
				</h1>
			</div>
		</>
	);
}

export default Home;
