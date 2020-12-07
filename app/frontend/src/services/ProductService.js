import axios from 'axios'; 
import AuthorizationHeader from './AuthorizationHeader'; 
import {
    API_URL_PRODUCTS, API_URL_PRODUCT, API_URL_CREATE_PRODUCT
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
        return axios.get(API_URL_PRODUCT + '?title=' + title); 
      } 

      createProduct(fields) { 
        const data = {
          idHash: '0',
          title: fields.title,
          description: fields.description,
          inStore: fields.inStore,
          price: fields.price,
          type: fields.type,
          category: fields.category,
          signature: "0"
        };
        return axios.post(API_URL_CREATE_PRODUCT, data,  { headers: AuthorizationHeader() }); 
      }
} 
export default new ProductService(); 