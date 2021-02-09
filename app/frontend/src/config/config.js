export const API_BASE_URL = 'https://backend-7lhoj5kiqa-ey.a.run.app';
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
export const API_URL_MY_ACCOUNT_CHANGE_LANGUAGE = API_URL_MY_ACCOUNT + "/change-language";
export const API_URL_RESET_PASSWORD = API_BASE_URL + '/api/auth/reset-password';
export const API_URL_CHANGE_RESETTED_PASSWORD = API_URL_RESET_PASSWORD + '/change';

export const API_URL_PRODUCTS = API_BASE_URL + "/api/products";
export const API_URL_PRODUCT = API_BASE_URL + "/api/product";
export const API_URL_CREATE_PRODUCT = API_URL_PRODUCT + "/create";
export const API_URL_EDIT_PRODUCT = API_URL_PRODUCT + "/edit";
export const API_URL_BLOCK_PRODUCT = API_URL_PRODUCT + "/block";
export const API_URL_UNBLOCK_PRODUCT = API_URL_PRODUCT + "/unblock";

export const API_URL_PROMOTIONS = API_BASE_URL + "/api/promotions";
export const API_URL_PROMOTION = API_BASE_URL + "/api/promotion";
export const API_URL_CREATE_PROMOTION = API_URL_PROMOTION + "/create";
export const API_URL_DELETE_PROMOTION = API_URL_PROMOTION + "/delete";
export const API_URL_BLOCK_PROMOTION = API_URL_PROMOTION + "/block";
export const API_URL_UNBLOCK_PROMOTION = API_URL_PROMOTION + "/unblock";

export const API_URL_BASKET = API_BASE_URL + "/api/basket";
export const API_URL_BASKET_SIZE = API_URL_BASKET + "/size";
export const API_URL_BASKET_ADD = API_URL_BASKET + "/add";
export const API_URL_BASKET_ITEM_EDIT = API_URL_BASKET + "/item/edit";
export const API_URL_BASKET_REMOVE = API_URL_BASKET + "/remove";

export const API_URL_ORDER = API_BASE_URL + "/api/order";
export const API_URL_ORDER_CHANGE_STATUS = API_URL_ORDER + "/change-status";
export const API_URL_ORDER_SUBMIT = API_URL_ORDER + "/submit";
export const API_URL_ORDER_TOTAL_PRICE = API_URL_ORDER + "/total-price";
export const API_URL_ORDERS = API_BASE_URL + "/api/orders";
export const API_URL_ORDERS_ALL = API_URL_ORDERS + "/all";
export const API_URL_ORDERS_MINE = API_URL_ORDERS + "/mine";


export const ACCESS_TOKEN = 'accessToken';
export const ACTIVE_ROLE = 'activeRole';

export const REACT_APP_URL =  'https://frontend-7lhoj5kiqa-ey.a.run.app/';

export const OAUTH2_REDIRECT_URI = REACT_APP_URL + '/oauth2/redirect';

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;

export const ROLE_CLIENT = 'ROLE_CLIENT';
export const ROLE_EMPLOYEE = 'ROLE_EMPLOYEE';
export const ROLE_ADMIN = 'ROLE_ADMIN';