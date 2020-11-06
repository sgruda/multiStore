import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {API_URL_ACCOUNTS, API_URL_SINGLE_ACCOUNT} from '../config/config';
 
class AccountService { 

  getAccounts() { 
    return axios.get(API_URL_ACCOUNTS, { headers: AuthorizationHeader() }); 
  } 
 
  getSingleAccount(email) { 
    return axios.get(API_URL_SINGLE_ACCOUNT + '?email=' + email, { headers: AuthorizationHeader() }); 
  } 
 
} 
 
export default new AccountService(); 