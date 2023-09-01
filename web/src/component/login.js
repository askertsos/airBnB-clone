import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

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
			console.log("jwt : " + response.jwt);
			console.log("AuthenticatedHost : " + response.isAuthenticatedHost);
			console.log("isHost : " + response.isHost);
			console.log("isAdmin : " + response.isAdmin);

			if( response.isHost === "true" && response.isAuthenticatedHost === "false"){
				navigate("/auth/login/unauthenticatedHostLogin");
				return;
			}
			else if( response.isAdmin === "true" ){
				localStorage.setItem("jwt", response.jwt);
				navigate("/admin/home");
				return;
			}
			else{
				localStorage.setItem("jwt", response.jwt);
				navigate("/home")
				return;
			}

		})
		.catch((message) => {
			alert(message);
		});
	};

	return (
		<>
			<div>
				<label>
					Username:
					<input
						id="username"
						name="username"
						type="text"
						placeholder="username"
						onChange={(event) => setUsername(event.target.value)}
						value={username}
					/>
				</label>
			</div>
			<div>
				<label>
					Password:
					<input
						id="password"
						name="password"
						type="password"
						placeholder="password"
						onChange={(event) => setPassword(event.target.value)}
						value={password}
					/>
				</label>
			</div>
			<button id="submit" type="button" onClick={() => onSubmit()}>
				Login
			</button>
		</>
	);
};
export default LoginPost;
