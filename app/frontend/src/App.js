import React, { useState } from "react";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import PrivateRoute from './routes/PrivateRoute';
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import { AuthContext } from "./context/AuthContext";
import SignIn from "./pages/SignIn";
import SignUp from './pages/SignUp';
import Routes from './routes/Routes';
import AuthenticationService from './services/AuthenticationService';

import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import { LinkSharp } from "@material-ui/icons";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
}));


function App(props) {
  const classes = useStyles();

  // const [authTokens, setAuthTokens] = useState();
  const [userIsAuthenticated, setUserIsAuthenticated] = useState(false);

  // const setTokens = (data) => {
  //   localStorage.setItem("tokens", JSON.stringify(data));
  //   setAuthTokens(data);
  // }
  const signOut = () => {
    localStorage.removeItem("user");
    setUserIsAuthenticated(false);
  }
  return (
    <div>
       <AppBar position="static">
          <Toolbar>
            <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
              <MenuIcon />
            </IconButton>
            <Typography variant="h6" className={classes.title} >
              <Button component={Link} to="/" color="inherit">
                EMPIK
              </Button>
            </Typography>

            { userIsAuthenticated
              ? <Button onClick={signOut} color="inherit">Sign out</Button>
              : <>
              <Button component={Link} to="/signin" color="inherit">
                Sign in
              </Button>
              <Button component={Link} to="/signup" color="inherit">
                Sign up
              </Button>
              </>
            }
          </Toolbar>
      </AppBar>
      <AuthContext.Provider value={{ userIsAuthenticated, setUserIsAuthenticated }}>
        <Routes />
      </AuthContext.Provider>
    </div>
  );
}

export default App;
