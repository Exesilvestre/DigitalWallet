import React from 'react';

export interface Errors {
  types?: Record<string, string>; // Hacemos que 'types' sea opcional
  message?: string; // También podemos hacer que 'message' sea opcional
}

export interface ErrorMessageProps {
  errors?: Errors; // Hacemos que 'errors' sea opcional
}

export const ErrorMessage = ({ errors }: ErrorMessageProps) => {
  if (!errors || !errors.types) {
    return null; // Si 'errors' o 'types' son undefined, no mostramos nada
  }

  const messages = Object.keys(errors.types).map((key) => errors.types![key]);

  return (
    <ul className="tw-flex tw-flex-col tw-gap-y-4 tw-pt-4 tw-bg-background">
      {messages.map((message, index) => (
        <li className="tw-text-error" key={`error-${index}`}>
          {message}
        </li>
      ))}
    </ul>
  );
};
