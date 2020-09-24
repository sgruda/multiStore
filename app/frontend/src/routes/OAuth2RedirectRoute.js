import React from "react";
import { ACCESS_TOKEN } from '../config/config';
import { Route, useParams, useLocation} from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import NotAuthorized from '../pages/NotAuthorized';

function OAuth2RedirectRoute({ component: Component, ...rest }) {
    const {setUserIsAuthenticated} = useAuth();
    let location = useLocation()
    const getUrlParameter  = (name) => {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        var results = regex.exec(location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }
    const token = getUrlParameter('token');
    const error = getUrlParameter('error');  

    if(token) {
        localStorage.setItem(ACCESS_TOKEN, token);
        setUserIsAuthenticated(true);
        return (
            <Route
                {...rest}
                render = {props =>
                    <Component {...props} />
                }
            />
        );
    } else {
        setUserIsAuthenticated(false);
        return (
            <Route component={NotAuthorized} />
        );
    }
}

export default OAuth2RedirectRoute;