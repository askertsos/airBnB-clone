// bothHome.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

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
		.catch((message) => console.log(message));
	}, [navigate]);

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<h1> Welcome! </h1>
            <div> <a href = 'https://localhost:3000/tenant/home'>Tenant interface</a> </div>
            <div> <a href = 'https://localhost:3000/host/hostHome'>Host interface</a> </div>
			<div> <a href = 'https://localhost:3000/user/profile'>View your profile</a> </div>
			<div> <a href = 'https://localhost:3000/auth/logout'>Logout</a> </div>
		</>
	);
}

export default BothHome;