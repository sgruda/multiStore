import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "../pages/Home";
import SignIn from "../pages/SignIn"
import Admin from "../pages/Admin"
import NotFound from "../pages/NotFound";

export default function Routes() {
  return (
    <Switch>
      <Route exact path="/">
          <Home />
      </Route>
      <Route exact path="/admin">
          <Admin />
      </Route>
      <Route exact path="/signin">
        <SignIn />
      </Route>
      <Route>
        <NotFound />
      </Route>
    </Switch>
  );
}
