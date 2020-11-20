import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {API_URL_ACCOUNTS, API_URL_SINGLE_ACCOUNT, API_URL_MY_ACCOUNT, API_URL_MY_ACCOUNT_EDIT, 
  API_URL_MY_ACCOUNT_CHANGE_PASSWORD, API_URL_SINGLE_ACCOUNT_EDIT, API_URL_SINGLE_ACCOUNT_CHANGE_PASSWORD} from '../config/config';

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

  changeOwnPassword(account, newPassword) {
    const data = {'newPassword': newPassword,
                  'accountDTO': account};
    return axios.put(API_URL_MY_ACCOUNT_CHANGE_PASSWORD, data, { headers: AuthorizationHeader()});
  }
  changePassword(account, newPassword) {
    account.authenticationDataDTO.password = newPassword;
    return axios.put(API_URL_SINGLE_ACCOUNT_CHANGE_PASSWORD, account, { headers: AuthorizationHeader()});
  }
 
} 
export default new AccountService(); 