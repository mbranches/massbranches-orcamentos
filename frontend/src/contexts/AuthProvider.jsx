import React, { useState, useEffect } from 'react';
import AuthContext from './AuthContext';
import { jwtDecode } from 'jwt-decode';
import api from '../services/Api';

function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token'));
    const [loading, setLoading] = useState(true);

    
    useEffect(() =>  {
        const initAuth = async () => {
            const storedToken = localStorage.getItem('token');
            if (storedToken) {
                try {
                    const fetchedUser = await fetchUser(storedToken);
                    setUser(fetchedUser);
                } catch (error) {
                    localStorage.removeItem('token');
                    setUser(null);
                } finally {
                    setLoading(false);
                }
            }
            setLoading(false);
        };

        initAuth();
    }, []);
    
    const fetchUser = async (token) => {
        const decodedToken = jwtDecode(token);
        const userCurrent = await getUser();

        const finalUser = {
            ...userCurrent,
            roles: decodedToken.roles
        }

        return finalUser;
    }

    const getUser = async () => {
        const response = await api.get("/users/me");

        return response.data
    }

    const login = async (token) => {
        localStorage.setItem('token', token);

        setToken(token);
        setUser(await fetchUser(token));
    };

    const logout = () => {
        localStorage.removeItem('token');
        setToken(null);
        setUser(null);
    };

    const contextValue = {
        user,
        token,
        login,
        logout,
        isAuthenticated: !!user, 
        isAdmin: user?.roles?.includes('ROLE_ADMIN'), 
        loading
    };
    
    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
}

export default AuthProvider;