// User.js
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function User() {
	const jwt = localStorage.getItem("jwt");
	const navigate = useNavigate();

	console.log("JWT is ", jwt);

	const fetchOptions = {
		headers: {
			"Content-Type": "application/json",
			Authorization: localStorage.getItem("jwt"),
			"Access-Control-Allow-Origin": "*",
		},
		method: "get",
		body: null,
	};
	fetch("http://localhost:8080/user/auth", fetchOptions)
		.then((response) => {
			console.log(response.status);
			if (response.status !== 200) {
				navigate("/unauthorized/user");
			}
		})
		.catch((message) => console.log(message));

	// localStorage.getItem("jwt") && <Navigate to="/login" replace={true} />;;

	return (
		<>
			<h1>User</h1>
			<div>Jwt is {localStorage.getItem("jwt")}</div>
		</>
	);
}

export default User;
