export enum TransactionType {
  Transfer = 'Transfer',
  Deposit = 'LOAD MONEY FROM CARD',
}
export interface Transaction {
  amount: number;
  detail?: string;
  date: string;
  id: string;
  type: TransactionType;
  origin?: string;
  destination?: string;
}

export interface Card {
  number: string;
  name: string;
  type: string;
  id: string;
}

export interface Account {
  name: string;
  origin: string;
}

export enum ActivityType {
  TRANSFER_IN = 'transfer-in',
  TRANSFER_OUT = 'transfer-out',
  DEPOSIT = 'deposit',
}
