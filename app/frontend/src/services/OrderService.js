import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_ORDER, API_URL_ORDER_CHANGE_STATUS, API_URL_ORDER_SUBMIT, API_URL_ORDER_TOTAL_PRICE, API_URL_ORDERS_MINE, API_URL_ORDERS_ALL
} from '../config/config';

class OrderService { 
    submit(basket, address) {
        const data = {
            basketDTO: basket,
            address: address
        };
        return axios.post(API_URL_ORDER_SUBMIT, data, { headers: AuthorizationHeader() });
    }
    getTotalPrice(basket) { 
        return axios.post(API_URL_ORDER_TOTAL_PRICE, basket, { headers: AuthorizationHeader() });
    } 

    getAllOrders(page, size) {
        return axios.get(API_URL_ORDERS_ALL + "?page=" + page + "&size=" + size, { headers: AuthorizationHeader() });
    }
    getClientOrders(page, size) {
        return axios.get(API_URL_ORDERS_MINE + "?page=" + page + "&size=" + size, { headers: AuthorizationHeader() });
    }
    changeStatus(order) {
        const data = {
            idHash: order.id,
            identifier: order.identifier,
            orderDate: order.orderDate,
            buyerEmail: order.buyerEmail,
            orderedItemDTOS: order.orderedItemDTOS,
            totalPrice: order.totalPrice,
            status: order.status,
            address: order.address,
            version: order.version,
            hash: order.hash
        };
        return axios.put(API_URL_ORDER_CHANGE_STATUS, data, { headers: AuthorizationHeader() });
    }
    getOrder(id) {
        return axios.get(API_URL_ORDER + "?id=" + id, { headers: AuthorizationHeader() });
    }

} 
export default new OrderService(); 