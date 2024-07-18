import Keycloak, { KeycloakConfig } from 'keycloak-js';

// Configuración de Keycloak
const keycloakConfig: KeycloakConfig = {
  realm: 'dh-money',
  url: 'http://localhost:8080', // URL base del servidor Keycloak con el sufijo /auth
  clientId: 'dh-money-app',
};

// Crear la instancia de Keycloak usando el objeto de configuración
const keycloak = new Keycloak(keycloakConfig);

export default keycloak;
