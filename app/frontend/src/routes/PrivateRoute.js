import React from "react";
import { Route } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import NotAuthorized from '../pages/NotAuthorized';

function PrivateRoute({ component: Component, ...rest }) {
  const {userIsAuthenticated} = useAuth();

  if(userIsAuthenticated) {
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

export default PrivateRoute;