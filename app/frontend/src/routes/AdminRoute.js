import React from "react";
import { Route } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import NotAuthorized from '../pages/NotAuthorized';
import { ROLE_ADMIN } from '../config/config';

function AdminRoute({ component: Component, ...rest }) {
    const {userIsAuthenticated, activeRole} = useAuth();
    
    if(userIsAuthenticated && activeRole === ROLE_ADMIN) {
        return (
            <Route
            {...rest}
            render = {props =>
                <Component {...props} />
            }
            />
        );
    } else {
        return (
            <Route component={NotAuthorized} />
        );
    }
}

export default AdminRoute;