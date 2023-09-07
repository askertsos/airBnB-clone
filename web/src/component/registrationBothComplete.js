// registrationBothComplete.js
import React from "react";

function RegisBothionBothComplete() {
	return (
		<>
			<h1>Succesfully registered!</h1>
            <div> Request to activate Host privileges is pending. A notification will be sent when an admin approves it. </div>
			<div> Log in with Tenant privileges from <a href="https://localhost:3000/auth/login"> here</a> .</div>
		</>
	);
}

export default RegisBothionBothComplete;