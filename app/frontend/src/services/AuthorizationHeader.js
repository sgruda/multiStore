import {ACCESS_TOKEN} from '../config/config';

export default function AuthorizationHeader() {
  const accessToken = localStorage.getItem(ACCESS_TOKEN);
  
    if (accessToken) {
      return { Authorization: 'Bearer ' + accessToken };
    } else {
      return {};
    }
  }
  