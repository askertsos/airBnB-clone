// Home.js
import React from "react";

function Home() {
	return (
		<>
			<h1>Home</h1>
			<div>Jwt is {localStorage.getItem("jwt")}</div>
		</>
	);
}

export default Home;
