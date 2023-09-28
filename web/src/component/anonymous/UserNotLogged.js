// UserNotLogged.js
import React from "react";
import { BaseUrl, ClientPort } from "../../constants.js"

function UserNotLogged() {
	return (
		<>
			<div className="register-complete-bg">
				<div className="register-complete-box">
					<div className="register-complete-h2 register-unauthorized-header">Unauthorised</div>
					<div className="host-text"> Login to access requested view from <a href={BaseUrl + ClientPort + "/auth/login"}> here </a> </div>
				</div>
			</div>
		</>
	);
}

export default UserNotLogged;
