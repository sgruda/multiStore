import React, {useState, useEffect} from "react";
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext';
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

const useStyles = makeStyles(({
    buttonRefresh: {
      backgroundColor: "#4285F4",
      "&:hover": {
        backgroundColor: "#2c0fab"
      }
    },
}));

function ActivitySwitch({classes, account, setLoadingData}) {
    const buttonStyleClasses = useStyles();
    const { t } = useTranslation();
    const [accountActivity, setAccountActivity] = useState(false);

    const [openWarningAlert, setOpenWarningAlert] = useState(false);
    const [alertWarningMessage, setAlertWarningMessage] = useState('');
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
    const [alertInfoMessage, setAlertInfoMessage] = useState('');
    const [showRefresh, setShowRefresh] = useState(false);

    const {checkExpiredJWTAndExecute} = useAuth();

    const handleActivityChange = () => {
        setAccountActivity((prev) => !prev)
        if(accountActivity)
            checkExpiredJWTAndExecute(blockAccount);
        else
            checkExpiredJWTAndExecute(unblockAccount);
    };

    const handleRefresh = () => {
        setLoadingData(true);
        setShowRefresh(false);
    }

    async function blockAccount() {
        await AccountService.block(account)
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
                console.error("ActivitySwitch: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
                setShowRefresh(true);
            }
        );
    }
    async function unblockAccount() {
        await AccountService.unblock(account)
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
                console.error("Activity SelectedAccountDetails: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
                setShowRefresh(true);
            }
        );
    }

    useEffect(() => {
        setAccountActivity(account.active);
    }, [account]);

    return (
        <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
            <Paper className={classes.paperTwo} elevation={3}>
                {t('pages.titles.account.activity')}: 
                <FormControlLabel
                    control={<Switch color="primary" checked={accountActivity} onChange={handleActivityChange} />}
                />
                <AlertApiResponseHandler
                  openWarningAlert={openWarningAlert}
                  setOpenWarningAlert={setOpenWarningAlert}
                  openSuccessAlert={openSuccessAlert}
                  setOpenSuccessAlert={setOpenSuccessAlert}
                  alertWarningMessage={alertWarningMessage}
                  alertInfoMessage={alertInfoMessage}
                />
                <Collapse in={showRefresh}>
                    <Button
                    onClick={ handleRefresh }
                    variant="contained"
                    color="primary"
                    fullWidth
                    className={buttonStyleClasses.buttonRefresh}
                    startIcon={<SyncIcon size="large" color="primary"/>}
                    >
                    {t("button.refresh")}
                    </Button>
                </Collapse>
            </Paper>
        </Typography>
    );
}
export default ActivitySwitch;