// Home.js
import React from "react";

function Home() {

	const loggedIn = localStorage.getItem("jwt");

	return (
		<>
			<h1> Welcome to rBBnBB! </h1>
			<div>
				{loggedIn != "null" &&
					<a href = 'http://localhost:3000/auth/logout'>Logout</a>
				}
			</div>
			<div>
				{loggedIn == "null" &&
					<a href = 'http://localhost:3000/auth/login'>Login </a>
				}
			</div>
		</>
	);
}

export default Home;
