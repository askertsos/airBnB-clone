// userDetails.js
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { BaseUrl, ServerPort, ClientPort } from "../../constants";

function UserDetails() {

    const routeParams = useParams();
    const navigate = useNavigate();
    const [loading ,setLoading] = useState(true);
    const [user, setUser] = useState([]);
    const [isHost, setIsHost] = useState();

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
		fetch(BaseUrl + ServerPort + "/admin/user/details", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setUser(response.user);
            setIsHost(response.isHost);
            setLoading(false);
        })
        .catch((message) => {
            navigate("/unauthorized/user");
            return;
        });
	}, [routeParams, navigate]);


    const activateHost = () => {
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
		fetch(BaseUrl + ServerPort + "/admin/user/activateHost", fetchOptions)
        .then((response) =>{
            if(response.status === 401){
                navigate("/unauthorized/user");
                return;
            }
            else if(response.status !== 200){
                alert("Failed to activate host.");
            }
            else{
                window.location.reload(false);
            }
        });
    }

    if(loading){
        return(<h1>Loading...</h1>)
    }
    

    return (
		<>
			<div className="searchDetails-admin-bg">
				<div className="user-details-box">
					<div className="user-details-fields">
						<p>
							<div key={user.id}>
								<p>Id : {user.id}</p>
								<p>First name : {user.first_name}</p>
								<p>Last name : {user.last_name}</p>
								<p>E-mail : {user.email}</p>
								<p>Phone Number : {user.phoneNumber}</p>
								{user.authorities.map((roles) => (
									<p>Role : {roles.authority}</p>
								))}
								{isHost === "true" && (
									<p>
										Authenticated host :{" "}
										{String(user.isAuthenticatedHost)}
									</p>
								)}
							</div>
						</p>
					</div>
				</div>
				{isHost === "true" && (
					<button
						className="button activate-user"
						onClick={() => activateHost()}
					>
						Activate Host
					</button>
				)}
				<a href={BaseUrl + ClientPort + "/admin/user/list"}>
					<button className="button backList-admin">User list</button>
				</a>
				<a href={BaseUrl + ClientPort + "/admin/home"}>
					<button className="button homepage-admin-user">
						Homepage
					</button>
				</a>
			</div>
		</>
	);

}

export default UserDetails;