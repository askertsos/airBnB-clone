// Home.js
import React from "react";

function Home() {
	const loggedIn = localStorage.getItem("jwt");

	return (
		<>
			<h1> Welcome to rBBnBB! </h1>
			<div>
				{loggedIn !== "null" &&
					<a href = 'https://localhost:3000/auth/logout'>Logout</a>
				}
			</div>
			<div>
				{loggedIn === "null" &&
					<>
						<div>
							<a href = 'https://localhost:3000/auth/login'>Login </a> or <a href = 'https://localhost:3000/auth/register'>Register</a>
						</div>
					</>
				}
			</div>
		</>
	);
}

export default Home;
