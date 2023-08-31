import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./component/home";
import LoginPost from "./component/login";
import Logout from "./component/logout";
import User from "./component/user";
import Register from "./component/register";
import UserNotLogged from "./component/UserNotLogged";
import RegistrationTenantComplete from "./component/registrationTenantComplete";
import RegistrationHostComplete from "./component/registrationHostComplete";
import RegistrationBothComplete from "./component/registrationBothComplete";
import UnauthenticatedHostLogin from "./component/unauthenticatedHostLogin";
import "./App.css";

function App() {

	return (
		<Router>
			<Routes>	
				<Route exact path="/" element={<Home />}></Route>
				<Route exact path="/auth/login" element={<LoginPost />}></Route>
				<Route exact path="/auth/logout" element={<Logout />}></Route>
				<Route exact path="/home" element={<Home />}></Route>
				<Route exact path="/user" element={<User />}></Route>
				<Route exact path="/unauthorized/user" element={<UserNotLogged />}></Route>
				<Route exact path="/auth/register" element={<Register />}></Route>
				<Route exact path="/auth/register/tenantComplete" element={<RegistrationTenantComplete />}></Route>
				<Route exact path="/auth/register/hostComplete" element={<RegistrationHostComplete />}></Route>
				<Route exact path="/auth/register/bothComplete" element={<RegistrationBothComplete />}></Route>
				<Route exact path="/auth/login/unauthenticatedHostLogin" element={<UnauthenticatedHostLogin />}></Route>
			</Routes>
		</Router>
	);
}

export default App;
