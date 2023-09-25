// reviewComplete.js
import React from "react";

import "../../css/anonymous/logout.css"

function ReviewComplete() {
	return (
		<>
			<div className="logout-bg">
				<div className="logout-box">
					<div className="logout-h2 logout-header">Succesfully submitted review!</div>
					<a href = 'https://localhost:3000/home'>
						<button className="button logout-home-button logout-home-btn">Homepage</button>
					</a>
				</div>
			</div>
		</>
	);
}

export default ReviewComplete;
