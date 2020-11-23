import React from "react";
import { ACCESS_TOKEN } from '../config/config';
import { useHistory} from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function OAuth2Redirect(){
    const {setUserIsAuthenticated} = useAuth();
    const history = useHistory();
    const getUrlParameter  = (name) => {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        var results = regex.exec(history.location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }
    const token = getUrlParameter('token');
    const error = getUrlParameter('error');  
    history.replace("", null);
    if(token) {
        localStorage.setItem(ACCESS_TOKEN, token);
        setUserIsAuthenticated(true);
        history.push("/");
    } else {
        setUserIsAuthenticated(false);
    }
    return (<div></div>);
}

export default OAuth2Redirect;