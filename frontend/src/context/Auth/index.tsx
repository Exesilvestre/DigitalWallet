/* eslint-disable @typescript-eslint/no-empty-function */
import React, { createContext, useState, SetStateAction } from 'react';
import { useLocalStorage } from '../../hooks';
import { logout as apiLogout } from '../../utils/api';

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

  const logout = async () => {
    console.log('Logout function called');
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
