import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth'; 
import LoadingScreen from '../components/LoadingScreen';

const PrivateRoutes = () => {
  const { isAuthenticated, loading } = useAuth();

  if(loading) {
    return <LoadingScreen />
  }

  return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />;
};

export default PrivateRoutes;