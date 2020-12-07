import React from "react";
import { Route, Switch } from "react-router-dom";
import PrivateRoute from './PrivateRoute';
import AdminRoute from './AdminRoute';
import EmployeeRoute from './EmployeeRoute';
import OAuth2Redirect from '../components/OAuth2Redirect';
import ProductList from '../pages/products/ProductList';
import SignIn from "../pages/SignIn";
import SignUp from "../pages/SignUp";
import NotFound from "../pages/NotFound";
import AccountsList from '../pages/accounts/AccountsList';
import AddAccount from '../pages/accounts/AddAccount';
import UserProfile from '../pages/accounts/UserProfile';
import ProductAdd from '../pages/products/ProductAdd';
import ResetPassword from '../pages/ResetPassword';
import MailVeryfication from '../pages/MailVeryfication';

export default function Routes() {
  return (
    <Switch>
      <Route exact path="/">
          <ProductList />
      </Route>
      <AdminRoute exact path="/admin/accountsList">
          <AccountsList />
      </AdminRoute>
      <AdminRoute exact path="/admin/addAccount">
          <AddAccount />
      </AdminRoute>
      <EmployeeRoute exact path="/employee/addProduct">
          <ProductAdd />
      </EmployeeRoute>
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
