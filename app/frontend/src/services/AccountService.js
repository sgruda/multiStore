import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {API_URL_ACCOUNTS, API_URL_SINGLE_ACCOUNT, API_URL_MY_ACCOUNT} from '../config/config';
 
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
 
} 
 
export default new AccountService(); 