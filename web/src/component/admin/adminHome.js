// adminHome.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import "../../css/admin/admin.css";
import { downloadDatabaseJSON, downloadDatabaseXML } from "./downloadDatabase";

function AdminHome() {
	const [loading, setLoading] = useState(true);
	const navigate = useNavigate();

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				Authorization: "Bearer " + localStorage.getItem("jwt"),
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
			.catch((message) => {
				console.log(message);
				navigate("/");
			});
	}, [navigate]);

	if (loading === true) {
		return <h1>Loading...</h1>;
	}

	return (
		<>
			<div className="admin-bg">
				<a href="https://localhost:3000/admin/user/list">
					<button className="list-users-btn-admin">
						List all users
					</button>
				</a>
				<div>
					<button
						className="download-db-button-json"
						onClick={() => {
							downloadDatabaseJSON();
						}}
					>
						Download Database (json)
					</button>
					<button
						className="download-db-button-xml"
						onClick={() => {
							downloadDatabaseXML();
						}}
					>
						Download Database (xml)
					</button>
				</div>
				<div className="main-bar admin-bar">
					<a href="https://localhost:3000/auth/logout">
						<button className="button">Logout</button>
					</a>
				</div>
			</div>
		</>
	);
}

export default AdminHome;
