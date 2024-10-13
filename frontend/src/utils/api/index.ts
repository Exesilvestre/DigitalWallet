import { UserAccount, User, Transaction, Card } from '../../types';

const myInit = (method = 'GET', token?: string) => {
  return {
    method,
    headers: {
      'Content-Type': 'application/json',
      Authorization: token ? `Bearer ${token}` : '',
    },
    mode: 'cors' as RequestMode,
    cache: 'default' as RequestCache,
  };
};

const myRequest = (endpoint: string, method: string, token?: string) =>
  new Request(endpoint, myInit(method, token));

const baseUrl = 'http://localhost:8082';

const rejectPromise = (response?: Response): Promise<Response> =>
  Promise.reject({
    status: (response && response.status) || '00',
    statusText: (response && response.statusText) || 'Ocurrió un error',
    err: true,
  });

export const login = (email: string, password: string) => {
  return fetch(myRequest(`${baseUrl}/auth-server/api/login`, 'POST'), {
    body: JSON.stringify({ email, password }),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const createAnUser = (user: User) => {
  return fetch(myRequest(`${baseUrl}/users-server/api/register`, 'POST'), {
    body: JSON.stringify(user),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const logout = (token: string) => {
  return fetch(myRequest(`${baseUrl}/auth-server/api/logout`, 'POST', token))
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUser = (id: string): Promise<User> => {
  return fetch(myRequest(`${baseUrl}/users-server/api/user/${id}`, 'GET'))
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const updateUser = (
  id: string,
  data: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/users/${id}`, 'PATCH', token), {
    body: JSON.stringify(data),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getAccount = (id: string, token: string): Promise<UserAccount> => {
  return fetch(myRequest(`${baseUrl}/accounts-server/api/accounts/${id}`, 'GET', token), {})
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getAccounts = (token: string): Promise<UserAccount[]> => {
  return fetch(myRequest(`${baseUrl}/accounts-server/api/accounts/getAll`, 'GET', token))
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const updateAccount = (
  id: string,
  data: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/accounts-server/api/accounts/${id}`, 'PATCH', token), {
    body: JSON.stringify(data),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserRecentActivities = (
  userId: string,
  token: string,
  limit?: number
): Promise<Transaction[]> => {
  return fetch(
    myRequest(
      `${baseUrl}/activities-server/api/${userId}/transferences${limit ? `?_limit=${limit}` : ''}`,
      'GET',
      token
    )
  )
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserActivities = (
  userId: string,
  token: string,
  limit?: number
): Promise<Transaction[]> => {
  return fetch(
    myRequest(
      `${baseUrl}/activities-server/api/${userId}/activty`,
      'GET',
      token
    )
  )
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserActivity = (
  userId: string,
  activityId: string,
  token: string
): Promise<Transaction> => {
  return fetch(
    myRequest(
      `${baseUrl}/activities-server/api/${userId}/activty/${activityId}`,
      'GET',
      token
    )
  )
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserCards = (
  userId: string,
  token: string
): Promise<Card[]> => {
  return fetch(myRequest(`${baseUrl}/cards-server/api/accounts/${userId}/cards`, 'GET', token))
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserCard = (userId: string, cardId: string): Promise<Card> => {
  return fetch(myRequest(`${baseUrl}/accounts-server/api/accounts/${userId}/cards/${cardId}`, 'GET'))
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const deleteUserCard = (
  userId: string,
  cardId: string,
  token: string
): Promise<Response> => {
  return fetch(
    myRequest(`${baseUrl}/cards-server/api/accounts/${userId}/cards/${cardId}`, 'DELETE', token)
  )
    .then((response) => {
      if (response.ok) {
        // No intentar convertir a JSON si no hay contenido
        return Promise.resolve(response);
      }
      return rejectPromise(response); // Manejar el caso de error
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const createUserCard = (
  userId: string,
  card: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/cards-server/api/accounts/${userId}/cards`, 'POST', token), {
    body: JSON.stringify(card),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const createDepositActivityByCard = (
  userId: string,
  amount: number,
  token: string,
  cardNumber: string,
) => {
  const maxAmount = 30000;
  if (amount > maxAmount) return rejectPromise();

  const activity = {
    amount,
    cardNumber,
  };

  return fetch(
    myRequest(`${baseUrl}/activities-server/api/accounts/${userId}/transferences`, 'POST', token),
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(activity),
    }
  )
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};



export const createTransferActivity = (
  userId: string,
  token: string,
  origin: string,
  destination: string,
  amount: number,
  name?: string
) => {
  return fetch(
    myRequest(`${baseUrl}/activities-server/api/accounts/${userId}/transfer`, 'POST', token),
    {
      body: JSON.stringify({
        type: 'Transfer',
        amount: amount * -1,
        origin,
        destination,
        name,
      }),
    }
  )
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};
