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

const baseUrl = 'http://localhost:3500';

const rejectPromise = (response?: Response): Promise<Response> =>
  Promise.reject({
    status: (response && response.status) || '00',
    statusText: (response && response.statusText) || 'Ocurrió un error',
    err: true,
  });

export const login = (email: string, password: string) => {
  return fetch(myRequest(`${baseUrl}/login`, 'POST'), {
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
  return fetch(myRequest(`${baseUrl}/register`, 'POST'), {
    body: JSON.stringify(user),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .then((data) => {
      createAnAccount(data);
      return data;
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUser = (id: string): Promise<User> => {
  return fetch(myRequest(`${baseUrl}/users/${id}`, 'GET'))
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

// TODO: remove this functionality once backend is ready
const generateCvu = (): string => {
  let cvu = '';
  for (let i = 0; i < 22; i++) {
    cvu += Math.floor(Math.random() * 10);
  }
  return cvu;
};

// TODO: remove this functionality once backend is ready
const generateAlias = (): string => {
  const words = [
    'Cuenta',
    'Personal',
    'Banco',
    'Argentina',
    'Digital',
    'Money',
    'House',
    'Bank',
    'Account',
    'Cartera',
    'Wallet',
    'Pago',
    'Pay',
    'Rapido',
    'Seguro',
  ];
  const length = 3;
  let alias = '';
  for (let i = 0; i < length; i++) {
    alias += words[Math.floor(Math.random() * words.length)];
    if (i < length - 1) {
      alias += '.';
    }
  }
  return alias;
};

// TODO: remove this functionality once backend is ready
export const createAnAccount = (data: any): Promise<Response> => {
  const { user, accessToken } = data;

  const alias = generateAlias();
  const cvu = generateCvu();
  const account = {
    alias,
    cvu,
    balance: 0,
    name: `${user.firstName} ${user.lastName}`,
  };

  return fetch(
    myRequest(`${baseUrl}/users/${user.id}/accounts`, 'POST', accessToken),
    {
      body: JSON.stringify(account),
    }
  ).then((response) =>
    response.ok ? response.json() : rejectPromise(response)
  );
};

export const getAccount = (id: string, token: string): Promise<UserAccount> => {
  return fetch(myRequest(`${baseUrl}/users/${id}/accounts`, 'GET', token), {})
    .then((response) => {
      if (response.ok) {
        return response.json().then((account) => account[0]);
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getAccounts = (): Promise<UserAccount[]> => {
  return fetch(myRequest(`${baseUrl}/accounts`, 'GET'))
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
  return fetch(myRequest(`${baseUrl}/users/${id}/accounts/1`, 'PATCH', token), {
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

export const getUserActivities = (
  userId: string,
  token: string,
  limit?: number
): Promise<Transaction[]> => {
  return fetch(
    myRequest(
      `${baseUrl}/users/${userId}/activities${limit ? `?_limit=${limit}` : ''}`,
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
      `${baseUrl}/users/${userId}/activities/${activityId}`,
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
  return fetch(myRequest(`${baseUrl}/users/${userId}/cards`, 'GET', token))
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
  return fetch(myRequest(`${baseUrl}/users/${userId}/cards/${cardId}`, 'GET'))
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
    myRequest(`${baseUrl}/users/${userId}/cards/${cardId}`, 'DELETE', token)
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

export const createUserCard = (
  userId: string,
  card: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/users/${userId}/cards`, 'POST', token), {
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

// TODO: edit when backend is ready
export const createDepositActivity = (
  userId: string,
  amount: number,
  token: string
) => {
  const maxAmount = 30000;
  if (amount > maxAmount) return rejectPromise();

  const activity = {
    amount,
    type: 'Deposit',
    description: 'Depósito con tarjeta',
    dated: new Date(), // date must be genarated in backend
  };

  return fetch(
    myRequest(`${baseUrl}/users/${userId}/activities`, 'POST', token),
    {
      body: JSON.stringify(activity),
    }
  )
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .then((data) => {
      depositMoney(data.amount, userId, token);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

// TODO: remove when backend is ready
const depositMoney = (amount: number, userId: string, token: string) => {
  return getAccount(userId, token)
    .then((account) => {
      const newBalance = account.balance + amount;
      const accountId = account.id;
      return {
        newBalance,
        accountId,
      };
    })
    .then(({ newBalance, accountId }) => {
      fetch(
        myRequest(
          `${baseUrl}/users/${userId}/accounts/${accountId}`,
          'PATCH',
          token
        ),
        {
          body: JSON.stringify({ balance: newBalance }),
        }
      )
        .then((response) =>
          response.ok ? response.json() : rejectPromise(response)
        )
        .catch((err) => {
          console.log(err);
          return rejectPromise(err);
        });
    });
};

// TODO: edit when backend is ready
export const createTransferActivity = (
  userId: string,
  token: string,
  origin: string,
  destination: string,
  amount: number,
  name?: string
) => {
  return fetch(
    myRequest(`${baseUrl}/users/${userId}/activities`, 'POST', token),
    {
      body: JSON.stringify({
        type: 'Transfer',
        amount: amount * -1,
        origin,
        destination,
        name,
        dated: new Date(), // date must be genarated in backend
      }),
    }
  )
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .then((response) => {
      discountMoney(response.amount, userId, token);
      return response;
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

// TODO: remove when backend is ready
const discountMoney = (amount: number, userId: string, token: string) => {
  return getAccount(userId, token)
    .then((account) => {
      // amount is negavite
      const newBalance = account.balance + amount;
      const accountId = account.id;
      return {
        newBalance,
        accountId,
      };
    })
    .then(({ newBalance, accountId }) => {
      fetch(
        myRequest(
          `${baseUrl}/users/${userId}/accounts/${accountId}`,
          'PATCH',
          token
        ),
        {
          body: JSON.stringify({ balance: newBalance }),
        }
      )
        .then((response) =>
          response.ok ? response.json() : rejectPromise(response)
        )
        .catch((err) => {
          console.log(err);
          return rejectPromise(err);
        });
    });
};
