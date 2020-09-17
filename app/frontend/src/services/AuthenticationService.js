import axios from 'axios';

const API_URL_SIGN_IN = 'https://localhost:8181/api/auth/signin'
const API_URL_SIGN_UP = 'https://localhost:8181/api/auth/signup'

class AuthenticationService {
    signIn(username, password) {
        return axios.post(API_URL_SIGN_IN, {username, password })
    }
    signUp(firstname, lastname, email, username, password) {
        return axios.post(API_URL_SIGN_UP, {firstname, lastname, email, username, password })
    }
    saveTokenJWT(response) {
        if (response.status === 200 && response.data.accessToken) {
            localStorage.setItem("user", JSON.stringify(response.data));
            return true;
          }
        return false;
    }
    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));;
    }

}

export default new AuthenticationService();