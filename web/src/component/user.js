// User.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function User() {
	const [loading, setLoading] = useState(true);
	const jwt = localStorage.getItem("jwt");
	const navigate = useNavigate();

	console.log("JWT is ", jwt);

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "get",
		};
		fetch("http://localhost:8080/user/auth", fetchOptions)
			.then((response) => {
				console.log(response.status);
				console.log(response.headers);
				console.log(response);
				if (response.status !== 200) {
					navigate("/unauthorized/user");
				}
				setLoading(false);
			})
			.catch((message) => console.log(message));
	});

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<h1>User</h1>
			<div>Jwt is {localStorage.getItem("jwt")}</div>
		</>
	);
}

export default User;
