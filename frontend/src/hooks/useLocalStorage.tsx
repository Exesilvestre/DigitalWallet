import { useState, useEffect, useRef } from 'react';

export function useLocalStorage(
  key: string,
  { serialize = JSON.stringify, deserialize = JSON.parse } = {}
) {
  const [value, setValue] = useState(() => {
    const valueInLocalStorage = window.localStorage.getItem(key);
    console.log("value in localstorage: ", valueInLocalStorage)
    if (valueInLocalStorage) {
      return deserialize(valueInLocalStorage);
    }
    return null;
  });

  const prevKeyRef = useRef(key);

  useEffect(() => {
    console.log("key: ", key)
    const prevKey = prevKeyRef.current;

    if (prevKey !== key) {
      window.localStorage.removeItem(prevKey);
    }
    prevKeyRef.current = key;
    window.localStorage.setItem(key, serialize(value));
  }, [value, serialize, key]);

  return [value, setValue];
}
