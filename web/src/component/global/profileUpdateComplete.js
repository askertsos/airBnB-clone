// profileUpdateComplete.js
import React from "react";
import { BaseUrl, ClientPort } from "../../constants.js";

function ProfileUpdateComplete() {
	return (
		<>
			<h1> Succesfully updated profile! Login from <a href={BaseUrl + ClientPort + "/auth/login"}> here </a> with your updated info!</h1>
		</>
	);
}

export default ProfileUpdateComplete;
