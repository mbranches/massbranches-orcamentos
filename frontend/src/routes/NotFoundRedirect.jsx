import { Navigate } from "react-router-dom";
import LoadingScreen from "../components/LoadingScreen";
import { useAuth } from "../hooks/useAuth";
import NotFoundPage from "../pages/NotFound";

const NotFoundRedirect = () => {
    const { isAuthenticated, loading } = useAuth();

    if(loading) return (<LoadingScreen />);

    if (isAuthenticated) return <NotFoundPage />
    
    return <Navigate to="/login" replace />;
};

export default NotFoundRedirect;