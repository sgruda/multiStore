import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import { useHistory } from "react-router-dom";
import PrivateRoute from './routes/PrivateRoute';
import { AuthContext, useAuth } from "./context/AuthContext";
import Routes from './routes/Routes';
import AuthenticationService from './services/AuthenticationService';


import {ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN} from './config/config';


import clsx from 'clsx';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import { LinkSharp } from "@material-ui/icons";
import Drawer from '@material-ui/core/Drawer';
import CssBaseline from '@material-ui/core/CssBaseline';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import MailIcon from '@material-ui/icons/Mail';

const drawerWidth = 240;
const useStyles = makeStyles((theme) => ({
  appbar : {
    backgroundColor: "#4285F4",
    transition: theme.transitions.create(['margin', 'width'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
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
  appBarShift: {
    backgroundColor: "#4285F4",
    width: `calc(100% - ${drawerWidth}px)`,
    marginLeft: drawerWidth,
    // transition: theme.transitions.create(['margin', 'width'], {
    //   easing: theme.transitions.easing.easeOut,
    //   duration: theme.transitions.duration.enteringScreen,
    // }),
  },
  hide: {
    display: 'none',
  },
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
  },
  drawerPaper: {
    width: drawerWidth,
  },
  drawerHeader: {
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    // // necessary for content to be below app bar
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
  },
}));


function App(props) {
  const classes = useStyles();
  const theme = useTheme();
  const history = useHistory();

  const [userIsAuthenticated, setUserIsAuthenticated] = useState(false);
  const [activeRole, setActiveRole] = useState(undefined);
  const [currentAccessToken, setCurrentAccessToken] = useState(undefined);


  useEffect(() => {
    if (currentAccessToken) {
      const roles = AuthenticationService.parseJWT(currentAccessToken).roles;
      if(roles.includes(ROLE_CLIENT)) {
        setActiveRole(ROLE_CLIENT);
      }
      else if(roles.includes(ROLE_EMPLOYEE)) {
        setActiveRole(ROLE_EMPLOYEE);
      }
      else if(roles.includes(ROLE_ADMIN)) {
        setActiveRole(ROLE_ADMIN);
      }
    }
  }, [currentAccessToken]);
  
  const signOut = () => {
    AuthenticationService.signOut();
    setUserIsAuthenticated(false);
    history.push("/")
  }



  const [openDrawer, setOpenDrawer] = React.useState(false);

  return (
    // !currentAccessToken &&
    <div>
      <AppBar
        position="static"//fixed to bedzie rowniej z ekranem, ale tak nie miesci sie logo przy rejestracji
        className={clsx(classes.appbar, {
          [classes.appBarShift]: openDrawer,
        })}
      >
       {/* <AppBar position="static" className={classes.appbar}> */}
          <Toolbar>
            { ( userIsAuthenticated  &&  (activeRole === ROLE_ADMIN || activeRole === ROLE_EMPLOYEE) ) &&
              <IconButton edge="start" color="inherit" aria-label="menu"
                onClick={() => {setOpenDrawer(true);}} className={clsx(classes.menuButton, openDrawer && classes.hide)}>
                <MenuIcon />
              </IconButton>
            }
          
            <Typography variant="h6" className={classes.title} >
              <Button component={Link} to="/" color="inherit">EMPIK</Button>
            </Typography>
              { userIsAuthenticated && activeRole === ROLE_ADMIN &&
                <Button component={Link} to="/admin" color="inherit">AdminPage</Button>
              }
              { userIsAuthenticated
                ? <Button onClick={signOut} color="inherit">Sign out</Button>
                : <>
                    <Button component={Link} to="/signin" color="inherit">Sign in</Button>
                    <Button component={Link} to="/signup" color="inherit">Sign up</Button>
                  </>
              }
          </Toolbar>
      </AppBar>

      <Drawer
        className={classes.drawer}
        variant="persistent"
        anchor="left"
        open={openDrawer}
        classes={{
          paper: classes.drawerPaper,
        }}
      >
        <div className={classes.drawerHeader}>
          <IconButton onClick={() => {setOpenDrawer(false);}}>
            {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
          </IconButton>
        </div>
        <Divider />
        { userIsAuthenticated  && activeRole === ROLE_ADMIN &&
        <List>
          {['Inbox', 'Starred', 'Send email', 'Drafts'].map((text, index) => (
            <ListItem button key={text}>
              <ListItemIcon>{index % 2 === 0 ? <InboxIcon /> : <MailIcon />}</ListItemIcon>
              <ListItemText primary={text} />
            </ListItem>
          ))}
        </List>
        }
        <Divider />
        { userIsAuthenticated  && activeRole === ROLE_EMPLOYEE &&
        <List>
          {['All mail', 'Trash', 'Spam'].map((text, index) => (
            <ListItem button key={text}>
              <ListItemIcon>{index % 2 === 0 ? <InboxIcon /> : <MailIcon />}</ListItemIcon>
              <ListItemText primary={text} />
            </ListItem>
          ))}
        </List>
        }
      </Drawer>



      <AuthContext.Provider value={{setCurrentAccessToken, userIsAuthenticated, setUserIsAuthenticated }}>
        <Routes />
      </AuthContext.Provider>
    </div>
  );
}

export default App;