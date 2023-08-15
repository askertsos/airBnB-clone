import { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./component/home";
import LoginPost from "./component/login";
import Logout from "./component/logout";
import User from "./component/user";
import UserNotLogged from "./component/UserNotLogged";
import "./App.css";

function App() {
	const [jwt, setJwt] = useState(null);
	const [username, setUsername] = useState(null);

	// localStorage.setItem("jwt", null);

	// useEffect(() => {
	// 	const reqBody = {
	// 		username: "user1",
	// 		password: "pass1",
	// 	};
	// 	fetch("http://localhost:8080/auth/login", {
	// 		headers: {
	// 			"Content-Type": "application/json",
	// 		},
	// 		method: "post",
	// 		body: JSON.stringify(reqBody),
	// 	})
	// 		.then((response) =>
	// 			Promise.all([response.json(), response.headers])
	// 		)
	// 		.then(([body, headers]) => {
	// 			setJwt(body.jwt);
	// 			setUsername(body.user.username);
	// 			console.log("Server Response Body:", body);
	// 			console.log("Server Response Headers:", headers);
	// 		});
	// }, []);

	return (
		<Router>
			<Routes>
				<Route exact path="/" element={<Home />}></Route>
				<Route exact path="/auth/login" element={<LoginPost />}></Route>
				<Route exact path="/auth/logout" element={<Logout />}></Route>
				<Route exact path="/home" element={<Home />}></Route>
				<Route exact path="/user" element={<User />}></Route>
				<Route exact path="/unauthorized/user" element={<UserNotLogged />}></Route>
			</Routes>
		</Router>
	);
}

export default App;
