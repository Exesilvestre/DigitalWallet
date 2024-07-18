import React, { createContext, useState, useEffect, useRef, ReactNode } from 'react';
import Keycloak from 'keycloak-js';
import { useNavigate } from 'react-router-dom';

interface AuthContextType {
  isAuthenticated: boolean;
  token: string | undefined;
  login: () => void;
  logout: () => void;
  setIsAuthenticated: (isAuthenticated: boolean) => void;
}

const client = new Keycloak({
  realm: 'dh-money',
  url: 'http://localhost:8080', // URL base del servidor Keycloak
  clientId: 'dh-money-app',
});

export const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  token: undefined,
  login: () => {},
  logout: () => {},
  setIsAuthenticated: () => {},
});

const AuthProvider = ({ children }: { children: ReactNode }) => {
  const isRun = useRef(false);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [token, setToken] = useState<string | undefined>(undefined);

  useEffect(() => {
    if (isRun.current) return;
    isRun.current = true;

    const initializeKeycloak = async () => {
      try {
        const authenticated = await client.init({
          onLoad: 'login-required',
          checkLoginIframe: false,
          redirectUri: 'http://localhost:3000/', // URL específica para redireccionar después de la autenticación
        });

        if (authenticated) {
          setIsAuthenticated(true);
          setToken(client.token || undefined);
          localStorage.setItem('token', client.token || ''); // Guarda el token en localStorage

          client.onAuthSuccess = () => {
            setToken(client.token || undefined);
            localStorage.setItem('token', client.token || ''); // Actualiza el token en localStorage
          };

          client.onAuthError = () => setIsAuthenticated(false);
          client.onAuthLogout = () => {
            setIsAuthenticated(false);
            localStorage.removeItem('token'); // Remueve el token de localStorage
          };
        } else {
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error('Error initializing Keycloak', error);
        setIsAuthenticated(false);
      }
    };

    initializeKeycloak();
  }, []);

  const login = () => {
    client.login();
  };

  const logout = () => {
    client.logout({ redirectUri: 'http://localhost:3000/' });
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, token, login, logout, setIsAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
