import React from "react";
import { Route } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import NotAuthorized from '../pages/NotAuthorized';
import { ROLE_ADMIN } from '../config/config';
import AuthenticationService from '../services/AuthenticationService';
import RedirectToSignIn from '../components/simple/RedirectToSignIn';

function AdminRoute({ component: Component, ...rest }) {
    const {userIsAuthenticated, activeRole} = useAuth();

    if(userIsAuthenticated && activeRole === ROLE_ADMIN && !AuthenticationService.jwtIsExpired()) {
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

export default AdminRoute;