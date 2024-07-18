import React, { createContext, useEffect, useReducer } from 'react';
import userReducer from './userReducer';
import { User } from '../../types';
import { useAuth } from '../../hooks';
import { getUser, parseJwt } from '../../utils';
import { userActionTypes } from './types';
import { UNAUTHORIZED } from '../../constants/status';

export interface UserInfoState {
  user: User | null;
  loading: boolean;
}

const initialState: UserInfoState = {
  user: null,
  loading: true,
};

export const userInfoContext = createContext<{
  user: User | null;
  loading: boolean;
  dispatch: React.Dispatch<any>;
}>({
  ...initialState,
  dispatch: () => null,
});

const UserInfoProvider = ({ children }: { children: React.ReactNode }) => {
  const [state, dispatch] = useReducer(userReducer, initialState);
  const { isAuthenticated, setIsAuthenticated } = useAuth();

  useEffect(() => {
    if (isAuthenticated) {
      const token = localStorage.getItem('token');
      if (token) {
        const info = parseJwt(token);
        const userId = info && info.sub;
        userId &&
          getUser(userId)
            .then((res) => {
              dispatch({ type: userActionTypes.SET_USER, payload: res });
              dispatch({
                type: userActionTypes.SET_USER_LOADING, payload: false,
              });
            })
            .catch((error) => {
              if (error.status === UNAUTHORIZED) {
                localStorage.removeItem('token');
                setIsAuthenticated(false);
              }
              console.log(error);
            });
      } else {
        setIsAuthenticated(false);
      }
    }
  }, [dispatch, isAuthenticated, setIsAuthenticated]);

  return (
    <userInfoContext.Provider
      value={{ user: state.user, loading: state.loading, dispatch }}
    >
      {children}
    </userInfoContext.Provider>
  );
};

export default UserInfoProvider;
