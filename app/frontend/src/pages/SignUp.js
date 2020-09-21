import  React, { useState } from 'react';
import { Link } from "react-router-dom";
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
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  socialButtons: {
    padding: 10,
    width: '47%'
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(2),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
    backgroundColor: "#4285F4",
    "&:hover": {
      backgroundColor: "#2c0fab"
    }
  },
}));

function SignUp() {
  const classes = useStyles();
  const [fields, setFields] = useFields({
    firstname: "",
    lastname: "",
    email: "",
    username: "",
    password: "",
    confirmPassword: ""
  });
  const history = useHistory();
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [openDialog, setOpenDialog] = useState(false);


  function signUp() {
    return AuthenticationService.signUp(fields.firstname, fields.lastname, fields.email, fields.username, fields.password)
      .then(response => {
        //  AuthenticationService.saveTokenJWT(response) ? setUserIsAuthenticated(true) : onError(new Error("nie udało sie zalogowac"))
         console.info(response);
        if (response.status === 201) { 
            // history.push("/");
            setOpenDialog(true);
          }
        }).catch(e => {
          onError(e);
        }
      );
  }

  const handleCloseDialog = (event, reason) => {
    setOpenDialog(false);
    history.push("/")
  }

  return (
    <div>
        <div>

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
                      value={ fields.password }
                      onChange={ setFields }
                      variant="outlined"
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
                                          validate: confirmPassword => confirmPassword === fields.password})}
                      error={errors.confirmPassword ? true : false}
                      helperText={errors.confirmPassword ? 
                                  errors.confirmPassword?.type === "validate" ? "Both must be the same" : "Password is required (must have 8 digits and...)"
                                  : ""}
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
                <SocialButtons GOOGLE_AUTH_URL={GOOGLE_AUTH_URL} GOOGLE_TEXT="Sign up with Google"
                          FACEBOOK_AUTH_URL={FACEBOOK_AUTH_URL} FACEBOOK_TEXT="Sign up with Facebook"
                          className={classes.socialButtons}/>
                <Grid item>
                  <Link to="/signin" variant="body2">
                  {"Already have an account? Sign in"}
                  </Link>
                </Grid>
                <Dialog
                  open={openDialog}
                  onClose={handleCloseDialog}
                  aria-describedby="dialog-description"
                >
                    <DialogContent>
                      <DialogContentText id="dialog-description">
                        Registration has been done!
                        Confirm your accont (e-mail with activation link)
                      </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                      <Button onClick={handleCloseDialog} color="primary" autoFocus>
                        OK
                      </Button>
                    </DialogActions>
                </Dialog>
              </form>
            </div>
          </Container>
      </div >
    </div>
  );
}

export default SignUp;