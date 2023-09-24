// newRentalComplete.js
import React from "react";

import "../../css/host/newRentalComplete.css"

function NewRentalComplete() {
	return (
		<>
			<div className="new-rental-bg">
				<div className="new-rental-box">
					<div className="new-rental-h2 new-rental-complete-header">Succesfully added new rental!</div>
					<a href="https://localhost:3000/host/hostHome">
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