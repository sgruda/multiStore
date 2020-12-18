import React from "react";
import { Route } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import NotAuthorized from '../pages/NotAuthorized';
import { ROLE_EMPLOYEE } from '../config/config';
import AuthenticationService from '../services/AuthenticationService';
import RedirectToSignIn from '../components/simple/RedirectToSignIn';

function EmployeeRoute({ component: Component, ...rest }) {
    const {userIsAuthenticated, activeRole} = useAuth();

    if(userIsAuthenticated && activeRole === ROLE_EMPLOYEE && !AuthenticationService.jwtIsExpired()) {
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

export default EmployeeRoute;