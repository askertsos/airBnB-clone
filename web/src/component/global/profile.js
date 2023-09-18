// profile.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Profile() {

	const [passwordConfirm, setPasswordConfirm] = useState(null);
	const [password, setPassword] = useState(null);
	const [username, setUsername] = useState(null);
	const [firstname, setFirstname] = useState(null);
	const [lastname, setLastname] = useState(null);
	const [email, setEmail] = useState(null);
	const [phoneNumber, setPhoneNumber] = useState(null);

	const [isTenant, setIsTenant] = useState(false);
	const [isHost, setIsHost] = useState(false);
	const [isBoth, setIsBoth] = useState(false);
	const [user, setUser] = useState();
	const [loading, setLoading] = useState(true);
	const navigate = useNavigate();

	useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "get"
		};
		fetch("https://localhost:8080/user/profile", fetchOptions)
		.then((response) => response.json())
		.then((response) => {
			setUser(response.User);
			response.User.authorities.map( (auth) => {
				if (auth.authority === "HOST") setIsHost(true);
				if (auth.authority === "TENANT") setIsTenant(true);
			})
			setIsBoth(isHost && isTenant);
			setLoading(false);
		})
        .catch((message) => {
            console.log(message);
            navigate("/unauthorized/user");
            return;
        });
	}, [isHost, isTenant, isBoth, navigate]);

	const updateUser = () => {

		if (password !== null && password !== passwordConfirm) {
			alert("New password and Confirm new password must match.");
			return;
		}

		const reqBody = {
			username : username,
			first_name : firstname,
			last_name : lastname,
			email : email,
			phoneNumber : phoneNumber,
			password : password
		};
        console.log(reqBody);
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt")
			},
			method: "post",
			body: JSON.stringify(reqBody),
		};
		fetch("https://localhost:8080/user/profile/update", fetchOptions)
			.then((response) => {
				if (response.status === 200) {
					if(username !== null || password !== null){
						localStorage.setItem("jwt", null);
						navigate("/user/profile/update/complete");
					}
					else window.location.reload(false);
					return;
                }
				else {
					alert("Failed to update user.");
				}
			})
			.catch((message) => {
				alert(message);
			});
	};

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<h1>View/Change Profile info : </h1>
			<p>Username : {user.username} </p>
			<p>
				<label>
					Change username :
					<input
						id="new username"
						name="new username"
						type="text"
						placeholder="new username"
						onChange={(event) => {
							if(event.target.value === "") setUsername(null);
							else setUsername(event.target.value);
						}}
						value={username}
					/>
				</label>
			</p>
			<p>First name : {user.first_name} </p>
			<p>
				<label>
					Change first name :
					<input
						id="new firstname"
						name="new firstname"
						type="text"
						placeholder="new firstname"
						onChange={(event) => {
							if(event.target.value === "") setFirstname(null);
							else setFirstname(event.target.value);
						}}
						value={firstname}
					/>
				</label>
			</p>
			<p>Last name : {user.last_name} </p>
			<p>
				<label>
					Change last name :
					<input
						id="new lastname"
						name="new lastname"
						type="text"
						placeholder="new lastname"
						onChange={(event) => {
							if(event.target.value === "") setLastname(null);
							else setLastname(event.target.value);
						}}
						value={lastname}
					/>
				</label>
			</p>
			<p>E-mail : {user.email} </p>
			<p>
				<label>
					Change e-mail :
					<input
						id="new email"
						name="new email"
						type="text"
						placeholder="new email"
						onChange={(event) => {
							if(event.target.value === "") setEmail(null);
							else setEmail(event.target.value);
						}}
						value={email}
					/>
				</label>
			</p>
			<p>Phone number : {user.phoneNumber} </p>
			<p>
				<label>
					Change phone number :
					<input
						id="new phone number"
						name="new phone number"
						type="text"
						placeholder="new phone number"
						onChange={(event) => {
							if(event.target.value === "") setPhoneNumber(null);
							else setPhoneNumber(event.target.value);
						}}
						value={phoneNumber}
					/>
				</label>
			</p>
			<p>
				<h2>Reset password : </h2>
				<p>
					<label>
						New password :
						<input
							id="new password"
							name="new password"
							type="password"
							placeholder="new password"
							onChange={(event) => {
								if(event.target.value === "") setPassword(null);
								else setPassword(event.target.value);
							}}
							value={password}
						/>
					</label>
				</p>
				<p>
					<label>
						Confirm new password :
						<input
							id="confirm new password"
							name="confirm new password"
							type="password"
							placeholder="confirm new password"
							onChange={(event) => {
								if(event.target.value === "") setPasswordConfirm(null);
								else setPasswordConfirm(event.target.value);
							}}
							value={passwordConfirm}
						/>
					</label>
				</p>
			</p>
			<div>
				<button id="submit" type="button" onClick={() => updateUser()}>
					Submit changes
				</button>
			</div>
			<div>
				{ isBoth === true && <a href="https://localhost:3000/host/bothHome">Home page</a> }
				{ isHost === true && isBoth === false && <a href="https://localhost:3000/host/hostHome">Home page</a> }
				{ isTenant === true && isBoth === false && <a href="https://localhost:3000/tenant/home">Home page</a> }
			</div>
		</>
	);
}

export default Profile;