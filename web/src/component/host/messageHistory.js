// messageHistoryHost.js
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import "../../css/messages.css"

function MessageHistoryHost() {

    const [history, setHistory] = useState([]);
    const [page, setPage] = useState(1);
    const [maxPage, setMaxPage] = useState(1);
    const navigate = useNavigate();
    const routeParams = useParams();
    const rentalId = routeParams.id;

    useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
                "Authorization": "Bearer "  + localStorage.getItem("jwt")
			},
			method: "get",
		};
		fetch("https://localhost:8080/host/rentals/" + rentalId + "/messages/" + page, fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setHistory(response.History);
            setMaxPage(response.MaxPage);
        })
        .catch((message) => {
            console.log(message);
            navigate("/");
        })
    }, [page]);

    const MessageDetails = (tenantId, messages) => {
        localStorage.setItem("messageHistory", JSON.stringify(messages));
        navigate("/host/rental/" + rentalId +"/messages/tenant/" + tenantId);
    };

    const nextPage = () => {
        if (page + 1 <= maxPage) setPage(page + 1);
    };

    const previousPage = () => {
        if (page > 1) setPage(page - 1);
    };


	return (
		<>
            <div className="host-messages-bg">
                <a href = {"https://localhost:3000/host/rental/" + rentalId + "/details"}>
                    <button className="button back-todetails">
                        Back to rental details
                    </button>
                </a>
                <div className="page-header-mes" >Current page : {page}</div>
                <button className="button mes-next" id="submit" type="button" onClick={() => nextPage()}>
                        Next Page
                </button>
                <button className="button mes-prev" id="submit" type="button" onClick={() => previousPage()}>
                        Previous Page
                </button>
                <div>
                    {history.map((item) => (
                        <p>
                            <button className="user-list msg-user-color" onClick={() => MessageDetails(item.tenant.id, item.messageList)}>
                                <img className="admin-photo-field" src={require("../profile_photos/" + "default" + ".jpg")} alt="profilePic"/>
                                <div className="admin-uname-field"> Username : {item.tenant.username} </div>
                            </button>
                        </p>
                    ))}
				</div>
			</div>
		</>
	);
}

export default MessageHistoryHost;
