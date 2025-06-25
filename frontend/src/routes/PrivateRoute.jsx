import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

function PrivateRoute({ children }) {
    const token = localStorage.getItem('token');

    if (!token) {
        return <Navigate to="/login" replace />;
    }

    try {
        const decodedToken = jwtDecode(token);
        
        const isExpired = decodedToken.exp * 1000 < Date.now();

        if (isExpired) {
            localStorage.removeItem('token'); 
            return <Navigate to="/login" replace />;
        }
    } catch (error) {
        localStorage.removeItem('token');
        return <Navigate to="/login" replace />;
    }

    return children;
}

export default PrivateRoute;