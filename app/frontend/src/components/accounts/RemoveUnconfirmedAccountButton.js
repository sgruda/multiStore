import React, {useState, useEffect} from "react";
import AccountService from '../../services/AccountService';
import AlertApiResponseHandler from '../AlertApiResponseHandler';
import ConfirmDialog from '../ConfirmDialog';

import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { Button } from "@material-ui/core";
import Paper from '@material-ui/core/Paper';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import Alert from '@material-ui/lab/Alert';
import SyncIcon from '@material-ui/icons/Sync';

const useStyles = makeStyles(({
    buttonRefresh: {
      backgroundColor: "#4285F4",
      "&:hover": {
        backgroundColor: "#2c0fab"
      }
    },
}));

function RemoveUnconfirmedAccountButton({account, buttonStyle, afterDeleteAccount, handleRefresh}) {
    const classes = useStyles();
    const accountName = account.firstName + ' ' + account.lastName;
    const [emailVerified, setEmailVerified] = useState(false);
    const [disabledButton, setDisabledButton] = useState(false);

    const [openWarningAlert, setOpenWarningAlert] = useState(false);
    const [alertWarningMessage, setAlertWarningMessage] = useState('');
    const [openSuccessDialog, setOpenSuccessDialog] = useState(false);
    const [openConfirmDialog, setOpenConfirmDialog] = useState(false);

    const handleConfirmDialog = () => {
      setOpenConfirmDialog(!openConfirmDialog);
    }

    const handleCloseSuccessDialog = () => {
        afterDeleteAccount();
        setOpenSuccessDialog(false);
    }

    const handleClickRefresh = () => {
        handleRefresh();
        setOpenWarningAlert(false);
    }

    const handleRemove = () => {
        setDisabledButton(true);
        if(!emailVerified)
            removeAccount();
    };

    async function removeAccount() {
        await AccountService.removeSingleAccount(account)
        .then(response => {
            if (response.status === 200) { 
                setOpenSuccessDialog(true);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("RemoveUnconrfimedAccount: " + resMessage);
                setAlertWarningMessage(error.response.data.message.toString());
                setOpenWarningAlert(true);
            }
        );
        setDisabledButton(false);
    }

    useEffect(() => {
        setEmailVerified(account.authenticationDataDTO.emailVerified);
    }, [account]);


    return (
        <Typography color="inherit" variant="subtitle1" component="div"  align="center">
            <Paper elevation={3}>
                { openWarningAlert ? 
                    <Button
                        onClick={handleClickRefresh}
                        variant="contained"
                        color="primary"
                        fullWidth
                        className={classes.buttonRefresh}
                        startIcon={<SyncIcon size="large" color="primary"/>}
                    >
                        Refresh data
                    </Button>
                :
                    <Button 
                        onClick={handleConfirmDialog}
                        fullWidth
                        disabled={disabledButton}
                        className={buttonStyle}
                    >
                    Remove</Button>
                }
                <Collapse in={openWarningAlert}>
                    <Alert severity="warning" action={
                            <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenWarningAlert(false); handleRefresh(); }}>
                            <CloseIcon fontSize="inherit" />
                            </IconButton>
                    }>
                        {alertWarningMessage}
                    </Alert>
                </Collapse>
                <Dialog
                  open={openSuccessDialog}
                  onClose={handleCloseSuccessDialog}
                  aria-describedby="dialog-description"
                >
                    <DialogContent>
                      <DialogContentText id="dialog-description">
                        The {accountName} account was successfully deleted.
                      </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                      <Button onClick={handleCloseSuccessDialog} color="primary" autoFocus>
                        OK
                      </Button>
                    </DialogActions>
                </Dialog>
                <ConfirmDialog
                  openConfirmDialog={openConfirmDialog}
                  setOpenConfirmDialog={setOpenConfirmDialog}
                  handleConfirmAction={handleRemove}
                />
            </Paper>
        </Typography>
    );
}
export default RemoveUnconfirmedAccountButton;