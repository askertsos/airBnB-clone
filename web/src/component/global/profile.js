// profile.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BaseUrl, ServerPort, ClientPort } from "../../constants.js";

import "../../css/profile.css"

function Profile() {

	const [passwordConfirm, setPasswordConfirm] = useState(null);
	const [password, setPassword] = useState(null);
	const [username, setUsername] = useState(null);
	const [firstname, setFirstname] = useState(null);
	const [lastname, setLastname] = useState(null);
	const [email, setEmail] = useState(null);
	const [phoneNumber, setPhoneNumber] = useState(null);
	const [profilePic, setProfilePic] = useState(null);
	const [renderedProfilePic, setRenderedProfilePic] = useState(null);

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
		fetch(BaseUrl + ServerPort + "/user/profile", fetchOptions)
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

		fetch(BaseUrl + ServerPort + "/user/profile_picture", fetchOptions)
		.then((response) => response.json())
		.then((response) => {
			console.log(response);
			setRenderedProfilePic(response.Photo);
		})
        .catch((message) => {
            console.log(message);
            navigate("/unauthorized/user");
            return;
        });
	}, [isHost, isTenant, isBoth, navigate]);

	const updateUser = () => {

		console.log(profilePic);

		if (profilePic === "default") setProfilePic(null);

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
		fetch(BaseUrl + ServerPort + "/user/profile/update", fetchOptions)
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

	const submitPhoto = () => {
		if (profilePic !== null) {
			const reqBody = new FormData();
			reqBody.append("image", profilePic[0]);
			console.log(reqBody);
			const fetchOptions = {
				headers: {
					"Authorization": "Bearer "  + localStorage.getItem("jwt")
				},
				method: "post",
				body: reqBody
			};
			console.log(fetchOptions);
			fetch(BaseUrl + ServerPort + "/user/update_profile_picture", fetchOptions)
			.then((response) => {
				if (response.status === 200) {
					window.location.reload(false);
					return;
				}
				else {
					alert("Failed to update profile picture.");
				}
			})
			.catch((message) => {
				alert(message);
			});
		}
	};

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<div className="bg">
				<div className="update_form">
					<h1 className="header header1">View/Change Profile info </h1>
					<p className="field usernameField">Username : {user.username} </p>
					<p className="field usernameInput">
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
					<p className="field firstNameField">First name : {user.first_name} </p>
					<p className="field firstNameInput">
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
					<p className="field lastNameField">Last name : {user.last_name} </p>
					<p className="field lastNameInput">
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
					<p className="field emailField">E-mail : {user.email} </p>
					<p className="field emailInput">
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
					<p className="field phoneNumberField">Phone number : {user.phoneNumber} </p>
					<p className="field phoneNumberInput">
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
						<h1 className="header header2">Reset password </h1>
						<p>
							<label className="field passwordInput">
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
						<p className="field passwordConfirmInput">
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
					<h1 className="header header3">Profile picture</h1>
					{renderedProfilePic !== null && <img className="profilePic" src={require("../profile_photos/user_" + user.id + "/" + renderedProfilePic)} alt="profilePic"/>}
					{renderedProfilePic === null && <img className="profilePic" src={require("../profile_photos/default.jpg")} alt="profilePic"/>}
					<p className="field profilePicInput">
						<label>
							Change profile picture :
							<input
								id="new profile picture"
								name="new profile picture"
								type="file"
								placeholder="new profile picture"
								onChange={(event) => {
									if(event.target.value === "") setProfilePic(null);
									else setProfilePic(event.target.files);
								}}
							/>
						</label>
					</p>
					<p>
						<button className="button submit-photo-btn" onClick={() => submitPhoto()}>
							Submit photo
						</button>
					</p>
					<div>
						<button className="button submit" id="submit" type="button" onClick={() => updateUser()}>
							Submit changes
						</button>
					</div>
					<div>
						{ isBoth === true && <a href={BaseUrl + ClientPort + "/host/bothHome"}> <button className="button home">Home page </button></a> }
						{ isHost === true && isBoth === false && <a href={BaseUrl + ClientPort + "/host/hostHome"}><button className="button home">Home page </button></a> }
						{ isTenant === true && isBoth === false && <a href={BaseUrl + ClientPort + "/home"}><button className="button home">Home page </button></a> }
					</div>
				</div>
			</div>
		</>
	);
}

export default Profile;