/* eslint-disable @typescript-eslint/no-empty-function */
import React, { createContext, useState, useEffect, SetStateAction } from 'react';
import { useLocalStorage } from '../../hooks';
import { logout as apiLogout } from '../../utils/api';
import { parseJwt } from '../../utils';

export const AuthContext = createContext<{
  isAuthenticated: boolean;
  setIsAuthenticated: React.Dispatch<SetStateAction<boolean>>;
  logout: () => void;
}>({
  isAuthenticated: false,
  setIsAuthenticated: () => {},
  logout: () => {},
});

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [token, setToken] = useLocalStorage('token');
  const [isAuthenticated, setIsAuthenticated] = useState(!!token);

  useEffect(() => {
    if (token) {
      const info = parseJwt(token);
      if (info && info.exp && info.exp * 1000 < Date.now()) {
        // Token expirado
        setToken(null);
        setIsAuthenticated(false);
        window.location.href = '/login'; // Redirige al login
      }
    }
  }, [token, setToken]);

  const logout = async () => {
    console.log("token", token);
    if (token) {
      console.log('token exists');
      try {
        await apiLogout(token);
      } catch (error) {
        console.log('Error during logout:', error);
      }
    }
    setIsAuthenticated(false);
    setToken(null);
  };

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, setIsAuthenticated, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
