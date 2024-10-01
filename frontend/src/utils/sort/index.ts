import { Transaction } from '../../types';

export const sortByDate = (
  transactions: Transaction[],
  mode = 'desc'
): Transaction[] => {
  if (mode === 'desc' && transactions.length > 0) {
    return transactions.sort((a, b) => {
      return new Date(b.date).getTime() - new Date(a.date).getTime();
    });
  }
  if (mode === 'asc' && transactions.length > 0) {
    return transactions.sort((a, b) => {
      return new Date(a.date).getTime() - new Date(b.date).getTime();
    });
  }

  return transactions;
};
