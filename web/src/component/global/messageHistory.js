// messageHistory.js
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import "../../css/messages.css"

function MessageHistory() {

    const [history, setHistory] = useState([]);
    const [newMessage, setNewMessage] = useState("");
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
		fetch("https://localhost:8080/rentals/" + rentalId + "/message_history", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setHistory(response.messageSet);
        })
        .catch((message) => {
            console.log(message);
            navigate("/");
        })
    }, []);

    const reformatDate = (datetime) => {
        let reformated_datetime = datetime.substring(0,10) + " " + datetime.substring(11,16);
        return reformated_datetime;
    };

    const sendNewMessage = () =>{

        console.log(newMessage);
        if( newMessage !== ""){
            const fetchOptions = {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer "  + localStorage.getItem("jwt")
                },
                method: "post",
                body: newMessage
            };
            fetch("https://localhost:8080/rentals" + rentalId + "/message_host", fetchOptions)
            .then((response) => {
                window.location.reload(false);
            })
            .catch((message) => {
                alert(message);
            });
        }

    };

	return (
		<>
            <div className="messages-bg">
                <a href = {"https://localhost:3000/search/" + rentalId + "/details"}>
                    <button className="button back-todetails">
                        Back to rental details
                    </button>
                </a>
                <div className="message-history-box">
                    {history.slice(0, 18).map((message) => (
                        <p className="message"> [{reformatDate(message.sentAt)}] {message.sender.username} : {message.contents}</p>
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

export default MessageHistory;
