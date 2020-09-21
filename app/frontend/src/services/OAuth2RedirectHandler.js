import React from 'react';
import { ACCESS_TOKEN } from '../config/config';
import { Redirect } from 'react-router-dom'
import { useAuth } from "../context/AuthContext";
import { useHistory } from "react-router-dom";

function OAuth2RedirectHandler() {
    const getUrlParameter  = (name) => {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

        var results = regex.exec(this.props.location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }
    
    const {setUserIsAuthenticated} = useAuth();
    const history = useHistory();
    const token = getUrlParameter('token');
    const error = getUrlParameter('error');  

    if(token) {
        localStorage.setItem(ACCESS_TOKEN, token);
        history.push("/admin");
        setUserIsAuthenticated(true);
    } else {
        history.push("/login");
        setUserIsAuthenticated(false);
    }
}

export default OAuth2RedirectHandler;