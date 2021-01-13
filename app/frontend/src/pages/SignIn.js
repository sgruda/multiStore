import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useHistory } from "react-router-dom";
import { useForm } from "react-hook-form";

import AuthenticationService from '../services/AuthenticationService';
import { useFields } from '../hooks/FieldHook';

import  SocialButtons from '../components/SocialButtons';
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL } from '../config/config';

import Alert from '@material-ui/lab/Alert';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import CircularProgress from '@material-ui/core/CircularProgress';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';

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
  socialButtons: {
    padding: 10,
    width: '47%'
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
    left: '47%',
    margin: theme.spacing(3, 0, 2),
    color: "#4285F4",
  },
}));

function SignIn() {
  const classes = useStyles();
  const { t } = useTranslation();
  const [fields, setFields] = useFields({
    username: "",
    password: ""
  });
  const { setUserIsAuthenticated, setCurrentAccessToken } = useAuth();
  const history = useHistory();

  const [loading, setLoading] = useState(false);
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [openAlert, setOpenAlert] = useState(false);
  const [alertErrorMessage, setAlertErrorMessage] = useState(undefined);


  const convertValidationMessage = (message) => {
    if(message.startsWith("error")) return t(message);
    let retMessage = '';
    message = message.replace('{', '').replace('}', '')
    let parts = message.split(", ");
    parts.map(part => {
      let fieldError = part.split('=');
      let code = fieldError[1] + '.' + fieldError[0];
      retMessage += t(code) + ' ';
    })
    return retMessage;
  }

  function handleSignIn() {
    setLoading(true);

      AuthenticationService.signIn(fields.username,  fields.password)
      .then( () => {
          setUserIsAuthenticated(true);
          setCurrentAccessToken(AuthenticationService.getAccessTokenFromStorage());
          history.push("/");
          setLoading(false)
        },
        (error) => {
          const resMessage =
            (error.response && error.response.data && error.response.data.message) 
            || error.message || error.toString();

          setLoading(false);
          setAlertErrorMessage(convertValidationMessage(resMessage));
          setOpenAlert(true);
        }
      );
  }

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          {t('pages.titles.signin')}
        </Typography>
        <form className={classes.form} noValidate onSubmit={handleSubmit(handleSignIn)}>
          <TextField
            value={ fields.username }
            onChange={ setFields }
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="username"
            label={t('account.signin.username')}
            name="username"
            autoComplete="username"
            
            inputRef={register({ required: true,  pattern: /[a-zA-Z0-9!@#$%^*]+/ })}
            error={errors.username ? true : false}
            helperText={errors.username ? t('validation.message.incorrect.entry') : t('validation.message.required.helper.username') }
          />    
          <TextField
            value={ fields.value }
            onChange={ setFields }
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label={t('account.signin.password')}
            type="password"
            id="password"
            autoComplete="current-password"

            inputRef={register({ required: true })}
            error={errors.password ? true : false}
            helperText={errors.password ? t('validation.message.incorrect.entry') : t('validation.message.required.helper.password.default')}
          />
          <Collapse in={openAlert}>
            <Alert severity="warning" action={
                  <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenAlert(false); }}>
                    <CloseIcon fontSize="inherit" />
                  </IconButton>
            }>
              {alertErrorMessage}
            </Alert>
          </Collapse>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
            disabled={loading}
          >
            {t('button.signin.default')}
          </Button>
          { loading && <CircularProgress size={70} className={classes.circularProgress} />}
          <SocialButtons GOOGLE_AUTH_URL={GOOGLE_AUTH_URL} GOOGLE_TEXT={t('button.signin.google')}
                        FACEBOOK_AUTH_URL={FACEBOOK_AUTH_URL} FACEBOOK_TEXT={t('button.signin.facebook')}
                        className={classes.socialButtons} />
          <Grid container>
            <Grid item xs>
              <Link to="/reset-password" variant="body2">
                {t('redirect.to.page.reset-password')}
              </Link>
            </Grid>
            <Grid item>
              <Link to="/signup" variant="body2">
                {t('redirect.to.page.signup')}
              </Link>
            </Grid>
          </Grid>
        </form>
      </div>
    </Container>
  );
}
export default SignIn;