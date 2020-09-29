import React from "react";
import { ACCESS_TOKEN } from '../config/config';

const getUrlParameter  = (name, location) => {
    // name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    // let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    let regex = new RegExp(name);
    let results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

const getAccessTokenFromResponse  = (data) => {
    return JSON.parse(JSON.stringify(data)).accessToken;
}

export default {
    getUrlParameter,
    getAccessTokenFromResponse,
  };