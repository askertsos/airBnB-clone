import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const RedirectToLogin = () => {

    const navigate = useNavigate();

    useEffect(() => {
        navigate("/auth/login");
    }, [navigate])
    
}

export default RedirectToLogin;