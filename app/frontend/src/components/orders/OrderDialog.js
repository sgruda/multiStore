import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import { Backdrop } from '@material-ui/core';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';
import TextField from '@material-ui/core/TextField';
import AlertApiResponseHandler from '../AlertApiResponseHandler';
import BasketService from '../../services/BasketService';
import OrderService from '../../services/OrderService';
import ConfirmDialog from '../ConfirmDialog';

function OrderDialog({openDialog, handleClose, basket}) {
    const { t } = useTranslation();
    const [address, setAddress] = useState('');
    const [openWarningAlert, setOpenWarningAlert] = useState(false);
    const [alertWarningMessage, setAlertWarningMessage] = useState('');
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
    const [alertInfoMessage, setAlertInfoMessage] = useState('');
    const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
    const {checkExpiredJWTAndExecute} = useAuth();


    const handleConfirm = () => {
        setOpenConfirmDialog(!openConfirmDialog);
    }
    const handleSubmit = () => {
        checkExpiredJWTAndExecute(submitOrder);
    }

    const checkErrors = () => {
        if(address == '')
            return true ;
        return false;
    }

    const addressIsWrong = checkErrors();

    async function submitOrder() {
        await OrderService.submit(basket, address)
        .then(response => {
            if (response.status === 200) { 
                setOpenSuccessAlert(true);
                setAlertInfoMessage(t('response.ok'));
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("OrderDialog: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
            }
        );
    }

  return (
    <div>
        <Dialog
            open={openDialog}
            onClose={handleClose}
            aria-describedby="dialog-description"
        >
            <DialogContent>
            <DialogContentText id="dialog-description">
                {t('dialog.content.order-address')}
            </DialogContentText>
            <TextField
                    value={ address }
                    onChange={ (event) => setAddress(event.target.value) }
                    variant="outlined"
                    required
                    fullWidth
                    id="address"
                    label={t('order.submit.address')}
                    name="address"
                    autoComplete="address"
                    error={addressIsWrong}
            /> 
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
                handleConfirmAction={handleSubmit}
              />
            </DialogContent>
            <DialogActions>
            <Button onClick={handleConfirm} autoFocus startIcon={<DoneIcon/>} disabled={addressIsWrong}>
                {t('button.order')}
            </Button>
            <Button onClick={handleClose} startIcon={<CloseIcon/>}>
                {t('button.back')}
            </Button>
            </DialogActions>
        </Dialog>
    </div >
  );
}
export default OrderDialog;