import React from 'react';
import { ACCESS_TOKEN } from '../config/config';
import { Redirect } from 'react-router-dom'
import { useAuth } from "../context/AuthContext";
import { useHistory } from "react-router-dom";
import Admin from '../pages/Admin';

function OAuth2RedirectHandler() {
    const getUrlParameter  = (name) => {
        
        // name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        // console.error("WTF + name " + name)
        // var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        // console.error("WTF + regex " + regex)
        // var results = regex.exec(this.props.location.search);
        // return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }
    
    const {setUserIsAuthenticated} = useAuth();
    const history = useHistory();
    // const token = getUrlParameter('token');
    // const error = getUrlParameter('error');  

    // if(token) {
        localStorage.setItem(ACCESS_TOKEN, "token");
        // history.push("/admin");
        setUserIsAuthenticated(true);
        return Admin;
    // } else {
    //     history.push("/login");
    //     setUserIsAuthenticated(false);
    // }
}

export default OAuth2RedirectHandler;