import { useState, useEffect } from "react";
import { BrowserRouter as Router,Routes, Route, Link } from 'react-router-dom';
import Home from './component/home';
import About from './component/about';
import Contact from './component/contact';
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
    <Router>
        <div className="App">
            <ul className="App-header">
            <li>
                <Link to="/">Home</Link>
            </li>
            <li>
                <Link to="/about">About Us</Link>
            </li>
            <li>
                <Link to="/contact">Contact Us</Link>
            </li>
            </ul>
        <Routes>
                <Route exact path='/' element={< Home />}></Route>
                <Route exact path='/about' element={< About />}></Route>
                <Route exact path='/contact' element={< Contact />}></Route>
        </Routes>
        </div>
    </Router>
);
}

export default App;
