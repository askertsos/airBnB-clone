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
import SearchResults from "./component/anonymous/searchResults";
import SearchDetails from "./component/anonymous/searchDetails";

import AdminHome from "./component/admin/adminHome";
import UserList from "./component/admin/userList";
import UserDetails from "./component/admin/userDetails";

import HostHome from "./component/host/hostHome";
import BothHome from "./component/host/bothHome";
import NewRentalComplete from "./component/host/newRentalComplete";
import NewRental from "./component/host/newRental";
import ListRentals from "./component/host/listRentals";
import RentalDetails from "./component/host/rentalDetails";
import MessageHistoryHost from "./component/host/messageHistory";
import MessageDetails from "./component/host/messageDetails";

import Review from "./component/tenant/review";
import ReviewComplete from "./component/tenant/reviewComplete";
import BookComplete from "./component/tenant/bookComplete";

import Profile from "./component/global/profile";
import ProfileUpdateComplete from "./component/global/profileUpdateComplete";
import MessageHistory from "./component/global/messageHistory";

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
				<Route exact path="/search/results" element={<SearchResults />}></Route>
				<Route exact path="/search/:id/details" element={<SearchDetails />}></Route>
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
				<Route exact path="/host/rental/:id/messages" element={<MessageHistoryHost />}></Route>
				<Route exact path="/host/rental/:id/messages/tenant/:tenant_id" element={<MessageDetails />}></Route>

				<Route exact path="/tenant/:id/review" element={<Review />}></Route>
				<Route exact path="/tenant/review/complete" element={<ReviewComplete />}></Route>
				<Route exact path="/tenant/book/complete" element={<BookComplete />}></Route>

				<Route exact path="/rental/:id/message/history" element={<MessageHistory />}></Route>

			</Routes>
		</Router>
	);
}

export default App;
