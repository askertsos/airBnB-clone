// Register.js
import React, { useState, onSubmit } from "react";
import { useNavigate } from "react-router-dom";

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
			<div>
				<label>
					Username : 
                </label>
                <input
                    id="username"
                    name="username"
                    type="text"
                    placeholder="username"
                    onChange={(event) => setUsername(event.target.value)}
                    value={username}
                    required
                />
			</div>
			<div>
				<label>
					Password : 
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
			<div>
				<label>
					Confirm password : 
					<input
						id="confirmPassword"
						name="confirmPassword"
						type="password"
						placeholder="confirmPassword"
						onChange={(event) => setConfirmPassword(event.target.value)}
						value={confirmPassword}
					/>
				</label>
			</div>
            <div>
				<label>
					First Name : 
                </label>
                <input
                    id="firstName"
                    name="firstName"
                    type="text"
                    placeholder="firstName"
                    onChange={(event) => setFirstName(event.target.value)}
                    value={firstName}
                />
			</div>
            <div>
				<label>
					Last Name : 
					<input
						id="lastName"
						name="lastName"
						type="text"
						placeholder="lastName"
						onChange={(event) => setLastName(event.target.value)}
						value={lastName}
					/>
				</label>
			</div>
            <div>
				<label>
					E-mail : 
					<input
						id="email"
						name="email"
						type="email"
						placeholder="email"
						onChange={(event) => setEmail(event.target.value)}
						value={email}
					/>
				</label>
			</div>
            <div>
				<label>
					Phone Number : 
					<input
						id="phoneNumber"
						name="phoneNumber"
						type="text"
						placeholder="phoneNumber"
						onChange={(event) => setPhoneNumber(event.target.value)}
						value={phoneNumber}
					/>
				</label>
			</div>
            <div>
				<label for="roles" >
					Roles on site : 
                </label>
                <select name="roles" id="roles" value={roles} onChange={(event) => setRoles(event.target.value)}>
                    <option></option>
                    <option value="tenant">Tenant</option>
                    <option value="host">Host</option>
                    <option value="both">Both</option>
                </select>
			</div>
            <div>
				<label>
					Profile picture:
					<input
						id="profilePicture"
						name="profilePicture"
						type="text"
						placeholder="profilePicture"
						onChange={(event) => setProfilePicture(event.target.value)}
						value={profilePicture}
					/>
				</label>
			</div>
			<button id="submit" type="button" onClick={() => onSubmit()}>
				Register
			</button>
		</>
	);
}

export default Register;