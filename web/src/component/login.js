import React, { useState } from "react";

const LoginPost = () => {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [jwt, setJwt] = useState(null);

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
					// console.log("jwt is: ", auth);
					localStorage.setItem("jwt", auth);
					setJwt(auth);
					window.location.href = "../../";
				} else return Promise.reject("Login attempt failed");
			})
			// .then((codeIs200) => {
			// 	if (codeIs200) {

			// 	} else {

			// 	}
			// })
			// .then((response) => {
			// 	localStorage.setItem("test", "TEST");
			// 	// if (response.status === 200)
			// 	return Promise.all([
			// 		response.json(),
			// 		response.headers.get("Authorization"),
			// 	]);
			// 	// else return Promise.reject("Login attempt failed");
			// 	// if (response.status === "200") {
			// 	// 	localStorage.setItem(
			// 	// 		"jwt",
			// 	// 		response.headers.get("Authorization")
			// 	// 	);
			// 	// 	window.location.href = "../../home";
			// 	// } else return Promise.reject("Invalid login attempt");
			// })
			// .then((body, auth) => {
			// 	localStorage.setItem("test", "ttt");
			// 	localStorage.setItem("jwt", auth);
			// 	setJwt(auth);
			// 	window.location.href = "../../home";
			// })
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
