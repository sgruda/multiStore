import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_PROMOTIONS, API_URL_CREATE_PROMOTION, API_URL_DELETE_PROMOTION, API_URL_BLOCK_PROMOTION, API_URL_UNBLOCK_PROMOTION
} from '../config/config';

class PromotionService { 
    getPromotions(page, size) { 
        return axios.get(API_URL_PROMOTIONS + '?page=' + page + '&size=' + size,  { headers: AuthorizationHeader() });
    } 

    createPromotion(fields) { 
        const data = {
            idHash: '0',
            name: fields.name,
            discount: fields.discount,
            onCategory: fields.onCategory,
            signature: "0"
        };
    return axios.post(API_URL_CREATE_PROMOTION, data,  { headers: AuthorizationHeader() }); 
    }

    block(promotion) {
        return axios.put(API_URL_BLOCK_PROMOTION, promotion, { headers: AuthorizationHeader()});
    }
    unblock(promotion) {
        return axios.put(API_URL_UNBLOCK_PROMOTION, promotion, { headers: AuthorizationHeader()});
    }
    delete(promotion) {
        return axios.delete(API_URL_DELETE_PROMOTION, promotion, { headers: AuthorizationHeader()});
    }
} 
export default new PromotionService(); 