import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useFields } from '../../hooks/FieldHook';
import { useForm } from "react-hook-form";
import ConfirmDialog from '../ConfirmDialog';
import AcceptButtons from '../AcceptButtons';
import AlertApiResponseHandler from '../AlertApiResponseHandler';

import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import AccountBoxIcon from '@material-ui/icons/AccountBox';

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
}));

function PasswordChange({account, handleClose, apiMethod, adminView}) {
  const classes = useStyles();
  const [fields, setFields] = useFields({
    oldPassword: '',
    newPassword: '',
    confirmNewPassword: '',
  });
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [showRefresh, setShowRefresh] = useState(false);
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);

  const handleConfirmDialog = () => {
    setOpenConfirmDialog(!openConfirmDialog);
  }

  const handleChangePassword = () => {
    account.authenticationDataDTO.password = fields.oldPassword;
    changePassword();
    handleConfirmDialog();
  }


  async function changePassword() {
    await apiMethod(account, fields.newPassword)
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
            console.error("PasswordChange: " + resMessage);
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
                Change password
            </Typography>
            <form className={classes.form} noValidate onSubmit={handleSubmit(handleConfirmDialog)}>
                <Grid container spacing={2}>
                  { adminView ?
                      <></>
                    :
                    <Grid item xs={12}>
                      <TextField
                        value={ fields.oldPassword }
                        onChange={ setFields }
                        variant="outlined"
                        required
                        fullWidth
                        name="oldPassword"
                        label="Present password"
                        type="password"
                        id="oldPassword"
                        autoComplete="oldPassword"

                        inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/ })}
                        error={errors.oldPassword ? true : false}
                        helperText={errors.oldPassword ? "Password is required (must have 8 digits and...)" : ""}
                      />
                    </Grid>
                  } 
                <Grid item xs={12}>
                    <TextField
                      value={ fields.newPassword }
                      onChange={ setFields }
                      variant="outlined"
                      required
                      fullWidth
                      name="newPassword"
                      label="New password"
                      type="password"
                      id="newPassword"
                      autoComplete="newPassword"

                      inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/ })}
                      error={errors.newPassword ? true : false}
                      helperText={errors.newPassword ? "Password is required (must have 8 digits and...)" : ""}
                     />
                  </Grid>
                  <Grid item xs={12}>
                  <TextField
                      value={ fields.confirmPassword }
                      onChange={ setFields }
                      variant="outlined"
                      required
                      fullWidth
                      name="confirmPassword"
                      label="Confirm password"
                      type="password"
                      id="confirmPassword"
                      autoComplete="current-password"

                      inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/, 
                                          validate: confirmPassword => confirmPassword === fields.newPassword})}
                      error={errors.confirmPassword ? true : false}
                      helperText={errors.confirmPassword ? 
                                  errors.confirmPassword?.type === "validate" ? "Both must be the same" : "Password is required (must have 8 digits and...)"
                                  : ""}
                  />
                  </Grid>
                </Grid>
                <AlertApiResponseHandler
                  openWarningAlert={openWarningAlert}
                  setOpenWarningAlert={setOpenWarningAlert}
                  openSuccessAlert={openSuccessAlert}
                  setOpenSuccessAlert={setOpenSuccessAlert}
                  alertWarningMessage={alertWarningMessage}
                  alertInfoMessage={alertInfoMessage}
                />
                <AcceptButtons
                  submitButtonTitle="Change"
                  handleClose={handleClose}
                  showRefreshButton={showRefresh}
                />
                <ConfirmDialog
                  openConfirmDialog={openConfirmDialog}
                  setOpenConfirmDialog={setOpenConfirmDialog}
                  handleConfirmAction={handleChangePassword}
                />
            </form>
            </div>
        </Container>
    </div >
  );
}
export default PasswordChange;