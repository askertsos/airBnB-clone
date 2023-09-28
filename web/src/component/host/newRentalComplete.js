// newRentalComplete.js
import React from "react";
import { BaseUrl, ClientPort } from "../../constants.js";

import "../../css/host/newRentalComplete.css"

function NewRentalComplete() {
	return (
		<>
			<div className="new-rental-bg">
				<div className="new-rental-box">
					<div className="new-rental-h2 new-rental-complete-header">Succesfully added new rental!</div>
					<a href={BaseUrl + ClientPort + "/host/hostHome"}>
						<button className="button new-rental-complete-button">
							Homepage
						</button>
					</a>
				</div>
			</div>
		</>
	);
}

export default NewRentalComplete;