import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_BASKET, API_URL_BASKET_SIZE, API_URL_BASKET_ADD, API_URL_BASKET_REMOVE, API_URL_BASKET_ITEM_EDIT
} from '../config/config';

class BasketService { 
    removeItemOnce(arr, value) {
        var index = arr.indexOf(value);
        if (index > -1) {
          arr.splice(index, 1);
        }
        return arr;
    }


    getBasket() { 
        return axios.get(API_URL_BASKET,  { headers: AuthorizationHeader() });
    } 
    getBasketSize() { 
        return axios.get(API_URL_BASKET_SIZE,  { headers: AuthorizationHeader() });
    } 
    deleteItemFromBasket(basket, itemToDelete) {
        basket.orderedItemDTOS.splice(itemToDelete, 1);
        return axios.put(API_URL_BASKET_REMOVE, basket, { headers: AuthorizationHeader() });
    }
    editItemInBasket(item) {
        const data = {
            idHash: item.id,
            identifier: item.identifier,
            orderedNumber: item.orderedNumber,
            orderedProduct: item.orderedProduct,
            version: item.version,
            signature: item.signature,
        }
        return axios.put(API_URL_BASKET_ITEM_EDIT, data, { headers: AuthorizationHeader() });
    }
} 
export default new BasketService(); 