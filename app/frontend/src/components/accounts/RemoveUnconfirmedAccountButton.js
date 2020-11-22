import React, {useState, useEffect} from "react";
import AccountService from '../../services/AccountService';
import AlertApiResponseHandler from '../AlertApiResponseHandler';
import ConfirmDialog from '../ConfirmDialog';

import Typography from '@material-ui/core/Typography';
import { Button } from "@material-ui/core";
import Paper from '@material-ui/core/Paper';


function RemoveUnconfirmedAccountButton({account, buttonStyle}) {
    const [emailVerified, setEmailVerified] = useState(false);
    const [disabledButton, setDisabledButton] = useState(false);

    const [openWarningAlert, setOpenWarningAlert] = useState(false);
    const [alertWarningMessage, setAlertWarningMessage] = useState('');
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
    const [alertInfoMessage, setAlertInfoMessage] = useState('');
    const [openConfirmDialog, setOpenConfirmDialog] = useState(false);

    const handleConfirmDialog = () => {
      setOpenConfirmDialog(!openConfirmDialog);
    }

    const handleRemove = () => {
        setDisabledButton(true);
        if(!emailVerified)
            removeAccount();
    };

    async function removeAccount() {
        await AccountService.removeSingleAccount(account)
        .then(response => {
            console.log('response')
            console.log(response)

            if (response.status === 200) { 
                setAlertInfoMessage('response.data');
                setOpenSuccessAlert(true);
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
                <Button 
                    onClick={handleConfirmDialog}
                    fullWidth
                    disabled={disabledButton}
                    className={buttonStyle}
                >
                    Remove</Button>
                <AlertApiResponseHandler
                  openWarningAlert={openWarningAlert}
                  setOpenWarningAlert={setOpenWarningAlert}
                  openSuccessAlert={openSuccessAlert}
                  setOpenSuccessAlert={setOpenSuccessAlert}
                  alertWarningMessage={alertWarningMessage}
                  alertInfoMessage={alertInfoMessage}
                />
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