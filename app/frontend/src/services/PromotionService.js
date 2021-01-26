import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_PROMOTIONS, API_URL_CREATE_PROMOTION, API_URL_DELETE_PROMOTION, API_URL_BLOCK_PROMOTION, API_URL_UNBLOCK_PROMOTION
} from '../config/config';

class PromotionService { 
    getPromotions(page, size) { 
        return axios.get(API_URL_PROMOTIONS + '?page=' + page + '&size=' + size,  { headers: AuthorizationHeader() });
    } 

    createPromotion(fields, active) { 
        const data = {
            idHash: '0',
            name: fields.name,
            discount: fields.discount,
            onCategory: fields.onCategory,
            active: active,
            expireDate: fields.expireDate + "T00:00:00",
            version: '0',
            signature: '0'
        };
        return axios.post(API_URL_CREATE_PROMOTION, data,  { headers: AuthorizationHeader() }); 
    }

    block(promotion) {
        const data = {
            idHash: promotion.id,
            name: promotion.name,
            discount: promotion.discount,
            onCategory: promotion.onCategory,
            active: promotion.active,
            expireDate: promotion.expireDate,
            version: promotion.version,
            signature: promotion.signature
        };
        return axios.put(API_URL_BLOCK_PROMOTION, data, { headers: AuthorizationHeader()});
    }
    unblock(promotion) {
        const data = {
            idHash: promotion.id,
            name: promotion.name,
            discount: promotion.discount,
            onCategory: promotion.onCategory,
            active: promotion.active,
            expireDate: promotion.expireDate,
            version: promotion.version,
            signature: promotion.signature
        };
        return axios.put(API_URL_UNBLOCK_PROMOTION, data, { headers: AuthorizationHeader()});
    }
    delete(promotion) {
        const data = {
            idHash: promotion.id,
            name: promotion.name,
            discount: promotion.discount,
            onCategory: promotion.onCategory,
            active: promotion.active,
            expireDate: promotion.expireDate,
            version: promotion.version,
            signature: promotion.signature
        };
        return axios.post(API_URL_DELETE_PROMOTION, data, { headers: AuthorizationHeader()});
    }
} 
export default new PromotionService(); 