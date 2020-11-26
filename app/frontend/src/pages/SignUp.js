import  React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { useForm } from "react-hook-form";

import AuthenticationService from '../services/AuthenticationService';
import { useFields } from '../hooks/FieldHook';

import SocialButtons from '../components/SocialButtons';
import AddAccountForm from '../components/accounts/AddAccountForm';
import SimpleAlert from '../components/simple/SimpleAlert';
import SimpleDialog from '../components/simple/SimpleDialog';
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL } from '../config/config';

import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import CircularProgress from '@material-ui/core/CircularProgress';



const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(3),
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
    marginTop: theme.spacing(2),
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

function SignUp() {
  const classes = useStyles();
  const { t } = useTranslation();
  const [fields, setFields] = useFields({
    firstName: "",
    lastName: "",
    email: "",
    username: "",
    password: "",
    confirmPassword: ""
  });
  const history = useHistory();

  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [openDialog, setOpenDialog] = useState(false);
  const [loading, setLoading] = useState(false);
  const [openAlert, setOpenAlert] = useState(false);
  const [alertErrorMessage, setAlertErrorMessage] = useState(undefined);

  const handleSignUp = () => {
    setLoading(true);
    signUp();
  }

  const convertValidationMessage = (message) => {
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
  
  async function signUp() {
    await AuthenticationService.signUp(fields)
      .then(response => {
        if (response.status === 201) { 
            // history.push("/");
            setOpenDialog(true);
          }
        },
        (error) => {
          const resMessage =
            (error.response && error.response.data && error.response.data.message) 
            || error.message || error.toString();

          setAlertErrorMessage(convertValidationMessage(error.response.data.message.toString()));
          setOpenAlert(true);
        }
      );
      setLoading(false);
  }

  const handleCloseDialog = () => {
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
                {t('pages.titles.signup')}
              </Typography>
              <form className={classes.form} noValidate onSubmit={handleSubmit(handleSignUp)}>
                <AddAccountForm
                  fields={fields}
                  setFields={setFields}
                  register={register}
                  errors={errors}
                />
                <SimpleAlert
                  openAlert={openAlert}
                  severityAlert="warning"
                  setOpenAlert={setOpenAlert}
                  alertMessage={alertErrorMessage}
                />
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  color="primary"
                  className={classes.submit}
                >
                  {t('button.signup.default')}
                </Button>
                { loading && <CircularProgress size={70} className={classes.circularProgress} />}
                <SocialButtons 
                  GOOGLE_AUTH_URL={GOOGLE_AUTH_URL} 
                  GOOGLE_TEXT={t('button.signup.google')}
                  FACEBOOK_AUTH_URL={FACEBOOK_AUTH_URL} 
                  FACEBOOK_TEXT={t('button.signup.facebook')}
                  className={classes.socialButtons}
                />
                <Grid item>
                  <Link to="/signin" variant="body2">
                  {t('redirect.to.page.signin.signup')}
                  </Link>
                </Grid>
                <SimpleDialog
                  openDialog={openDialog}
                  handleCloseDialog={handleCloseDialog}
                  dialogContent={t('dialog.content.signup')}
                  buttonText={t('button.ok')}
                />
              </form>
            </div>
          </Container>
      </div >
    </div>
  );
}

export default SignUp;