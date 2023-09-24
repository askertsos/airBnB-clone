// registrationHostComplete.js
import React from "react";

import "../../css/anonymous/registerComplete.css"

function RegistrationHostComplete() {
	return (
		<>
			<div className="register-complete-bg">
				<div className="register-complete-box">
					<div className="register-complete-h2 register-complete-header">Succesfully registered!</div>
					<div className="host-text"> Request to activate account is pending. You will be able to login from <a href="https://localhost:3000/auth/login"> here</a> when an admin approves it. </div>
				</div>
			</div>
		</>
	);
}

export default RegistrationHostComplete;