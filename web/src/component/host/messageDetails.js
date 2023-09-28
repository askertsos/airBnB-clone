// messageDetail.js
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { BaseUrl, ServerPort, ClientPort } from "../../constants.js";

import "../../css/messages.css"

function MessageDetail() {

    const [history, setHistory] = useState([]);
    const [newMessage, setNewMessage] = useState("");
    const [page, setPage] = useState(1);
    const [maxPage, setMaxPage] = useState(1);
    const navigate = useNavigate();
    const routeParams = useParams();
    const rentalId = routeParams.id;
    const tenantId = routeParams.tenant_id;

    useEffect(() => {
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
                "Authorization": "Bearer "  + localStorage.getItem("jwt")
			},
			method: "get",
		};
		fetch(BaseUrl + ServerPort + "/host/" + rentalId + "/tenant/" + tenantId + "/message_history/" + page, fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            let reverted_history;
            if(response.Messages.length > 0) {
                reverted_history = new Array(18);
                response.Messages.map((message, index) => {
                    reverted_history[18 - index] = message;
                })
            }
            else reverted_history = [];
            setHistory(reverted_history);
            setMaxPage(response.MaxPage);
        })
        .catch((message) => {
            console.log(message);
            navigate("/");
        })
    }, [page]);

    const reformatDate = (datetime) => {
        let reformated_datetime = datetime.substring(0,10) + " " + datetime.substring(11,16);
        return reformated_datetime;
    };

    const sendNewMessage = () =>{

        if( newMessage !== ""){
            const fetchOptions = {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer "  + localStorage.getItem("jwt")
                },
                method: "post",
                body: newMessage
            };
            fetch(BaseUrl + ServerPort + "/host/rentals/" + rentalId + "/message/" + tenantId, fetchOptions)
            .then((response) => {
                window.location.reload(false);
            })
            .catch((message) => {
                alert(message);
            });
        }

    };

    const nextPage = () => {
        if (page + 1 <= maxPage) setPage(page + 1);
    };

    const previousPage = () => {
        if (page > 1) setPage(page - 1);
    };


	return (
		<>
            <div className="messages-bg">
                <a href = {BaseUrl + ClientPort + "/host/rental/" + rentalId + "/messages"}>
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
                <div className="message-history-box">
                    {history.map((message) => (
                        <p className="message"> [{reformatDate(message.sentAt)}] {message.sender.first_name} : {message.contents}</p>
                    ))}
                    <input 
                        className="new-message-input"
                        id="new message"
                        name="new message"
                        type="text"
                        placeholder="new message"
                        onChange={(event) => setNewMessage(event.target.value)}
                        value={newMessage}
                    />
                    <button className="new-message-button" onClick={() => sendNewMessage()}>
                        Send new message
                    </button>
				</div>
			</div>
		</>
	);
}

export default MessageDetail;
