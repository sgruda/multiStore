import React, { useState } from 'react';
import { Link, Redirect } from "react-router-dom";
import axios from 'axios';
import { useAuth } from "../context/AuthContext";
import { useHistory } from "react-router-dom";


import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import AuthenticationService from '../services/AuthenticationService';



const API_URL = 'https://localhost:8181/api/auth/signin'



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
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

function SignIn(props) {
  const classes = useStyles();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const {setUserIsAuthenticated} = useAuth();
  const history = useHistory();

  function signIn() {
    return AuthenticationService.signIn(username, password)
      .then(response => {
         AuthenticationService.saveTokenJWT(response) ? setUserIsAuthenticated(true) : alert("Nie udalo sie zalogowac")
         history.push("/admin");
        //  window.location.reload();
        }).catch(e => {
          alert(e);
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
        <form className={classes.form} noValidate>
          <TextField
            value={username}
            onChange={e => {
              setUsername(e.target.value);
            }}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="username"
            label="Username"
            name="text"
            autoComplete="username"
            autoFocus
          />
          <TextField
            value={password}
            onChange={e => {
              setPassword(e.target.value);
            }}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
          />
          <Button
            onClick={signIn}
            // type="submit" //To chyba komplikowało sprawę sprawdź później 
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
          >
            Sign In
          </Button>
           {/* <Button onClick={signInPost}>Sign In</Button> */}
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
               {/* { isError &&<Error>The username or password provided were incorrect!</Error> } */}
            </Grid>
          </Grid>
        </form>
      </div>
    </Container>
  );
}
export default SignIn;