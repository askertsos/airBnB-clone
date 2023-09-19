import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "./component/anonymous/home";
import LoginPost from "./component/anonymous/login";
import Logout from "./component/anonymous/logout";
import Register from "./component/anonymous/register";
import UserNotLogged from "./component/anonymous/UserNotLogged";
import RegistrationTenantComplete from "./component/anonymous/registrationTenantComplete";
import RegistrationHostComplete from "./component/anonymous/registrationHostComplete";
import RegistrationBothComplete from "./component/anonymous/registrationBothComplete";
import UnauthenticatedHostLogin from "./component/anonymous/unauthenticatedHostLogin";
import RedirectToLogin from "./component/anonymous/redirectToLogin";

import AdminHome from "./component/admin/adminHome";
import UserList from "./component/admin/userList";
import UserDetails from "./component/admin/userDetails";

import HostHome from "./component/host/hostHome";
import BothHome from "./component/host/bothHome";
import NewRentalComplete from "./component/host/newRentalComplete";
import NewRental from "./component/host/newRental";
import ListRentals from "./component/host/listRentals";
import RentalDetails from "./component/host/rentalDetails";

import Profile from "./component/global/profile";
import ProfileUpdateComplete from "./component/global/profileUpdateComplete";

import "./App.css";

function App() {

	return (
		<Router>
			<Routes>	

				<Route exact path="/auth/login" element={<LoginPost />}></Route>
				<Route exact path="/auth/logout" element={<Logout />}></Route>
				<Route exact path="/auth/register" element={<Register />}></Route>
				<Route exact path="/auth/register/tenantComplete" element={<RegistrationTenantComplete />}></Route>
				<Route exact path="/auth/register/hostComplete" element={<RegistrationHostComplete />}></Route>
				<Route exact path="/auth/register/bothComplete" element={<RegistrationBothComplete />}></Route>
				<Route exact path="/auth/login/unauthenticatedHostLogin" element={<UnauthenticatedHostLogin />}></Route>

				<Route exact path="/" element={<RedirectToLogin />}></Route>
				<Route exact path="/home" element={<Home />}></Route>
				<Route exact path="/unauthorized/user" element={<UserNotLogged />}></Route>
				<Route exact path="/user/profile" element={<Profile />}></Route>
				<Route exact path="/user/profile/update/complete" element={<ProfileUpdateComplete />}></Route>

				<Route exact path="/admin/home" element={<AdminHome />}></Route>
				<Route exact path="/admin/user/list" element={<UserList />}></Route>
				<Route exact path="/admin/user/:id/details" element={<UserDetails />}></Route>

				<Route exact path="/host/hostHome" element={<HostHome />}></Route>
				<Route exact path="/host/bothHome" element={<BothHome />}></Route>
				<Route exact path="/host/rental/list" element={<ListRentals />}></Route>
				<Route exact path="/host/rental/:id/details" element={<RentalDetails />}></Route>
				<Route exact path="/host/newRental" element={<NewRental />}></Route>
				<Route exact path="/host/newRentalComplete" element={<NewRentalComplete />}></Route>

			</Routes>
		</Router>
	);
}

export default App;
