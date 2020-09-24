import React from "react";
import { Route, Switch } from "react-router-dom";
import PrivateRoute from './PrivateRoute';
import OAuth2RedirectRoute from './OAuth2RedirectRoute';
import Home from "../pages/Home";
import SignIn from "../pages/SignIn"
import SignUp from "../pages/SignUp"
import Admin from "../pages/Admin"
import NotFound from "../pages/NotFound";

export default function Routes() {
  return (
    <Switch>
      <Route exact path="/">
          <Home />
      </Route>
      <PrivateRoute exact path="/admin">
          <Admin />
      </PrivateRoute>
      <Route exact path="/signin">
        <SignIn />
      </Route>
      <Route exact path="/signup">
        <SignUp />
      </Route>
      <OAuth2RedirectRoute exact path="/oauth2/redirect">
        <Admin />
      </OAuth2RedirectRoute>
      <Route>
        <NotFound />
      </Route>
    </Switch>
  );
}
