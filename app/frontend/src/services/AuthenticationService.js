import axios from 'axios';

const API_URL = 'https://localhost:8181/api/auth/signin'

class AuthenticationService {
    signIn(username, password) {
        return axios.post(API_URL, {username, password })
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