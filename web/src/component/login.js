import React, { useState } from "react";
import axios from "axios";

const LoginPost = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const onSubmit = (e) => {
    e.preventDefault();
    const newPost = {
      username: {username},
      password: {password},
    };
    console.log(newPost);
    axios.post("http://localhost:8080/auth/login", JSON.stringify(newPost))
      .then((res) => console.log(res.data))
      .catch((err) => console.log(err));
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