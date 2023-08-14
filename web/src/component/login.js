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
		fetch("http://localhost:8080/auth/login", fetchOptions)
			.then((response) => {
				if (response.status === 200) {
					const auth = response.headers.get("authorization");
					localStorage.setItem("jwt", auth);
					navigate("/home")
				} else return Promise.reject("Login attempt failed");
			})
			.catch((message) => {
				alert(message);
			});
	};

	return (
		// <form onSubmit={onSubmit}>
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
						type="text"
						placeholder="password"
						onChange={(event) => setPassword(event.target.value)}
						value={password}
					/>
				</label>
			</div>
			<button id="submit" type="button" onClick={() => onSubmit()}>
				Login
			</button>
			{/*  </form> */}
		</>
	);
};
export default LoginPost;
