import axios from "axios";
// import { useAuth } from "../../context/AuthContext";

const API_URL = 'https://localhost:8181/api/auth/';
// const {setUserIsAuthenticated} = useAuth();


export default function signUp(username, email, password) {
    return axios.post(API_URL + "signup", {
      username,
      email,
      password
    });
}