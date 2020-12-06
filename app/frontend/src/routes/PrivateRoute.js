import React from "react";
import { Route } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import NotAuthorized from '../pages/NotAuthorized';
import AuthenticationService from '../services/AuthenticationService';
import RedirectToSignIn from '../components/simple/RedirectToSignIn';

function PrivateRoute({ component: Component, ...rest }) {
    const {userIsAuthenticated} = useAuth();

    if(userIsAuthenticated && !AuthenticationService.jwtIsExpired()) {
        return (
            <Route
            {...rest}
            render = {props =>
                <Component {...props} />
            }
            />
        );
    } else if(AuthenticationService.jwtIsExpired()) {
        return (
            <Route component={RedirectToSignIn} />
        );    
    } else {
        return (
            <Route component={NotAuthorized} />
        );
    }
}

export default PrivateRoute;