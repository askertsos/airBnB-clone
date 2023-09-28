// Logout.js
import React from "react";
import { useEffect } from "react";
import { BaseUrl, ClientPort } from "../../constants";

import "../../css/anonymous/logout.css"

function Logout() {

    useEffect(() => {
        localStorage.setItem("jwt", null);
    }, [])

	return (
		<>
			<div className="logout-bg">
				<div className="logout-box">
					<div className="logout-h2 logout-header">Succesfully logged out!</div>
					<a href = {BaseUrl + ClientPort + "/home"}> 
						<button className="button logout-home-button logout-home-btn">Homepage</button>
					</a>
				</div>
			</div>
		</>
	);
}

export default Logout;
