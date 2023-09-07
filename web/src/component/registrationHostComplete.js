// registrationHostComplete.js
import React from "react";

function RegistrationHostComplete() {
	return (
		<>
			<h1>Succesfully registered!</h1>
            <div> Request to activate account is pending. You will be able to login from <a href="https://localhost:3000/auth/login"> here</a> when an admin approves it. </div>
		</>
	);
}

export default RegistrationHostComplete;