import React, { useEffect } from 'react';
import { useAuth } from '../../hooks';
import { CircularProgress } from '@mui/material';

const Login = () => {
  const { login } = useAuth();

  useEffect(() => {
    // Redirige al usuario a la página de inicio de sesión de Keycloak
    login();
  }, [login]);

  return (
    <div
      className="tw-w-full tw-flex tw-flex-col tw-flex-1 tw-items-center tw-justify-center"
      style={{
        height: 'calc(100vh - 128px)',
      }}
    >
      <h2>Redirigiendo a Keycloak...</h2>
      <CircularProgress />
    </div>
  );
};

export default Login;
