import axios from 'axios';
import { ACCESS_TOKEN } from '../config/config';

const API_URL_SIGN_IN = 'https://localhost:8181/api/auth/signin'
const API_URL_SIGN_UP = 'https://localhost:8181/api/auth/signup'

// class AuthenticationService {
    const signIn = (username, password) => {
        return axios
        .post(API_URL_SIGN_IN, {
            username, 
            password 
        })
        .then((response) => {
            if (response.status === 200 && response.data.accessToken) {
                console.info("WTF " + response.data.accessToken)
                localStorage.setItem(ACCESS_TOKEN, JSON.parse(JSON.stringify( response.data)).accessToken);              
            }
            return response.data;
        });
    };
    const signUp = (firstname, lastname, email, username, password) => {
        return axios
            .post(API_URL_SIGN_UP, {
                firstname, 
                lastname, 
                email, 
                username, 
                password 
            })
    };
    // const getCurrentUser = () => {
    //     return JSON.parse(localStorage.getItem(ACCESS_TOKEN));;
    // };
    const signOut = () => {
        localStorage.removeItem(ACCESS_TOKEN);
      };
    export default {
        signIn,
        signUp,
        // getCurrentUser,
        signOut,
      };
// }

// export default new AuthenticationService();