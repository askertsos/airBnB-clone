// Logout.js
import React from "react";
import { useEffect } from "react";

function Logout() {

    useEffect(() => {
        localStorage.setItem("jwt", null);
    })

	return (
		<>
			<h1>Succesfully logged out!</h1>
            <a href = 'https://localhost:3000/home'>Home </a>
		</>
	);
}

export default Logout;
