import React, { useState } from "react";

const LoginPost = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [jwt,setJwt] = useState(null);

  const onSubmit = (e) => {
    const reqBody = {
        username: {username},
        password: {password}
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
          console.log("Server Response Body:", body);
          console.log("Server Response Headers:", headers);
        });
    };

  return (
    <form onSubmit={onSubmit}>
      <div>
        <label>
            Username
            <input
                id="username"
                name="username"
                type="text"
                placeholder="username"
                onChange={event => setUsername(event.target.value)}
                value={username}
            />
        </label>
      </div>
      <div>
        <label>
            Password:
            <input
                id="password"
                name="password"
                type="text"
                placeholder="password"
                onChange={event => setPassword(event.target.value)}
                value={password}
            />
        </label>
      </div>
      <button type="submit">Sumbit</button>
    </form>
  );
};
export default LoginPost;