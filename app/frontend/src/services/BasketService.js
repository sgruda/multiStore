import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_BASKET, API_URL_BASKET_SIZE, API_URL_BASKET_ADD, API_URL_BASKET_REMOVE
} from '../config/config';

class BasketService { 
    getBasket() { 
        return axios.get(API_URL_BASKET,  { headers: AuthorizationHeader() });
    } 
    getBasketSize() { 
        return axios.get(API_URL_BASKET_SIZE,  { headers: AuthorizationHeader() });
    } 
} 
export default new BasketService(); 