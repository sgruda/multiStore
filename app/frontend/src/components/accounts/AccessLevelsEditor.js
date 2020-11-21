import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useFields } from '../../hooks/FieldHook';
import { useForm } from "react-hook-form";
import AccountService from '../../services/AccountService';
import ConfirmDialog from '../ConfirmDialog';
import AcceptButtons from '../AcceptButtons';
import AlertApiResponseHandler from '../AlertApiResponseHandler';
import { ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN } from '../../config/config';

import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import { Button } from '@material-ui/core';


const useStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(3),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      },
      avatar: {
        backgroundColor: theme.palette.primary.main,
      },
      form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(2),
      },
      buttonEdit: {
        backgroundColor: "#51c953",
        "&:hover": {
          backgroundColor: "#0bb00d"
        }
      },
      buttonCancel: {
        backgroundColor: "#e35656",
        "&:hover": {
          backgroundColor: "#eb1e1e"
        }
      },
      buttonRefresh: {
        backgroundColor: "#4285F4",
        "&:hover": {
          backgroundColor: "#2c0fab"
        }
      },
}));

function AccessLevelsEditor({account, handleClose, apiMethod, operationTitle}) {
  const classes = useStyles();
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [showRefresh, setShowRefresh] = useState(false);
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);

  const [roleClient, setRoleClient] = useState(false);
  const [roleEmployee, setRoleEmployee] = useState(false);
  const [roleAdmin, setRoleAdmin] = useState(false);

  const handleConfirmDialog = () => {
    setOpenConfirmDialog(!openConfirmDialog);
  }

  const handleAddAccessLevels = () => {
    // account.firstName = fields.firstName;
    // account.lastName = fields.lastName;
    // editAccount();
    handleConfirmDialog();
  }

  const initRoles = () => {
    if(account.roles.includes(ROLE_CLIENT))
        setRoleClient(true);
    if(account.roles.includes(ROLE_EMPLOYEE))
        setRoleEmployee(true);
    if(account.roles.includes(ROLE_ADMIN))
        setRoleAdmin(true);
  }

  const convertRolesToList = () => {
    let roles;
    if(roleClient)
      roles.add(ROLE_CLIENT);
    if(roleEmployee)
      roles.add(', ' + ROLE_EMPLOYEE);
    if(roleAdmin)
      roles.add(', ' + ROLE_ADMIN);
    return roles;
  }

  async function apiMethodExecution() {
    const roles = convertRolesToList();
    console.log("Roles " + roles)
    await apiMethod(account, roles)
    .then(response => {
        if (response.status === 200) { 
            setAlertInfoMessage('response.data');
            setOpenSuccessAlert(true);
        }
    },
        (error) => {
        const resMessage =
            (error.response && error.response.data && error.response.data.message) 
            || error.message || error.toString();
            console.error("AccessLevelsEditor: " + resMessage);
            setAlertWarningMessage(error.response.data.message.toString());
            setOpenWarningAlert(true);
            setShowRefresh(true);
        }
    );
  }

  useEffect(() => {
    initRoles();
  }, []);

  return (
    <div>
        <Container component="main" maxWidth="xs" >
        <CssBaseline />
        <div className={classes.paper}>
             <Avatar className={classes.avatar}>
                <AccountBoxIcon fontSize="medium"/>
            </Avatar>
            <Typography component="h1" variant="h5">
                {operationTitle} access level
            </Typography>
            
            <Button onClick={apiMethodExecution}>Click</Button>

            <AlertApiResponseHandler
              openWarningAlert={openWarningAlert}
              setOpenWarningAlert={setOpenWarningAlert}
              openSuccessAlert={openSuccessAlert}
              setOpenSuccessAlert={setOpenSuccessAlert}
              alertWarningMessage={alertWarningMessage}
              alertInfoMessage={alertInfoMessage}
            />
            <AcceptButtons
              submitButtonTitle={operationTitle}
              handleClose={handleClose}
              showRefreshButton={showRefresh}
            />
            <ConfirmDialog
              openConfirmDialog={openConfirmDialog}
              setOpenConfirmDialog={setOpenConfirmDialog}
              handleConfirmAction={handleAddAccessLevels}
            />
            </div>
        </Container>
    </div >
  );
}
export default AccessLevelsEditor;