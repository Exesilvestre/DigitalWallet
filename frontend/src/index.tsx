import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import UserInfoProvider from './context/User';
import AuthProvider from './context/Auth';
import { BrowserRouter } from 'react-router-dom';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
    <BrowserRouter>
      <AuthProvider>
        <UserInfoProvider>
          <ThemeProvider theme={darkTheme}>
            <App />
          </ThemeProvider>
        </UserInfoProvider>
      </AuthProvider>
    </BrowserRouter>
);
