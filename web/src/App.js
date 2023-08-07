// import logo from './logo.svg';
import { useState, useEffect } from "react";
import './App.css';

function App() {
  
  const [jwt,setJwt] = useState("");

  useEffect(() => {
    const reqBody = {
      username: "kk",
      password: "ll"
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
        setJwt(headers.get("authorization"));
        // console.log(a);
        // console.log(body);
        // setJwt(a);
      });
  
  },[jwt])


  return (
    <div className="App">
      <h1>i sucka da cocka {jwt}</h1> 
      <div>JWT is {jwt}</div>
    </div>
  )

  // return (
  //   <div className="App">
  //     <header className="App-header">
  //       <img src={logo} className="App-logo" alt="logo" />
  //       <p>
  //         Edit <code>src/App.js</code> and save to reload.
  //       </p>
  //       <a
  //         className="App-link"
  //         href="https://reactjs.org"
  //         target="_blank"
  //         rel="noopener noreferrer"
  //       >
  //         Learn React
  //       </a>
  //     </header>
  //   </div>
  // );
}

export default App;
