// User.js
import React, { useEffect } from "react";
import { Navigate } from "react-router-dom";

function User() {
	console.log("in User");
	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				Authorization: localStorage.getItem("jwt"),
			},
			method: "get",
			body: null,
		};
		console.log("fetching");
		fetch("http://localhost:8080/user", fetchOptions)
			.then((response) => {
				if (response.status !== response.ok) {
					return Promise.reject(
						"You must login to access this field"
					);
				}
			})
			.catch((message) => alert(message));

		// localStorage.getItem("jwt") && <Navigate to="/login" replace={true} />;
	});
	console.log("after useEffect");
	return (
		<>
			<h1>User</h1>
			<div>Jwt is {localStorage.getItem("jwt")}</div>
		</>
	);
}

export default User;
