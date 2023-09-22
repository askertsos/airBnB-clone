// Register.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import "../../css/anonymous/register.css"

function Register() {

	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [roles, setRoles] = useState("");
    const [profilePicture, setProfilePicture] = useState("");

    const navigate = useNavigate();

	const onSubmit = (e) => {

        //Validate required fields have values
        if ( username === "" ){
            alert("'Username' is required to continue!");
            return;
        }
        if ( password === "" ){
            alert("'Password' is required to continue!");
            return;
        }
        if ( confirmPassword === "" ){
            alert("'Confirm password' is required to continue!");
            return;
        }
        if ( firstName === "" ){
            alert("'First name' is required to continue!");
            return;
        }
        if ( lastName === "" ){
            alert("'Last name' is required to continue!");
            return;
        }
        if ( email === "" ){
            alert("'E-mail' is required to continue!");
            return;
        }
        if ( phoneNumber === "" ){
            alert("'Phone number' is required to continue!");
            return;
        }
        if ( roles === "" ){
            alert("'Roles on site' is required to continue!");
            return;
        }

        //Validate password and confirm password are the same
        if(password !== confirmPassword){
            alert("'Password' and 'Confirm Password' must be identical!");
            return;
        }

		const reqBody = {
			username: username,
			password: password,
            first_name : firstName,
            last_name : lastName,
            email : email,
            phoneNumber : phoneNumber,
            roles : roles,
            profilePicture : profilePicture
		};
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
			},
			method: "post",
			body: JSON.stringify(reqBody),
		};
		fetch("https://localhost:8080/auth/register", fetchOptions)
			.then((response) => {
				if (response.status === 200) {
					if (roles === "host"){
						navigate("/auth/register/hostComplete")
						return;
					}
					else if ( roles === "both"){
						navigate("/auth/register/bothComplete")
						return;
					}
					else{
						navigate("/auth/register/tenantComplete")
						return;
					}
				} else if (response.status === 409) {
                    alert("Username already taken. Try a different one.");
                    return;
                } else return Promise.reject("Register attempt failed");
			})
			.catch((message) => {
				alert(message);
			});
	};

	return (
		<>
			<div className="register-bg">
				<div className="register-box">
					<div className="register-h2 username-header">
						<label>
							Username 
						</label>
						<input
							className="register-input username-input"
							id="username"
							name="username"
							type="text"
							placeholder="username"
							onChange={(event) => setUsername(event.target.value)}
							value={username}
							required
						/>
					</div>
					<div className="register-h2 password-header">
						<label>
							Password 
							<input
								className="register-input password-input"
								id="password"
								name="password"
								type="password"
								placeholder="password"
								onChange={(event) => setPassword(event.target.value)}
								value={password}
							/>
						</label>
					</div>
					<div className="register-h2 confirm-password-header">
						<label>
							Confirm password 
							<input
								className="register-input register-confirm-password-input"
								id="confirmPassword"
								name="confirmPassword"
								type="password"
								placeholder="confirmPassword"
								onChange={(event) => setConfirmPassword(event.target.value)}
								value={confirmPassword}
							/>
						</label>
					</div>
					<div className="register-h2 fname-header">
						<label>
							First Name 
						</label>
						<input
							className="register-input fname-input"
							id="firstName"
							name="firstName"
							type="text"
							placeholder="firstName"
							onChange={(event) => setFirstName(event.target.value)}
							value={firstName}
						/>
					</div>
					<div className="register-h2 lname-header">
						<label>
							Last Name  
							<input
								className="register-input lname-input"
								id="lastName"
								name="lastName"
								type="text"
								placeholder="lastName"
								onChange={(event) => setLastName(event.target.value)}
								value={lastName}
							/>
						</label>
					</div>
					<div className="register-h2 email-header">
						<label>
							E-mail 
							<input
								className="register-input email-input"
								id="email"
								name="email"
								type="email"
								placeholder="email"
								onChange={(event) => setEmail(event.target.value)}
								value={email}
							/>
						</label>
					</div>
					<div className="register-h2 pnum-header">
						<label>
							Phone Number 
							<input
								className="register-input pnum-input"
								id="phoneNumber"
								name="phoneNumber"
								type="text"
								placeholder="phoneNumber"
								onChange={(event) => setPhoneNumber(event.target.value)}
								value={phoneNumber}
							/>
						</label>
					</div>
					<div className="register-h2 roles-header">
						<label for="roles" >
							Roles on site 
						</label>
						<select className = "register-input roles-input" name="roles" id="roles" value={roles} onChange={(event) => setRoles(event.target.value)}>
							<option></option>
							<option value="tenant">Tenant</option>
							<option value="host">Host</option>
							<option value="both">Both</option>
						</select>
					</div>
					<div className="register-h2 photo-header">
						<label>
							Profile picture 
							<input
								className="photo-input"
								id="profilePicture"
								name="profilePicture"
								type="file"
								placeholder="profilePicture"
								onChange={(event) => setProfilePicture(event.target.value)}
								value={profilePicture}
							/>
						</label>
					</div>
					<button className="register-btn submit-register" id="submit" type="button" onClick={() => onSubmit()}>
						Register
					</button>
					<div>
						<a href = "https://localhost:3000/home" >
							<button className = "register-btn register-home">
								Homepage
							</button>
						</a>
					</div>
				</div>
			</div>
		</>
	);
}

export default Register;