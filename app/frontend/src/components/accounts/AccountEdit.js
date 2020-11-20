import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useFields } from '../../hooks/FieldHook';
import { useForm } from "react-hook-form";
import AccountService from '../../services/AccountService';
import ConfirmDialog from '../ConfirmDialog';
import AcceptButtons from '../AcceptButtons';

import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Collapse from '@material-ui/core/Collapse';
import Grid from '@material-ui/core/Grid';
import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import Avatar from '@material-ui/core/Avatar';
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Alert from '@material-ui/lab/Alert';
import SyncIcon from '@material-ui/icons/Sync';
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
  const [openAlert, setOpenAlert] = useState(false);
  const [alertErrorMessage, setAlertErrorMessage] = useState('');
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
            setAlertErrorMessage(error.response.data.message.toString());
            setOpenAlert(true);
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
                <Collapse in={openAlert}>
                <Alert severity="warning" action={
                        <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenAlert(false); }}>
                        <CloseIcon fontSize="inherit" />
                        </IconButton>
                }>
                    {alertErrorMessage}
                </Alert>
                </Collapse>
                <Collapse in={openSuccessAlert}>
                <Alert severity="success" action={
                        <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenSuccessAlert(false); }}>
                        <CloseIcon fontSize="inherit" />
                        </IconButton>
                }>
                    {alertInfoMessage}
                </Alert>
                </Collapse>
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