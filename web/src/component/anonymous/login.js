import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import "../../css//anonymous/login.css"

const LoginPost = () => {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");

	const navigate = useNavigate();

	const onSubmit = (e) => {
		const reqBody = {
			username: username,
			password: password,
		};
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
			},
			method: "post",
			body: JSON.stringify(reqBody),
		};
		fetch("https://localhost:8080/auth/login", fetchOptions)
		.then((response) => response.json())
		.then(response => {
			if( response.isAdmin === "true" ){
				localStorage.setItem("jwt", response.jwt);
				navigate("/admin/home");
				return;
			}
			else if( response.isHost === "true" && response.isTenant === "true"){
				localStorage.setItem("jwt", response.jwt);
				navigate("/host/bothHome");
				return;
			}
			else if( response.isHost === "true" && response.isAuthenticatedHost === "true" ){
				localStorage.setItem("jwt", response.jwt);
				navigate("/host/hostHome");
				return;
			}
			else{
				localStorage.setItem("jwt", response.jwt);
				navigate("/home")
				return;
			}

		})
		.catch((message) => {
			alert("Invalid username and password compination. Try again.");
		});
	};

	return (
		<>
			<div className="login-bg">
				<div className="box-form">
						<h2 className="login-h2"> Username: </h2>
						<input
							className="login-input"
							id="username"
							name="username"
							type="text"
							placeholder="username"
							onChange={(event) => setUsername(event.target.value)}
							value={username}
						/>
						<h2 className="login-h2"> Password: </h2>
						<input
							className="login-input"
							id="password"
							name="password"
							type="password"
							placeholder="password"
							onChange={(event) => setPassword(event.target.value)}
							value={password}
						/>
						<button className="login-btn btn1" id="submit" type="button" onClick={() => onSubmit()}>
							Login
						</button>
				</div>
				<a href = "https://localhost:3000/home">
					<button className="login-btn btn2">
						Homepage
					</button>
				</a>
			</div>
		</>
	);
};
export default LoginPost;
