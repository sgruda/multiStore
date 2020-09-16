import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "../pages/Home";
import SignIn from "../pages/SignIn"

export default function Routes() {
  return (
    <Switch>
      <Route exact path="/">
        <Home />
      </Route>
      <Route exact path="/signin">
        <SignIn />
      </Route>
    </Switch>
  );
}
