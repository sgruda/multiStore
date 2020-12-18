import React, {useState} from "react";
import { useTranslation } from 'react-i18next';
import AuthenticationService from '../services/AuthenticationService';
import CurrentRoleChanger from './CurrentRoleChanger';

import { makeStyles } from '@material-ui/core/styles';
import SpeedDial from '@material-ui/lab/SpeedDial';
import SpeedDialAction from '@material-ui/lab/SpeedDialAction';
import Button from '@material-ui/core/Button';
import Backdrop from '@material-ui/core/Backdrop';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import SettingsIcon from '@material-ui/icons/Settings';
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import Grid from '@material-ui/core/Grid';
import Container from '@material-ui/core/Container';
import LibraryBooksIcon from '@material-ui/icons/LibraryBooks';

import Popper from '@material-ui/core/Popper';
import Fade from '@material-ui/core/Fade';
import Paper from '@material-ui/core/Paper';

const useStyles = makeStyles((theme) => ({
  root: {
    height: 90,
    fontsize: 120,
  },
  accountIcon: {
    fontSize: 30,
  },
  actionIcon: {
    fontsize: 15,
  },
}));


function SpeedDialTooltipOpen({setUserIsAuthenticated, history, activeRole, setActiveRole}) {
  const classes = useStyles();
  const { t } = useTranslation();
  const [openSpeedDial, setOpenSpeedDial] = useState(false);
  const [hiddenSpeedDial, setHiddenSpeedDial] = useState(false);

  const [anchorElPopper, setAnchorElPopper] = useState(null);
  const [openPopper, setOpenPopper] = useState(false);

  const handleVisibilitySpeedDial = () => {
    setHiddenSpeedDial((prevHidden) => !prevHidden);
    // setHiddenSpeedDial(false);
  };
  const handleOpenSpeedDial = () => {
    setOpenSpeedDial(true);
  };
  const handleCloseSpeedDial = () => {
    setOpenSpeedDial(false);
  };

  const handleSpeedDialActionClick = (e, operation) => {
    e.preventDefault();
    if(operation === "handleProfile") {
        handleProfile();
    } else if(operation === "handleCurrentAccessLevel") {
      handleCurrentAccessLevel(e);
    } else if(operation === "handleSignOut") {
        handleSignOut();
    } else if(operation === "handleOrderList") {
      handleOrderList();
  }
  }

  const handleOrderList = () => {
    setOpenSpeedDial(false);
    history.push("/orders");
  }
  const handleProfile = () => {
    setOpenSpeedDial(false);
    history.push("/profile")
  };
  const handleCurrentAccessLevel = (event) => {
    setAnchorElPopper(event.currentTarget);
    setOpenPopper(!openPopper);
  };
  const handleClosePopper = (event) => {
    handleCurrentAccessLevel(event);
    handleCloseSpeedDial();
  };
  const handleSignOut = () => {
        AuthenticationService.signOut();
        setUserIsAuthenticated(false);
        history.push("/")
  };

  const actions = [
    { icon: <AccountBoxIcon className={classes.actionIcon} />, name: t('pages.titles.profile'), operation: "handleProfile", role: "all"},
    { icon: <LibraryBooksIcon className={classes.actionIcon}/>, name: t('pages.titles.order.list'), operation: "handleOrderList", role: "ROLE_CLIENT"},
    { icon: <SettingsIcon className={classes.actionIcon}/>, name: t('pages.titles.account.access-level.current'), operation: "handleCurrentAccessLevel", role: "all"},
    { icon: <ExitToAppIcon className={classes.actionIcon}/>, name: t('pages.titles.signout'),  operation: "handleSignOut", role: "all"},
  ];

  return (
    <div className={classes.root}>
    <Container component="main" maxWidth="xs" position="fixed">
        <Grid container>
            <Grid item>
            <Button onClick={handleVisibilitySpeedDial}></Button>
            <Backdrop open={openSpeedDial} />
            <SpeedDial
                ariaLabel="Profile Account SpeedDial"
                hidden={hiddenSpeedDial}
                icon={<AccountCircleIcon className={classes.accountIcon}/>}
                onClose={handleCloseSpeedDial}
                onOpen={handleOpenSpeedDial}
                open={openSpeedDial}
                direction="down"
            >
                {actions.map((action) => {
                  if(action.role === activeRole || action.role === "all") {
                   return (
                    <SpeedDialAction 
                      key={action.name}
                      icon={action.icon}
                      tooltipTitle={action.name}
                      onClick={(e) => {
                          handleSpeedDialActionClick(e, action.operation);
                      }}
                    />
                   );
                  }
              })}
            </SpeedDial>
            <Grid item>
                  <Popper open={openPopper} anchorEl={anchorElPopper} placement="left" transition>
                    {({ TransitionProps }) => (
                      <Fade {...TransitionProps} timeout={350}>
                        <Paper>
                          <CurrentRoleChanger
                            currentActiveRole={activeRole}
                            setCurrentActiveRole={setActiveRole}
                            handleClosePopper={handleClosePopper}
                          />
                        </Paper>
                      </Fade>
                    )}
                  </Popper>
                </Grid>
            </Grid>
      </Grid>
    </Container>
    </div>
    );
}
export default SpeedDialTooltipOpen;