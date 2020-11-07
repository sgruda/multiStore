import React from "react";
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
import { Group } from "@material-ui/icons";

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
  const [openSpeedDial, setOpenSpeedDial] = React.useState(false);
  const [hiddenSpeedDial, setHiddenSpeedDial] = React.useState(false);

  const [anchorElPopper, setAnchorElPopper] = React.useState(null);
  const [openPopper, setOpenPopper] = React.useState(false);

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
    if(operation == "handleProfile") {
        handleProfile();
    } else if(operation == "handleCurrentAccessLevel") {
      handleCurrentAccessLevel(e);
    } else if(operation == "handleSignOut") {
        handleSignOut();
    }
  }

  const handleProfile = () => {

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
    { icon: <AccountBoxIcon className={classes.actionIcon} />, name: 'Profile', operation: "handleProfile"},
    { icon: <SettingsIcon className={classes.actionIcon}/>, name: 'Current access level', operation: "handleCurrentAccessLevel"},
    { icon: <ExitToAppIcon className={classes.actionIcon}/>, name: 'Sign out',  operation: "handleSignOut"},
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
                {actions.map((action) => (
                <SpeedDialAction 
                    key={action.name}
                    icon={action.icon}
                    tooltipTitle={action.name}
                    onClick={(e) => {
                        handleSpeedDialActionClick(e, action.operation);
                }}
                />
                ))}
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