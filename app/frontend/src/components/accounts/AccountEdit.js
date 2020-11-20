import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useFields } from '../../hooks/FieldHook';
import { useForm } from "react-hook-form";
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

function AccountEdit({account, handleClose, apiMethod}) {
  const classes = useStyles();
  const [fields, setFields] = useFields({
    firstName: account.firstName,
    lastName: account.lastName,
  });
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [showRefresh, setShowRefresh] = useState(false);
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);

  const handleConfirmDialog = () => {
    setOpenConfirmDialog(!openConfirmDialog);
  }

  const handleEdit = () => {
    account.firstName = fields.firstName;
    account.lastName = fields.lastName;
    editAccount();
    handleConfirmDialog();
  }


  async function editAccount() {
    await apiMethod(account)
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
            console.error("AccountEdit: " + resMessage);
            setAlertWarningMessage(error.response.data.message.toString());
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
                Edit profile
            </Typography>
            <form className={classes.form} noValidate onSubmit={handleSubmit(handleConfirmDialog)}>
                <Grid container spacing={2}>
                <Grid item xs={12} sm={12}>
                    <TextField
                    value={ fields.firstName }
                    onChange={ setFields }
                    autoComplete="fname"
                    name="firstName"
                    variant="outlined"
                    required
                    fullWidth
                    id="firstName"
                    label="First Name"

                    inputRef={register({ required: true,  pattern: /^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+/ })}
                    error={errors.firstName ? true : false}
                    helperText={errors.firstName ? "Incorrect entry." : ""}
                    />
                </Grid>
                <Grid item xs={12} sm={12}>
                    <TextField
                    value={ fields.lastName }
                    onChange={ setFields }
                    variant="outlined"
                    required
                    fullWidth
                    id="lastName"
                    label="Last Name"
                    name="lastName"
                    autoComplete="lname"

                    inputRef={register({ required: true,  pattern: /^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+/ })}
                    error={errors.lastName ? true : false}
                    helperText={errors.lastName ? "Incorrect entry." : ""}
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
                  submitButtonTitle="Edit"
                  handleClose={handleClose}
                  showRefreshButton={showRefresh}
                />
                <ConfirmDialog
                  openConfirmDialog={openConfirmDialog}
                  setOpenConfirmDialog={setOpenConfirmDialog}
                  handleConfirmAction={handleEdit}
                />
            </form>
            </div>
        </Container>
    </div >
  );
}
export default AccountEdit;