import { useState, useEffect } from "react";
import './App.css';

function App() {
  
  const [jwt,setJwt] = useState(null);
  const [username,setUsername] = useState(null);

  useEffect(() => {
    const reqBody = {
      username: "user1",
      password: "pass1"
    }
    fetch("http://localhost:8080/auth/login", { 
      headers: {
        "Content-Type": "application/json",
      },
      method: "post",
      body: JSON.stringify(reqBody)
    }) 
      .then((response) => Promise.all([response.json(), response.headers]))
      .then(([body, headers]) => {
        setJwt(body.jwt);
        setUsername(body.user.username);
        console.log("Server Response Body:", body);
        console.log("Server Response Headers:", headers);
      });
  
  },[])


  return (
    <div className="App">
        <div>
          <h1>i sucka da cocka</h1> 
          <div>Hi, {username}</div>
        </div>
    </div>
  );

}

export default App;
