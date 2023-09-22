// unauthenticatedHostLogin.js
import React from "react";

function UnauthenticatedHostLogin() {
    return(
        <>
			<div className="register-complete-bg">
                <div className="register-complete-box">
                    <div className="register-complete-h2 register-unauthorized-header">Unable to login</div>
                    <div className="host-text">Your request to activate your account is still pending. You will be able to login when an admin activates your account.</div>
                </div>
            </div>
        </>
    );
}

export default UnauthenticatedHostLogin;