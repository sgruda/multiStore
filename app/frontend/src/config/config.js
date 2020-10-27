
export const API_BASE_URL = 'https://multistore-app-yka4rjl6za-lz.a.run.app';

export const ACCESS_TOKEN = 'accessToken';

// export const OAUTH2_REDIRECT_URI = 'http://localhost:8080/oauth2/redirect';
// export const OAUTH2_REDIRECT_URI = process.env.REACT_APP_API_BASE_URL + '/oauth2/redirect';
export const OAUTH2_REDIRECT_URI = 'https://multistore-app-yka4rjl6za-lz.a.run.app/oauth2/redirect';

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;

export const ROLE_CLIENT = 'ROLE_CLIENT';
export const ROLE_EMPLOYEE = 'ROLE_EMPLOYEE';
export const ROLE_ADMIN = 'ROLE_ADMIN';