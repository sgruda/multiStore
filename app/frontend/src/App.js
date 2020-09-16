import React, { useState } from "react";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import PrivateRoute from './routes/PrivateRoute';
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import { AuthContext } from "./context/AuthContext";
import SignIn from "./pages/SignIn";
import SignUp from './pages/SignUp';
import Routes from './routes/Routes';



import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';

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

  const [authTokens, setAuthTokens] = useState();
  
  const setTokens = (data) => {
    localStorage.setItem("tokens", JSON.stringify(data));
    setAuthTokens(data);
  }

  return (
    <AuthContext.Provider value={{ authTokens, setAuthTokens: setTokens }}>
      {/* <Router>
        <div>
        <ul>
          <li>
            <Link to="/">Home Pagee</Link>
          </li>
          <li>
            <Link to="/admin">Admin Pagee</Link>
          </li>
        </ul>
          <Route exact path="/" component={Home} />
          <Route path="/signin" component={SignIn} />
          <Route path="/signup" component={SignUp} />
          <PrivateRoute path="/admin" component={Admin} />
        </div>
      </Router> */}
       <AppBar position="static">
          <Toolbar>
            <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
              <MenuIcon />
            </IconButton>
            <Typography variant="h6" className={classes.title}>
              EMPIK
            </Typography>
            <Button href="/signin" color="inherit">
              SignIn
            </Button>
          </Toolbar>
      </AppBar>
      <Routes />
    </AuthContext.Provider>
  );
}

export default App;
