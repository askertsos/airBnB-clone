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
			if( response.isHost === "true" && response.isAuthenticatedHost === "false"){
				navigate("/auth/login/unauthenticatedHostLogin");
				return;
			}
			else if( response.isAdmin === "true" ){
				localStorage.setItem("jwt", response.jwt);
				navigate("/admin/home");
				return;
			}
			else if( response.isHost === "true" && response.isTenant === "true" && response.isAuthenticatedHost === "true" ){
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
				navigate("/tenant/home")
				return;
			}

		})
		.catch((message) => {
			alert("Invalid username and password compination. Try again.");
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
