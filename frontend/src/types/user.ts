export interface User {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone?: string;
  dni?: string;
  id?: string;
}

export interface UserAccount {
  balance: number;
  cvu: string;
  alias: string;
  userId: string;
  id: string;
  name: string;
}
