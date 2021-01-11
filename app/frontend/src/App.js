import React, { useState, useEffect } from "react";
import { useTranslation } from 'react-i18next';
import { Link, useHistory } from "react-router-dom";
import { AuthContext } from "./context/AuthContext";
import Routes from './routes/Routes';
import AuthenticationService from './services/AuthenticationService';
import RouterRedirectTo from './components/simple/RouterRedirectTo';
import {ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN, ACCESS_TOKEN, ACTIVE_ROLE} from './config/config';

import ProfileSpeedDial from "./components/ProfileSpeedDial";
import clsx from 'clsx';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import MailIcon from '@material-ui/icons/Mail';
import PeopleIcon from '@material-ui/icons/People';
import AddIcon from '@material-ui/icons/Add';
import MoneyOffIcon from '@material-ui/icons/MoneyOff';
import LibraryBooksIcon from '@material-ui/icons/LibraryBooks';

const drawerWidth = 240;
const appBarHeight = 80;
const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    marginTop: 90,
  },
  appbar : {
    height: appBarHeight,
    backgroundColor: "#4285F4",
    transition: theme.transitions.create(['margin', 'width'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
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
  },
  hide: {
    display: 'none',
  },
  drawer: {
    width: drawerWidth,
  },
  drawerPaper: {
    width: drawerWidth,
  },
  drawerHeader: {
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    height: appBarHeight,
    backgroundColor: "#4285F4",
    // // necessary for content to be below app bar
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
  },
  listItem: {
  },
  item: {
    '&:hover': {
      backgroundColor: '#7cc3eb'
    }
  },
}));


function App(props) {
  const classes = useStyles();
  const { t } = useTranslation();
  const theme = useTheme();
  const history = useHistory();

  const [userIsAuthenticated, setUserIsAuthenticated] = useState(false);
  const [activeRole, setActiveRole] = useState(undefined);
  const [currentAccessToken, setCurrentAccessToken] = useState(undefined);
  
  const [jwtExpiration, setJwtExpiration] = useState(false);

  const checkExpiredJWTAndExecute = (apiMethodToExecute) => {
    if(AuthenticationService.jwtIsExpired()) {
      setJwtExpiration(true);
    } else {
      apiMethodToExecute();
    }
  }

  useEffect(() => {
    const tokenInStorage = localStorage.getItem(ACCESS_TOKEN);
    if(tokenInStorage) {
      setCurrentAccessToken(tokenInStorage);
      setUserIsAuthenticated(true);
    }
    if (currentAccessToken) {
      if(AuthenticationService.jwtIsExpired()) {
        setJwtExpiration(true);
      } else {
        const activeRoleInStorege = localStorage.getItem(ACTIVE_ROLE);
        const roles = AuthenticationService.getParsedJWT(currentAccessToken).roles;
        if(activeRoleInStorege) {
          if(AuthenticationService.getParsedJWT(tokenInStorage).roles.includes(activeRoleInStorege)) {
            setActiveRole(activeRoleInStorege);
          }
        }
        else if(roles.includes(ROLE_CLIENT)) {
          setActiveRole(ROLE_CLIENT);
        }
        else if(roles.includes(ROLE_EMPLOYEE)) {
          setActiveRole(ROLE_EMPLOYEE);
        }
        else if(roles.includes(ROLE_ADMIN)) {
          setActiveRole(ROLE_ADMIN);
        }
      }
    }
  }, [currentAccessToken]);


  const [openDrawer, setOpenDrawer] = useState(false);
  const adminToolbarListItem = [
    { id: 'accountList', name: t('pages.titles.account.list'), path: '/admin/accountsList', icon: <PeopleIcon/>},
    { id: 'accountCreation', name: t('pages.titles.account.create'), path: '/admin/addAccount', icon:  <PersonAddIcon/>},
  ];
  const employeeToolbarListItem = [
    { id: 'productCreation', name: t('pages.titles.product.create'), path: '/employee/addProduct', icon:  <AddIcon/>},
    { id: 'promotionsList', name: t('pages.titles.promotion.list'), path: '/employee/promotionsList', icon:  <MoneyOffIcon/>},
    { id: 'promotionCreation', name: t('pages.titles.promotion.create'), path: '/employee/addPromotion', icon:  <AddIcon/>},
    { id: 'orderList', name: t('pages.titles.order.list'), path: '/employee/orders', icon:  <LibraryBooksIcon/>},

  ];

  return (
    // !currentAccessToken &&
    <div className={classes.root}>
      <AppBar
        position="fixed"//fixed/static to bedzie rowniej z ekranem, ale tak nie miesci sie logo przy rejestracji
        className={clsx(classes.appbar, {
          [classes.appBarShift]: openDrawer,
        })}
      >
       {/* <AppBar position="static" className={classes.appbar}> */}
          <Toolbar position="fixed">
            { ( userIsAuthenticated  &&  (activeRole === ROLE_ADMIN || activeRole === ROLE_EMPLOYEE) ) &&
              <IconButton edge="start" color="inherit" aria-label="menu"
                onClick={() => {setOpenDrawer(true);}} className={clsx(classes.menuButton, openDrawer && classes.hide)}>
                <MenuIcon />
              </IconButton>
            }
          
            <Typography variant="h6" className={classes.title} >
              <Button component={Link} to="/" color="inherit">Inżynierka</Button>
            </Typography>
              { userIsAuthenticated && activeRole === ROLE_ADMIN &&
                <></>
              }
              { userIsAuthenticated
                ? <>
                    <ProfileSpeedDial 
                      setUserIsAuthenticated={setUserIsAuthenticated} 
                      history={history}
                      activeRole={activeRole}
                      setActiveRole={setActiveRole}
                    />
                  </>
                : <>
                    <Button component={Link} to="/signin" color="inherit">{t('signin')}</Button>
                    <Button component={Link} to="/signup" color="inherit">{t('signup')}</Button>
                  </>
              }
          </Toolbar>
      </AppBar>
      <Drawer
        className={classes.drawer}
        variant="temporary"
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
        <List className={classes.listItem}>
          {adminToolbarListItem.map((item) => (
            <ListItem 
                className={classes.item}
                button 
                key={item.id} 
                onClick={() => history.push(item.path)}
            >
              <ListItemIcon>
                {item.icon}
              </ListItemIcon>
              <ListItemText primary={item.name} />
            </ListItem>
          ))}
        </List>
        }
        <Divider />
        { userIsAuthenticated  && activeRole === ROLE_EMPLOYEE &&
        <List>
         {employeeToolbarListItem.map((item) => (
            <ListItem 
                className={classes.item}
                button 
                key={item.id} 
                onClick={() => history.push(item.path)}
            >
              <ListItemIcon>
                {item.icon}
              </ListItemIcon>
              <ListItemText primary={item.name} />
            </ListItem>
          ))}
        </List>
        }
      </Drawer>
      <AuthContext.Provider value={{setCurrentAccessToken, userIsAuthenticated, setUserIsAuthenticated , activeRole, checkExpiredJWTAndExecute}}>
          <Routes />

          {jwtExpiration ? 
            <RouterRedirectTo 
                dialogContent={t('dialog.content.jwt-expired')}
                page="/signin"
              />
          :<></>}
      </AuthContext.Provider>
    </div>
  );
}

export default App;