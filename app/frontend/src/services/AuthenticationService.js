import axios from 'axios';
import { ACCESS_TOKEN } from '../config/config';
import jsonwebtoken from 'jsonwebtoken' ;
import {API_URL_SIGN_IN, API_URL_SIGN_UP} from '../config/config';


const getAccessTokenFromStorage = () => {
    return localStorage.getItem(ACCESS_TOKEN);
}
const getParsedJWT = () => {
    const jwt = require("jsonwebtoken");
    return jwt.decode(localStorage.getItem(ACCESS_TOKEN));
};

const signIn = (username, password) => {
    return axios
    .post(API_URL_SIGN_IN, {
        username, 
        password 
    })
    .then((response) => {
        if (response.status === 200 && response.data.accessToken) {
            localStorage.setItem(ACCESS_TOKEN, JSON.parse(JSON.stringify( response.data)).accessToken); 
        }
        return response.data;
    });
};
const signUp = (firstName, lastName, email, username, password) => {
    return axios
        .post(API_URL_SIGN_UP, {
            firstName, 
            lastName, 
            email, 
            username, 
            password 
        })
};
const signOut = () => {
    localStorage.removeItem(ACCESS_TOKEN);
    };



    
export default {
    getAccessTokenFromStorage,
    getParsedJWT,
    signIn,
    signUp,
    signOut,
};