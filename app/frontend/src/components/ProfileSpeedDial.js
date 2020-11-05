import React, { useState } from "react";
import AuthenticationService from '../services/AuthenticationService';

import { makeStyles } from '@material-ui/core/styles';
import SpeedDial from '@material-ui/lab/SpeedDial';
import SpeedDialAction from '@material-ui/lab/SpeedDialAction';
import Button from '@material-ui/core/Button';
import Backdrop from '@material-ui/core/Backdrop';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import SettingsIcon from '@material-ui/icons/Settings';
import AccountBoxIcon from '@material-ui/icons/AccountBox';

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


function SpeedDialTooltipOpen({setUserIsAuthenticated, history}) {
  const classes = useStyles();
  const [open, setOpen] = React.useState(false);
  const [hidden, setHidden] = React.useState(false);

  const handleVisibility = () => {
    setHidden((prevHidden) => !prevHidden);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSpeedDialActionClick = (e, operation) => {
    e.preventDefault();
    if(operation == "handleProfile") {
    } else if(operation == "handleCurrentAccessLevel") {
    } else if(operation == "handleSignOut") {
        handleSignOut();
    }
  }

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
      <Button onClick={handleVisibility}></Button>
      <Backdrop open={open} />
      <SpeedDial
        ariaLabel="Profile Account SpeedDial"
        hidden={hidden}
        icon={<AccountCircleIcon className={classes.accountIcon}/>}
        onClose={handleClose}
        onOpen={handleOpen}
        open={open}
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
    </div>
  );
}
export default SpeedDialTooltipOpen;