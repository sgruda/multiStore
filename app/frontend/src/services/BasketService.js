import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_BASKET, API_URL_BASKET_SIZE, API_URL_BASKET_ADD, API_URL_BASKET_REMOVE, API_URL_BASKET_ITEM_EDIT
} from '../config/config';

class BasketService { 
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
            hash: item.hash,
        }
        return axios.put(API_URL_BASKET_ITEM_EDIT, data, { headers: AuthorizationHeader() });
    }
    addToBasket(basket, item, orderedNumber) {
        const orderedItem = {
            idHash: "0",
            identifier: "0",
            orderedNumber: orderedNumber,
            orderedProduct: {
                idHash: item.idHash,
                title: item.title,
                description: item.description,
                inStore: item.inStore,
                price: item.price,
                type: item.type,
                category: item.category,
                version: item.version,
                hash: item.hash
            },
            version: 0,
            hash: "0",
        };
        const data = {
            basketDTO: basket,
            orderedItemDTO: orderedItem
        };
        return axios.put(API_URL_BASKET_ADD, data, { headers: AuthorizationHeader() });
    }

} 
export default new BasketService(); 