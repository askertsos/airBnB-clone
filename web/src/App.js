// import logo from './logo.svg';
import './App.css';

function App() {
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
      const jwtToken = headers.get("authorization");
      console.log(jwtToken);
      console.log(body);
    });

  return (
    <div className="App">
      <h1>i sucka da cocka</h1> 
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
