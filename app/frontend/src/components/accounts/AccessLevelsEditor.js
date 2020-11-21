import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useForm } from "react-hook-form";
import ConfirmDialog from '../ConfirmDialog';
import AcceptButtons from '../AcceptButtons';
import AlertApiResponseHandler from '../AlertApiResponseHandler';
import { ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN } from '../../config/config';

import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';

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

function AccessLevelsEditor({account, handleClose, apiMethod, operationTitle, clientRole, employeeRole, adminRole, setClientRole, setEmployeeRole, setAdminRole}) {
  const classes = useStyles();
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [showRefresh, setShowRefresh] = useState(false);
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const {handleSubmit } = useForm({mode: "onSubmit"}); 
  
  const handleConfirmDialog = () => {
    setOpenConfirmDialog(!openConfirmDialog);
  }

  const handleEditAccessLevels = () => {
    apiMethodExecution();
    handleConfirmDialog();
  }

  const convertRolesToList = () => {
    let rolesArray = [];
    if(clientRole)
      rolesArray.push(ROLE_CLIENT);
    if(employeeRole)
      rolesArray.push(ROLE_EMPLOYEE);
    if(adminRole)
      rolesArray.push(ROLE_ADMIN);
    return rolesArray;
  }

  async function apiMethodExecution() {
    const roles = convertRolesToList();
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
            <form className={classes.formControl} noValidate onSubmit={handleSubmit(handleConfirmDialog)}>
              <FormGroup>
                <FormControlLabel
                  control={<Checkbox checked={clientRole} onChange={(event)=>setClientRole(event.target.checked)} name="client" />}
                  label="Role Client"
                />
                <FormControlLabel
                  control={<Checkbox checked={employeeRole} onChange={(event)=>setEmployeeRole(event.target.checked)} name="employee" />}
                  label="Role Employee"
                />
                <FormControlLabel
                  control={<Checkbox checked={adminRole} onChange={(event)=>setAdminRole(event.target.checked)} name="admin" />}
                  label="Role Admin"
                />
              </FormGroup>
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
              handleConfirmAction={handleEditAccessLevels}
            />
            </form>
            </div>
        </Container>
    </div >
  );
}
export default AccessLevelsEditor;