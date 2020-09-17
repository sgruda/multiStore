import React from 'react';
import { Link } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { useForm } from "react-hook-form";

import AuthenticationService from '../services/AuthenticationService';
import { onError } from '../services/exceptions/ErrorService';
import { useFields } from '../hooks/FieldHook';

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
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(3),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

function SignUp() {
  const classes = useStyles();
  const [fields, setFields] = useFields({
    firstname: "",
    lastname: "",
    email: "",
    username: "",
    password: ""
  });
  const history = useHistory();
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 

  function signUp() {
    // return AuthenticationService.signIn(fields.username, fields.password)
    //   .then(response => {
    //      AuthenticationService.saveTokenJWT(response) ? setUserIsAuthenticated(true) : onError(new Error("nie udało sie zalogowac"))
    //      history.push("/admin");
    //     }).catch(e => {
    //       onError(e);
    //     }
    //   );
  }


  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign up
        </Typography>
        <form className={classes.form} noValidate onSubmit={handleSubmit(signUp)}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField
                value={ fields.firstname }
                onChange={ setFields }
                autoComplete="fname"
                name="firstname"
                variant="outlined"
                required
                fullWidth
                id="firstname"
                label="First Name"

                inputRef={register({ required: true,  pattern: /^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+/ })}
                error={errors.firstname ? true : false}
                helperText={errors.firstname ? "Incorrect entry." : ""}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                value={ fields.lastname }
                onChange={ setFields }
                variant="outlined"
                required
                fullWidth
                id="lastname"
                label="Last Name"
                name="lastname"
                autoComplete="lname"

                inputRef={register({ required: true,  pattern: /^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+/ })}
                error={errors.lastname ? true : false}
                helperText={errors.lastname ? "Incorrect entry." : ""}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                value={ fields.email }
                onChange={ setFields }
                variant="outlined"
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"

                inputRef={register({ required: true,  pattern: /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/ })}
                error={errors.email ? true : false}
                helperText={errors.email ? "Incorrect entry." : ""}
              />
            </Grid>
            <Grid item xs={12}>
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
            </Grid>
            <Grid item xs={12}>
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

                inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/ })}
                error={errors.password ? true : false}
                helperText={errors.password ? "Password is required (must have 8 digits and...)" : ""}
             />
            </Grid>
          </Grid>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
          >
            Sign Up
          </Button>
          {/* <Grid container justify="flex-end"> */}
            <Grid item>
              <Link to="/signin" variant="body2">
              {"Already have an account? Sign in"}
              </Link>
            </Grid>
          {/* </Grid> */}
        </form>
      </div>
    </Container>
  );
}

export default SignUp;