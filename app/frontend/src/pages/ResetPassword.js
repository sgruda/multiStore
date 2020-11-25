import React, { useState } from 'react';
import { useForm } from "react-hook-form";
import { useTranslation } from 'react-i18next';

import AccountService from '../services/AccountService';
import AlertApiResponseHandler from '../components/AlertApiResponseHandler';
import { useFields } from '../hooks/FieldHook';


import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import ContactMailIcon from '@material-ui/icons/ContactMail';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import CircularProgress from '@material-ui/core/CircularProgress';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import { useHistory } from 'react-router-dom';

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.primary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
    backgroundColor: "#4285F4",
    "&:hover": {
      backgroundColor: "#2c0fab"
    }
  },
  circularProgress: {
    position: 'absolute',
    top: '42%',
    left: '49%',
    margin: theme.spacing(3, 0, 2),
    color: "#4285F4",
  },
}));

function ResetPassword() {
  const classes = useStyles();
  const { t } = useTranslation();
  const history = useHistory();
  const [fields, setFields] = useFields({
    email: "",
    token: "",
    password: "",
    confirmPassword: ""
  });
  const [mailSent, setMailSent] = useState(false);
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [loading, setLoading] = useState(false);
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [openDialog, setOpenDialog] = useState(false);

  const handleCloseDialog = (event, reason) => {
    setOpenDialog(false);
    history.push("/signin")
  }
  const handleReset = () => {
    setLoading(true);
    resetPassword();
  }
  const handleChangePassword = () => {
    setLoading(true);
    changePassword();
  }

  async function resetPassword() {
    await AccountService.resetPassword(fields.email)
        .then(response => {
            if (response.status === 200) { 
                setAlertInfoMessage(t('response.ok'));
                setOpenSuccessAlert(true);
                setMailSent(true);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("ResetPassword: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
            }
        );
        setLoading(false);
    }   
    async function changePassword() {
        await AccountService.changeResettedPassword(fields.password, fields.token)
            .then(response => {
                if (response.status === 200) { 
                    setAlertInfoMessage(t('response.ok'));
                    setOpenSuccessAlert(true);
                    setOpenDialog(true);
                }
            },
                (error) => {
                const resMessage =
                    (error.response && error.response.data && error.response.data.message) 
                    || error.message || error.toString();
                    console.error("ResetPassword: " + resMessage);
                    setAlertWarningMessage(t(error.response.data.message.toString()));
                    setOpenWarningAlert(true);
                }
            );
            setLoading(false);
        }  
  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <ContactMailIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          {t('pages.titles.reset-password')}
        </Typography>
        { !loading && mailSent 
        ? 
        <form className={classes.form} noValidate onSubmit={handleSubmit(handleChangePassword)}>
        <Grid container spacing={2}>
            <Grid item xs={12}>
                <TextField
                    value={ fields.token }
                    onChange={ setFields }
                    variant="outlined"
                    required
                    fullWidth
                    id="token"
                    label={t('account.password.reset.form.label.token')}
                    name="token"
                    autoComplete="token"

                    inputRef={register({ required: true,  pattern: /[0-9a-zA-Z]+/ })}
                    error={errors.token ? true : false}
                    helperText={errors.token ? t('validation.message.incorrect.entry') : t('validation.message.required.helper.token')}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                    value={ fields.password }
                    onChange={ setFields }
                    variant="outlined"
                    required
                    fullWidth
                    name="password"
                    label={t('account.password.reset.form.label.password.main')}
                    type="password"
                    id="password"
                    autoComplete="current-password"

                    inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/ })}
                    error={errors.password ? true : false}
                    helperText={errors.password ? t('validation.message.required.incorrect.password.main') : t('validation.message.required.helper.password')}
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
                    label={t('account.password.reset.form.label.password.confirm')}
                    type="password"
                    id="confirmPassword"
                    autoComplete="current-password"

                    inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/, 
                                        validate: confirmPassword => confirmPassword === fields.password})}
                    error={errors.confirmPassword ? true : false}
                    helperText={errors.confirmPassword ? 
                                errors.confirmPassword?.type === "validate" ? t('validation.message.required.incorrect.password.confirm') : t('validation.message.required.helper.password')
                                : ""}
                />
            </Grid>
            <Grid item xs={12}>
            <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                className={classes.submit}
                disabled={loading}
            >
                {t('button.reset-password')}
            </Button>
            </Grid>
        </Grid>
        </form>
        :
        <form className={classes.form} noValidate onSubmit={handleSubmit(handleReset)}>
        <TextField
            value={ fields.email }
            onChange={ setFields }
            variant="outlined"
            required
            fullWidth
            id="email"
            label={t('account.password.reset.form.label.email')}
            name="email"
            autoComplete="email"

            inputRef={register({ required: true,  pattern: /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/ })}
            error={errors.email ? true : false}
            helperText={errors.email ? t('validation.message.incorrect.entry') : t('validation.message.required.helper.email')}
        />
        <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
            disabled={loading}
          >
            {t('button.send')}
          </Button>
        </form>
        }
          { loading && <CircularProgress size={35} className={classes.circularProgress} />}
          <AlertApiResponseHandler
                  openWarningAlert={openWarningAlert}
                  setOpenWarningAlert={setOpenWarningAlert}
                  openSuccessAlert={openSuccessAlert}
                  setOpenSuccessAlert={setOpenSuccessAlert}
                  alertWarningMessage={alertWarningMessage}
                  alertInfoMessage={alertInfoMessage}
                />
        <Dialog
            open={openDialog}
            onClose={handleCloseDialog}
            aria-describedby="dialog-description"
        >
            <DialogContent>
                <DialogContentText id="dialog-description">
                  {t('dialog.content.reset-password')}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleCloseDialog} color="primary" autoFocus>
                {t('button.ok')}
                </Button>
            </DialogActions>
        </Dialog>
      </div>
    </Container>
  );
}
export default ResetPassword;