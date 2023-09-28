// registrationBothComplete.js
import React from "react";
import { BaseUrl, ClientPort } from "../../constants"

function RegisBothionBothComplete() {
	return (
		<>
			<div className="register-complete-bg">
				<div className="register-complete-box">
					<div className="register-complete-h2 register-complete-header">Succesfully registered!</div>
					<div className="host-text">
						Request to activate Host privileges is pending. A notification will be sent when an admin approves it. Log in with Tenant privileges from <a href={BaseUrl + ClientPort + "/auth/login"}> here</a> .
					</div>
				</div>
			</div>
		</>
	);
}

export default RegisBothionBothComplete;