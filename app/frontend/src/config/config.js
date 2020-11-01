// export const API_BASE_URL = "http://localhost:8080";
// export const API_BASE_URL = 'https://multistore-app-yka4rjl6za-lz.a.run.app';
// export const API_BASE_URL = process.env.API_BASE_URL;
export const API_BASE_URL = 'https://multistore-backend-yka4rjl6za-ey.a.run.app';

export const ACCESS_TOKEN = 'accessToken';

// export const REACT_APP_URL = 'http://localhost:5000';
// export const REACT_APP_URL = process.env.REACT_APP_URL;
export const REACT_APP_URL =  'https://multistore-app-frontend-yka4rjl6za-ey.a.run.app';

export const OAUTH2_REDIRECT_URI = REACT_APP_URL + '/oauth2/redirect';

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;

export const ROLE_CLIENT = 'ROLE_CLIENT';
export const ROLE_EMPLOYEE = 'ROLE_EMPLOYEE';
export const ROLE_ADMIN = 'ROLE_ADMIN';