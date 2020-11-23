import React from "react";
import { Route, Switch } from "react-router-dom";
import PrivateRoute from './PrivateRoute';
import AdminRoute from './AdminRoute';
import OAuth2Redirect from '../components/OAuth2Redirect';
import Home from "../pages/Home";
import SignIn from "../pages/SignIn";
import SignUp from "../pages/SignUp";
import Admin from "../pages/Admin";
import NotFound from "../pages/NotFound";
import AccountsList from '../pages/AccountsList';
import UserProfile from '../pages/UserProfile';
import ResetPassword from '../pages/ResetPassword';
import MailVeryfication from '../pages/MailVeryfication';

export default function Routes() {
  return (
    <Switch>
      <Route exact path="/">
          <Home />
      </Route>
      <AdminRoute exact path="/admin/accountsList">
          <AccountsList />
      </AdminRoute>
      <PrivateRoute exact path="/profile">
          <UserProfile/>
      </PrivateRoute>
      <Route exact path="/signin">
        <SignIn />
      </Route>
      <Route exact path="/signup">
        <SignUp />
      </Route>
      <Route exact path="/reset-password">
        <ResetPassword/>
      </Route>
      <Route exact path="/oauth2/redirect">
        <OAuth2Redirect/>
      </Route>
      <Route  exact path="/verify-email">
        <MailVeryfication/>
      </Route>
      <Route>
        <NotFound />
      </Route>
    </Switch>
  );
}
