import React, { useState } from 'react';
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useHistory } from "react-router-dom";
import { useForm } from "react-hook-form";

import AuthenticationService from '../services/AuthenticationService';
// import { onError } from '../services/exceptions/ErrorService';
import { useFields } from '../hooks/FieldHook';

import {ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN} from '../config/config';

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
    backgroundColor: theme.palette.secondary.main,
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
          setAlertErrorMessage(error.response.data.message.toString());
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
          Sign in
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
            label="Username"
            name="username"
            autoComplete="username"
            
            inputRef={register({ required: true,  pattern: /[a-zA-Z0-9!@#$%^*]+/ })}
            error={errors.username ? true : false}
            helperText={errors.username ? "Incorrect entry." : ""}
          />    
          <TextField
            value={ fields.value }
            onChange={ setFields }
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"

            inputRef={register({ required: true })}
            error={errors.password ? true : false}
            helperText={errors.password ? "Password is required" : ""}
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
            Sign In
          </Button>
          { loading && <CircularProgress size={70} className={classes.circularProgress} />}

         


            <SocialButtons GOOGLE_AUTH_URL={GOOGLE_AUTH_URL} GOOGLE_TEXT="Sign in with Google"
                          FACEBOOK_AUTH_URL={FACEBOOK_AUTH_URL} FACEBOOK_TEXT="Sign in with Facebook"
                          className={classes.socialButtons} />
          <Grid container>
            <Grid item xs>
              <Link href="#" variant="body2">
                Forgot password?
              </Link>
            </Grid>
            <Grid item>
              <Link to="/signup" variant="body2">
                {"Don't have an account? Sign Up"}
              </Link>
            </Grid>
          </Grid>
        </form>
      </div>
    </Container>
  );
}
export default SignIn;