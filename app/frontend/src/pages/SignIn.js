import React, { useState } from 'react';
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useHistory } from "react-router-dom";
import { useForm } from "react-hook-form";

import AuthenticationService from '../services/AuthenticationService';
import { onError } from '../services/exceptions/ErrorService';
import { useFields } from '../hooks/FieldHook';

import  SocialButtons from '../components/SocialButtons';
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL } from '../config/config';

import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';



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
}));

function SignIn() {
  const classes = useStyles();
  const [fields, setFields] = useFields({
    username: "",
    password: ""
  });
  const {setUserIsAuthenticated} = useAuth();
  const history = useHistory();

  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 



  function signIn() {
    return AuthenticationService.signIn(fields.username, fields.password)
      .then(response => {
         AuthenticationService.saveTokenJWT(response) ? setUserIsAuthenticated(true) : onError(new Error("nie udało sie zalogowac"))
         history.push("/admin");
        }).catch(e => {
          onError(e);
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
        <form className={classes.form} noValidate onSubmit={handleSubmit(signIn)}>
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
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
          >
            Sign In
          </Button>
          {/* <Container className={classes.submit} > */}
            <SocialButtons GOOGLE_AUTH_URL={GOOGLE_AUTH_URL} GOOGLE_TEXT="Sign in with Google"
                          FACEBOOK_AUTH_URL={FACEBOOK_AUTH_URL} FACEBOOK_TEXT="Sign in with Facebook"
                          className={classes.socialButtons} />
          {/* </Container> */}
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