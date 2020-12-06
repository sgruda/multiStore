import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import { useFields } from '../../hooks/FieldHook';
import { useForm } from "react-hook-form";
import { useAuth } from '../../context/AuthContext';
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
  const { t } = useTranslation();
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

  const {checkExpiredJWTAndExecute} = useAuth();

  const handleConfirmDialog = () => {
    setOpenConfirmDialog(!openConfirmDialog);
  }

  const handleChangePassword = () => {
    account.authenticationDataDTO.password = fields.oldPassword;
    checkExpiredJWTAndExecute(changePassword);
    handleConfirmDialog();
  }


  async function changePassword() {
    await apiMethod(account, fields.newPassword)
    .then(response => {
        if (response.status === 200) { 
            setAlertInfoMessage(t('response.ok'));
            setOpenSuccessAlert(true);
        }
    },
        (error) => {
        const resMessage =
            (error.response && error.response.data && error.response.data.message) 
            || error.message || error.toString();
            console.error("PasswordChange: " + resMessage);
            setAlertWarningMessage(t(error.response.data.message.toString()));
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
                {t('pages.titles.account.change-password')}
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
                        label={t('account.password.change.present')}
                        type="password"
                        id="oldPassword"
                        autoComplete="oldPassword"

                        inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/ })}
                        error={errors.oldPassword ? true : false}
                        helperText={errors.oldPassword ? t('validation.message.required.incorrect.password.present') : t('validation.message.required.helper.password.present')}
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
                      label={t('account.password.change.new')}
                      type="password"
                      id="newPassword"
                      autoComplete="newPassword"

                      inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/ })}
                      error={errors.newPassword ? true : false}
                      helperText={errors.newPassword ? t('validation.message.required.incorrect.password.new') : t('validation.message.required.helper.password.new')}
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
                      label={t('account.password.change.confirm-password')}
                      type="password"
                      id="confirmPassword"
                      autoComplete="current-password"

                      inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/, 
                                          validate: confirmPassword => confirmPassword === fields.newPassword})}
                      error={errors.confirmPassword ? true : false}
                      helperText={errors.confirmPassword ? 
                                  errors.confirmPassword?.type === "validate" ? t('validation.message.required.incorrect.password.confirm') : t('validation.message.required.helper.password.confirm')
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
                  submitButtonTitle={t('button.change')}
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