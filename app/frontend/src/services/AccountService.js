import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {API_URL_ACCOUNTS, API_URL_SINGLE_ACCOUNT, API_URL_MY_ACCOUNT, API_URL_MY_ACCOUNT_EDIT, 
  API_URL_MY_ACCOUNT_CHANGE_PASSWORD, API_URL_SINGLE_ACCOUNT_EDIT, API_URL_SINGLE_ACCOUNT_CHANGE_PASSWORD,
  API_URL_SINGLE_ACCOUNT_ADD_ACCESS_LEVEL, API_URL_SINGLE_ACCOUNT_REMOVE_ACCESS_LEVEL,
  API_URL_SINGLE_ACCOUNT_BLOCK, API_URL_SINGLE_ACCOUNT_UNBLOCK, API_URL_SINGLE_ACCOUNT_RESEND_CONFIRM_MAIL,
  API_URL_SINGLE_ACCOUNT_REMOVE, API_URL_RESET_PASSWORD, API_URL_CHANGE_RESETTED_PASSWORD,
  API_URL_VERIFY_EMAIL, API_URL_SINGLE_ACCOUNT_CREATE, API_URL_MY_ACCOUNT_CHANGE_LANGUAGE } from '../config/config';

class AccountService { 

  getAccounts(textToSearch, page, size, sort, active) { 
    const API_URL_ACCOUNTS_PAGINATION = API_URL_ACCOUNTS + '?page=' + page + '&size=' + size + '&sort=' + sort;
    if(textToSearch == null && active == null)
      return axios.get(API_URL_ACCOUNTS_PAGINATION, { headers: AuthorizationHeader() }); 
    else if(textToSearch != null && active == null)
      return axios.get(API_URL_ACCOUNTS_PAGINATION + '&textToSearch=' + textToSearch, { headers: AuthorizationHeader() }); 
    else if(textToSearch == null && active != null)
      return axios.get(API_URL_ACCOUNTS_PAGINATION + '&active=' + active, { headers: AuthorizationHeader() });
    else if(textToSearch != null && active != null)
      return axios.get(API_URL_ACCOUNTS_PAGINATION + '&active=' + active + '&textToSearch=' + textToSearch, { headers: AuthorizationHeader() });

  } 
 
  getSingleAccount(email) { 
    return axios.get(API_URL_SINGLE_ACCOUNT + '?email=' + email, { headers: AuthorizationHeader() }); 
  } 

  getUserAccount() { 
    return axios.get(API_URL_MY_ACCOUNT, { headers: AuthorizationHeader() }); 
  }

  editUserAccount(account) {
    return axios.put(API_URL_MY_ACCOUNT_EDIT, account, { headers: AuthorizationHeader()});
  }

  editAccount(account) {
    return axios.put(API_URL_SINGLE_ACCOUNT_EDIT, account, { headers: AuthorizationHeader()});
  }

  removeSingleAccount(account) {  
    return axios.post(API_URL_SINGLE_ACCOUNT_REMOVE, account, { headers: AuthorizationHeader() }); 
  } 

  changeOwnPassword(account, newPassword) {
    const data = {'newPassword': newPassword,
                  'accountDTO': account};
    return axios.put(API_URL_MY_ACCOUNT_CHANGE_PASSWORD, data, { headers: AuthorizationHeader()});
  }

  changePassword(account, newPassword) {
    account.authenticationDataDTO.password = newPassword;
    return axios.put(API_URL_SINGLE_ACCOUNT_CHANGE_PASSWORD, account, { headers: AuthorizationHeader()});
  }

  resetPassword(email) {
    const language = navigator.language || navigator.userLanguage; 
    return axios.put(API_URL_RESET_PASSWORD + "?email=" + email + "&lang=" + language, '');
  }
  changeResettedPassword(password, token) {
    const data = {
        'password': password,
        'resetPasswordToken': token
    };
    return axios.put(API_URL_CHANGE_RESETTED_PASSWORD, data);
  }

  addAccessLevel(account, roles) {
    account.roles = roles;
    return axios.put(API_URL_SINGLE_ACCOUNT_ADD_ACCESS_LEVEL, account, { headers: AuthorizationHeader()});
  }
  removeAccessLevel(account, roles) {
    account.roles = roles;
    return axios.put(API_URL_SINGLE_ACCOUNT_REMOVE_ACCESS_LEVEL, account, { headers: AuthorizationHeader()});
  }

 
  block(account) {
    return axios.put(API_URL_SINGLE_ACCOUNT_BLOCK, account, { headers: AuthorizationHeader()});
  }
  unblock(account) {
    return axios.put(API_URL_SINGLE_ACCOUNT_UNBLOCK, account, { headers: AuthorizationHeader()});
  }

  sendMail(email) {
    return axios.put(API_URL_SINGLE_ACCOUNT_RESEND_CONFIRM_MAIL + '?email=' + email, '', { headers: AuthorizationHeader() }); 
  }

  verifyEmail(token) {
    return axios.post(API_URL_VERIFY_EMAIL + '?token=' + token, '',  { headers: AuthorizationHeader() }); 
  }

  changeLanguage(account) {
    return axios.put(API_URL_MY_ACCOUNT_CHANGE_LANGUAGE, account, { headers: AuthorizationHeader()});
  }

  createAccount(fields, roles) { 
    const data = {
      'firstName': fields.firstName,
      'lastName': fields.lastName,
      'email': fields.email,
      'username': fields.username,
      'password': fields.password,
      'roles': roles,
      "language": fields.language
    };
    return axios.post(API_URL_SINGLE_ACCOUNT_CREATE, data,  { headers: AuthorizationHeader() }); 
  }

} 
export default new AccountService(); 