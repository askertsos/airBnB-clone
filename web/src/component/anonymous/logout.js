// Logout.js
import React from "react";
import { useEffect } from "react";

import "../../css/anonymous/logout.css"

function Logout() {

    useEffect(() => {
        localStorage.setItem("jwt", null);
    }, [])

	return (
		<>
			<div className="logout-bg">
				<h1>Succesfully logged out!</h1>
				<a href = 'https://localhost:3000/home'>
					<button className="button logout-home-button">Homepage</button>
				</a>
			</div>
		</>
	);
}

export default Logout;
