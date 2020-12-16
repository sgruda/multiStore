import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_ORDER, API_URL_ORDER_CHANGE_STATUS, API_URL_ORDER_SUBMIT, API_URL_ORDER_TOTAL_PRICE, API_URL_ORDERS_MINE, API_URL_ORDERS_ALL
} from '../config/config';

class OrderService { 
    getTotalPrice(basket) { 
        return axios.post(API_URL_ORDER_TOTAL_PRICE, basket, { headers: AuthorizationHeader() });
    } 

} 
export default new OrderService(); 