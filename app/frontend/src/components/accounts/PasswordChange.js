import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useFields } from '../../hooks/FieldHook';
import { useForm } from "react-hook-form";
import AccountService from '../../services/AccountService';

import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Collapse from '@material-ui/core/Collapse';
import Grid from '@material-ui/core/Grid';
import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import Avatar from '@material-ui/core/Avatar';
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Alert from '@material-ui/lab/Alert';
import SyncIcon from '@material-ui/icons/Sync';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';

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

function PasswordChange({account, handleClose, apiMethod}) {
  const classes = useStyles();
  const [fields, setFields] = useFields({
    oldPassword: '',
    newPassword: '',
    confirmNewPassword: '',
  });
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [openAlert, setOpenAlert] = useState(false);
  const [alertErrorMessage, setAlertErrorMessage] = useState('');
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
            setAlertErrorMessage(error.response.data.message.toString());
            setOpenAlert(true);
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
                <Collapse in={openAlert}>
                <Alert severity="warning" action={
                        <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenAlert(false); }}>
                        <CloseIcon fontSize="inherit" />
                        </IconButton>
                }>
                    {alertErrorMessage}
                </Alert>
                </Collapse>
                <Collapse in={openSuccessAlert}>
                <Alert severity="success" action={
                        <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenSuccessAlert(false); }}>
                        <CloseIcon fontSize="inherit" />
                        </IconButton>
                }>
                    {alertInfoMessage}
                </Alert>
                </Collapse>
                <Grid container xs={12}>
                    <Grid item xs={6}>
                        <Button
                        type="submit"
                        variant="contained"
                        fullWidth
                        color="primary"
                        className={classes.buttonEdit}
                        >
                        Change
                        </Button>
                    </Grid>
                    <Grid item xs={6}>
                        <Button
                        onClick={handleClose}
                        variant="contained"
                        color="primary"
                        fullWidth
                        className={classes.buttonCancel}
                        >
                        Cancel
                        </Button>
                    </Grid>
                    <Grid item xs={12}>
                        <Collapse in={showRefresh}>
                            
                                <Button
                                onClick={handleClose}
                                variant="contained"
                                color="primary"
                                fullWidth
                                className={classes.buttonRefresh}
                                startIcon={<SyncIcon size="large" color="primary"/>}
                                >
                                Refresh data
                                </Button>
                        </Collapse>
                    </Grid>
                </Grid>
                <Dialog
                    open={openConfirmDialog}
                    onClose={handleConfirmDialog}
                    aria-describedby="dialog-description"
                >
                    <DialogContent>
                    <DialogContentText id="dialog-description">
                       Are you sure?
                    </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                    <Button onClick={handleChangePassword} color="primary" autoFocus>
                        Yes
                    </Button>
                    <Button onClick={handleConfirmDialog} color="primary" autoFocus>
                        No
                    </Button>
                    </DialogActions>
                </Dialog>
            </form>
            </div>
        </Container>
    </div >
  );
}
export default PasswordChange;