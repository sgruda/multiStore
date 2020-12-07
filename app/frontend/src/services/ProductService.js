import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_PRODUCTS, API_URL_GET_PRODUCT
} from '../config/config';

class ProductService { 
    getProducts(textToSearch, page, size, type, active) { 
      let API_URL_PRODUCTS_PAGINATION
        if(type != null) 
            API_URL_PRODUCTS_PAGINATION = API_URL_PRODUCTS + '?page=' + page + '&size=' + size + '&type=' + type;
        else
            API_URL_PRODUCTS_PAGINATION = API_URL_PRODUCTS + '?page=' + page + '&size=' + size;
        if(textToSearch == null && active == null) 
          return axios.get(API_URL_PRODUCTS_PAGINATION); 
        else if(textToSearch != null && active == null)
          return axios.get(API_URL_PRODUCTS_PAGINATION + '&textToSearch=' + textToSearch); 
        else if(textToSearch == null && active != null) 
          return axios.get(API_URL_PRODUCTS_PAGINATION + '&active=' + active);
        else if(textToSearch != null && active != null) 
          return axios.get(API_URL_PRODUCTS_PAGINATION + '&active=' + active + '&textToSearch=' + textToSearch);
      } 
      
      getProduct(title) { 
        return axios.get(API_URL_GET_PRODUCT + '?title=' + title); 
      } 
} 
export default new ProductService(); 