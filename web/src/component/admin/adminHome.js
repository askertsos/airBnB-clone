// adminHome.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BaseUrl, ServerPort, ClientPort } from "../../constants.js" 

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
		fetch(BaseUrl + ServerPort + "/admin/auth", fetchOptions)
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
				<a href={BaseUrl + ClientPort + "/admin/user/list"}>
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
					<a href={BaseUrl + ClientPort + "/auth/logout"}>
						<button className="button">Logout</button>
					</a>
				</div>
			</div>
		</>
	);
}

export default AdminHome;
