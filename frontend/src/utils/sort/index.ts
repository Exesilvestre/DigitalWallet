import { Transaction } from '../../types';

export const sortByDate = (
  transactions: Transaction[],
  mode = 'desc'
): Transaction[] => {
  if (mode === 'desc' && transactions.length > 0) {
    return transactions.sort((a, b) => {
      return new Date(b.dated).getTime() - new Date(a.dated).getTime();
    });
  }
  if (mode === 'asc' && transactions.length > 0) {
    return transactions.sort((a, b) => {
      return new Date(a.dated).getTime() - new Date(b.dated).getTime();
    });
  }

  return transactions;
};
