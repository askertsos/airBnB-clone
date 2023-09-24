// registrationTenantComplete.js
import React from "react";

import "../../css/anonymous/registerComplete.css"

function RegistrationTenantComplete() {
	return (
		<>
			<div className="register-complete-bg">
				<div className="register-complete-box">
					<div className="register-complete-h2 register-complete-header"> Succesfully registered!</div>
					<a href="https://localhost:3000/auth/login">
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