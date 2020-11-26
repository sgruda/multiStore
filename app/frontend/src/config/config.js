// export const API_BASE_URL = "http://localhost:8080";
export const API_BASE_URL = 'https://multistore-backend-yka4rjl6za-ey.a.run.app';
export const API_URL_SIGN_IN = API_BASE_URL + "/api/auth/signin";
export const API_URL_SIGN_UP = API_BASE_URL + "/api/auth/signup";
export const API_URL_VERIFY_EMAIL = API_BASE_URL + "/api/auth/verify-email";
export const API_URL_ACCOUNTS = API_BASE_URL + "/api/accounts";
export const API_URL_SINGLE_ACCOUNT = API_BASE_URL + "/api/account";
export const API_URL_SINGLE_ACCOUNT_EDIT = API_URL_SINGLE_ACCOUNT + "/edit";
export const API_URL_SINGLE_ACCOUNT_REMOVE = API_URL_SINGLE_ACCOUNT + "/remove";
export const API_URL_SINGLE_ACCOUNT_CREATE = API_URL_SINGLE_ACCOUNT + "/create";
export const API_URL_SINGLE_ACCOUNT_CHANGE_PASSWORD = API_URL_SINGLE_ACCOUNT + '/change-password';
export const API_URL_SINGLE_ACCOUNT_ADD_ACCESS_LEVEL = API_URL_SINGLE_ACCOUNT + '/add-access-level';
export const API_URL_SINGLE_ACCOUNT_REMOVE_ACCESS_LEVEL = API_URL_SINGLE_ACCOUNT + '/remove-access-level';
export const API_URL_SINGLE_ACCOUNT_BLOCK = API_URL_SINGLE_ACCOUNT + '/block';
export const API_URL_SINGLE_ACCOUNT_UNBLOCK = API_URL_SINGLE_ACCOUNT + '/unblock';
export const API_URL_SINGLE_ACCOUNT_RESEND_CONFIRM_MAIL = API_URL_SINGLE_ACCOUNT + '/send-email-verification';
export const API_URL_MY_ACCOUNT = API_BASE_URL + "/api/account/me";
export const API_URL_MY_ACCOUNT_EDIT = API_URL_MY_ACCOUNT + "/edit";
export const API_URL_MY_ACCOUNT_CHANGE_PASSWORD = API_URL_MY_ACCOUNT + "/change-password";
export const API_URL_RESET_PASSWORD = API_BASE_URL + '/api/auth/reset-password';
export const API_URL_CHANGE_RESETTED_PASSWORD = API_URL_RESET_PASSWORD + '/change';


export const ACCESS_TOKEN = 'accessToken';


// export const REACT_APP_URL = 'http://localhost:3000';
export const REACT_APP_URL =  'https://multistore-app-frontend-yka4rjl6za-ey.a.run.app';

export const OAUTH2_REDIRECT_URI = REACT_APP_URL + '/oauth2/redirect';

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;

export const ROLE_CLIENT = 'ROLE_CLIENT';
export const ROLE_EMPLOYEE = 'ROLE_EMPLOYEE';
export const ROLE_ADMIN = 'ROLE_ADMIN';