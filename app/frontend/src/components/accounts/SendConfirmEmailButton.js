import React, {useState, useEffect} from "react";
import { useTranslation } from 'react-i18next';
import AccountService from '../../services/AccountService';
import AlertApiResponseHandler from '../AlertApiResponseHandler';

import Typography from '@material-ui/core/Typography';
import { Button } from "@material-ui/core";
import Paper from '@material-ui/core/Paper';
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Collapse from '@material-ui/core/Collapse';
import SyncIcon from '@material-ui/icons/Sync';
import { makeStyles } from '@material-ui/core/styles';


function SendConfirmEmailButton({account}) {
    const { t } = useTranslation();
    const [emailVerified, setEmailVerified] = useState(false);
    const [disabledButton, setDisabledButton] = useState(false);

    const [openWarningAlert, setOpenWarningAlert] = useState(false);
    const [alertWarningMessage, setAlertWarningMessage] = useState('');
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
    const [alertInfoMessage, setAlertInfoMessage] = useState('');

    const handleClick = () => {
        setDisabledButton(true);
        if(!emailVerified)
            sendMail();
    };

    async function sendMail() {
        await AccountService.sendMail(account.email)
        .then(response => {
            if (response.status === 200) { 
                setAlertInfoMessage(t('response.ok'));
                setOpenSuccessAlert(true);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("SendConfirmEmailButton: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
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
                    onClick={handleClick}
                    fullWidth
                    disabled={disabledButton}
                >
                    {t('button.send-email-confirmation')}</Button>
                <AlertApiResponseHandler
                  openWarningAlert={openWarningAlert}
                  setOpenWarningAlert={setOpenWarningAlert}
                  openSuccessAlert={openSuccessAlert}
                  setOpenSuccessAlert={setOpenSuccessAlert}
                  alertWarningMessage={alertWarningMessage}
                  alertInfoMessage={alertInfoMessage}
                />
            </Paper>
        </Typography>
    );
}
export default SendConfirmEmailButton;