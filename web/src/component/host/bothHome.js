// bothHome.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import "../../css/host/bothHome.css"

function BothHome() {

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
		.catch((message) => {
            console.log(message);
            navigate("/");
        })
	}, [navigate]);

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<div className="home-bg">

				{/* Navigation bar */}
				<div className="main-bar">
					<a href = 'https://localhost:3000/user/profile'>
						<button className="bar-button small-button">Profile</button>
					</a>
					<a href = 'https://localhost:3000/auth/logout'>
						<button className="bar-button small-button">Logout</button>
					</a>
				</div>

				<a href = 'https://localhost:3000/home'>
					<button className="huge-button tenant-home-btn">
						Tenant interface
					</button>
				</a>
				<a href = 'https://localhost:3000/host/hostHome'>
					<button className="huge-button tenant-home-btn">
						Host interface
					</button>
				</a>
			</div>
		</>
	);
}

export default BothHome;