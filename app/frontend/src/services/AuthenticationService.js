import axios from 'axios';
import { ACCESS_TOKEN } from '../config/config';
import jsonwebtoken from 'jsonwebtoken' 

const API_URL_SIGN_IN = 'https://localhost:8181/api/auth/signin'
const API_URL_SIGN_UP = 'https://localhost:8181/api/auth/signup'

const getAccessTokenFromStorage = () => {
    return localStorage.getItem(ACCESS_TOKEN);
}
const parseJWT = () => {
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
    parseJWT,
    signIn,
    signUp,
    signOut,
};