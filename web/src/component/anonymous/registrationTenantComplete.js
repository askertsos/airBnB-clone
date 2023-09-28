// registrationTenantComplete.js
import React from "react";
import { BaseUrl, ClientPort } from "../../constants.js";

import "../../css/anonymous/registerComplete.css"

function RegistrationTenantComplete() {
	return (
		<>
			<div className="register-complete-bg">
				<div className="register-complete-box">
					<div className="register-complete-h2 register-complete-header"> Succesfully registered!</div>
					<a href={BaseUrl + ClientPort + "/auth/login"}> 
						<button className="button reg-com-btn">
							Log in from here
						</button>
					</a>
				</div>
			</div>
		</>
	);
}

export default RegistrationTenantComplete;