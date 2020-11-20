import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useFields } from '../../hooks/FieldHook';
import { useForm } from "react-hook-form";
import AccountService from '../../services/AccountService';

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
import DoneIcon from '@material-ui/icons/Done';
import Alert from '@material-ui/lab/Alert';
import SyncIcon from '@material-ui/icons/Sync';

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
      buttonEdit: {
        backgroundColor: "#51c953",
        "&:hover": {
          backgroundColor: "#0bb00d"
        }
      },
      buttonCancel: {
        backgroundColor: "#e35656",
        "&:hover": {
          backgroundColor: "#eb1e1e"
        }
      },
      buttonRefresh: {
        backgroundColor: "#4285F4",
        "&:hover": {
          backgroundColor: "#2c0fab"
        }
      },
}));

function AccountEdit({account, handleClose}) {
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
  
  const handleEdit = () => {
    account.firstName = fields.firstName;
    account.lastName = fields.lastName;
    editAccount();
  }
  async function editAccount() {
    await AccountService.editUserAccount(account)
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
            <form className={classes.form} noValidate onSubmit={handleSubmit(handleEdit)}>
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
                <Grid container xs={12}>
                    <Grid item xs={6}>
                        <Button
                        type="submit"
                        variant="contained"
                        fullWidth
                        color="primary"
                        className={classes.buttonEdit}
                        >
                        Edit
                        </Button>
                    </Grid>
                    <Grid item xs={6}>
                        <Button
                        onClick={handleClose}
                        variant="contained"
                        color="primary"
                        fullWidth
                        className={classes.buttonCancel}
                        >
                        Cancel
                        </Button>
                    </Grid>
                    <Grid item xs={12}>
                        <Collapse in={showRefresh}>
                            
                                <Button
                                onClick={handleClose}
                                variant="contained"
                                color="primary"
                                fullWidth
                                className={classes.buttonRefresh}
                                startIcon={<SyncIcon size="large" color="primary"/>}
                                >
                                Refresh data
                                </Button>
                        </Collapse>
                    </Grid>
                </Grid>
                {/* <Dialog
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
                </Dialog> */}
            </form>
            </div>
        </Container>
    </div >
  );
}
export default AccountEdit;