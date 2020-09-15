import axios from "axios";

const API_URL = 'https://localhost:8181/api/auth/'

const register = (username, email, password) => {
  return axios.post(API_URL + "signup", {
    firstname,
    lastname,
    username,
    email,
    password,
  });
};

const login = (username, password) => {
  return axios
    .post(API_URL + "signin", {
      username,
      password,
    })
    .then((response) => {
      if (response.data.accessToken) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }
      return response.data;
    });
};

const logout = () => {
  localStorage.removeItem("user");
};

function authHeader() {
    const user = JSON.parse(localStorage.getItem('user'));
  
    if (user && user.accessToken) {
      return { Authorization: 'Bearer ' + user.accessToken };
    } else {
      return {};
    }
  }
  

export default {
  register,
  login,
  logout,
  authHeader,
};
