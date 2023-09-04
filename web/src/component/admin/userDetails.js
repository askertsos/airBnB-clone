// userDetails.js
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function UserDetails() {

    const routeParams = useParams();
    const navigate = useNavigate();
    const [loading ,setLoading] = useState(true);
    const [user, setUser] = useState([]);

	useEffect(() => {
        const reqBody = {
            "id" : routeParams.id
        };
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "post",
            body: JSON.stringify(reqBody)
		};
		fetch("https://localhost:8080/admin/user/details", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setUser(response.Users);
            setLoading(false);
        })
        .catch((message) => {
            console.log(message);
            navigate("/unauthorized/user");
            
        });
	}, [routeParams, navigate]);

    if(loading){
        return(<h1>Loading...</h1>)
    }
    

    return (
        <>
            <h1>User Details :</h1>
            <div>
                <ul>
                <li key={user.id}>
                    <p>Id : {user.id}</p>
                    <p>First name : {user.first_name}</p>
                    <p>Last name : {user.last_name}</p>
                    <p>E-mail : {user.email}</p>
                    <p>Phone Number : {user.phoneNumber}</p>
                    {user.authorities.map( (roles) => <p>Role : {roles.authority}</p>)}
                    <p>Authenticated host : {String(user.isAuthenticatedHost)}</p>
                </li>
                </ul>
            </div>
            <a href="https://localhost:3000/admin/user/list">User List</a>
        </>
    )

}

export default UserDetails;