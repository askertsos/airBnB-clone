import React, { useState } from "react";
import axios from "axios";

const LoginPost = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const onChangeUsername = (e) => {
    setUsername(e.target.value);
  };

  const onChangePassword = (e) => {
    setPassword(e.target.value);
  };

  const onSubmit = (e) => {
    e.preventDefault();
    const newPost = {
      username: e.username,
      password: e.password,
    };
    console.log(newPost);
    axios.post("http://localhost:8080/auth/login", newPost)
      .then((res) => console.log(res.data))
      .catch((err) => console.log(err));
  };
  return (
    <form onSubmit={onSubmit}>
      <div>
        <label>
            Username:
            <input
                type="text"
                name="username"
                value={username} // Link to the state variable
                onChange={onChangeUsername} // Handle input changes
            />
        </label>
      </div>
      <div>
        <label>
            Password:
            <input
                type="text"
                name="password"
                value={password} // Link to the state variable
                onChange={onChangePassword} // Handle input changes
            />
        </label>
      </div>
      <button type="submit">POST</button>
    </form>
  );
};
export default LoginPost;