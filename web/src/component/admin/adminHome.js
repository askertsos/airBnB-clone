// adminHome.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function AdminHome() {

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
		fetch("https://localhost:8080/admin/auth", fetchOptions)
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
			<h1> Welcome to the admin site! </h1>
			<div> See list of all users <a href = "https://localhost:3000/admin/user/list" >here</a> .</div>
			<div> <a href = 'https://localhost:3000/auth/logout'>Logout</a> </div>
		</>
	);
}

export default AdminHome;