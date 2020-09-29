import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import { useHistory } from "react-router-dom";
import PrivateRoute from './routes/PrivateRoute';
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import { AuthContext } from "./context/AuthContext";
import SignIn from "./pages/SignIn";
import SignUp from './pages/SignUp';
import Routes from './routes/Routes';
import AuthenticationService from './services/AuthenticationService';

import { ACCESS_TOKEN } from './config/config';

import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import { LinkSharp } from "@material-ui/icons";

const useStyles = makeStyles((theme) => ({
  appbar : {
    backgroundColor: "#4285F4"
  },
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
  const [userIsAuthenticated, setUserIsAuthenticated] = useState(false);
  const history = useHistory();

  // const [showModeratorBoard, setShowModeratorBoard] = useState(false);
  // const [showAdminBoard, setShowAdminBoard] = useState(false);
  const [currentAccessToken, setCurrentAccessToken] = useState(undefined);


  useEffect(() => {
    const accessToken = localStorage.getItem(ACCESS_TOKEN);
    if (accessToken) {
      setCurrentAccessToken(accessToken);
      // setShowModeratorBoard(user.roles.includes("ROLE_MODERATOR"));
      // setShowAdminBoard(user.roles.includes("ROLE_ADMIN"));
    }
  }, []);
  
  const signOut = () => {
    AuthenticationService.signOut();
    setUserIsAuthenticated(false);
    history.push("/")
  }
  return (
    !currentAccessToken &&
    <div>
       <AppBar position="static" className={classes.appbar}>
          <Toolbar>
            <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
              <MenuIcon />
            </IconButton>
            <Typography variant="h6" className={classes.title} >
              <Button component={Link} to="/" color="inherit">
                EMPIK
              </Button>
            </Typography>

            { userIsAuthenticated &&
              <Button component={Link} to="/admin" color="inherit">
                AdminPage
              </Button>
            }

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
