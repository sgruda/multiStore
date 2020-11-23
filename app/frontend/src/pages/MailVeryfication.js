import React, { useState, useEffect } from 'react';
import { useHistory} from "react-router-dom";

import AccountService from '../services/AccountService';
import AlertApiResponseHandler from '../components/AlertApiResponseHandler';

import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import MailIcon from '@material-ui/icons/Mail';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import CircularProgress from '@material-ui/core/CircularProgress';

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
  submit: {
    margin: theme.spacing(3, 0, 2),
    marginTop: '18%',
    backgroundColor: "#4285F4",
    "&:hover": {
      backgroundColor: "#2c0fab"
    }
  },
  circularProgress: {
    position: 'absolute',
    top: '25%',
    left: '48%',
    margin: theme.spacing(3, 0, 2),
    color: "#4285F4",
  },
}));

function MailVerification() {
  const classes = useStyles();
  const [loading, setLoading] = useState(true);
  const history = useHistory();

  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');

  const getTokenFromURL = () => {
        var regex = new RegExp('[\\?&]token=([^&#]*)');
        var results = regex.exec(history.location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  }

  async function verifyEmail(token) {
    await AccountService.verifyEmail(token)
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
                console.error("ResetPassword: " + resMessage);
                setAlertWarningMessage(error.response.data.message.toString());
                setOpenWarningAlert(true);
            }
        );
        setLoading(false);
    } 

    useEffect(() => {
        if (loading) {
            verifyEmail(getTokenFromURL());
        }
        setLoading(false);
    }, [loading]);
  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <MailIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
             E-mail Veryfication
        </Typography>
            { loading && <CircularProgress size={50} className={classes.circularProgress} />}
            <AlertApiResponseHandler
                    openWarningAlert={openWarningAlert}
                    setOpenWarningAlert={setOpenWarningAlert}
                    openSuccessAlert={openSuccessAlert}
                    setOpenSuccessAlert={setOpenSuccessAlert}
                    alertWarningMessage={alertWarningMessage}
                    alertInfoMessage={alertInfoMessage}
                />
            <Button 
                fullWidth 
                className={classes.submit} 
                onClick={() => history.push("/signin")}
            >
                Go sign in</Button>
      </div>
    </Container>
  );
}
export default MailVerification;