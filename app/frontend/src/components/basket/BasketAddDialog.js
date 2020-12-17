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
import { ROLE_CLIENT } from '../../config/config';
import BasketService from '../../services/BasketService';

function BasketAddDialog({openDialog, handleClose, getProduct, setOpenWarningAlert, setOpenSuccessAlert, setAlertWarningMessage, setAlertInfoMessage, loadingData, setLoadingData}) {
    const { t } = useTranslation();
    const [orderedNumber, setOrderedNumber] = useState(1);
    const [basket, setBasket] = useState(Object);
    const { checkExpiredJWTAndExecute } = useAuth();
    const product = getProduct();

    const handleConfirm = () => {
        checkExpiredJWTAndExecute(addItem);
        // setLoadingData(true);
        handleClose();
    }
    const checkErrors = () => {
        if(orderedNumber <= 0)
        return true ;
        return false;
    }

    const orderedNumberIsWrong = checkErrors();

    async function addItem() {
        await BasketService.addToBasket(basket, product, orderedNumber)
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
                console.error("BaskeAddDialog: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
            }
        );
    }

    async function getBasket() {
        await BasketService.getBasket()
        .then(response => {
            if (response.status === 200) { 
                setBasket(response.data);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("BasketAddDialog: " + resMessage);
            }
        );
    }
    useEffect(() => {
        if (loadingData) {
            setLoadingData(false);
            checkExpiredJWTAndExecute(getBasket);
        }
    }, [loadingData]);

  return (
    <div>
        <Dialog
            open={openDialog}
            onClose={handleConfirm}
            aria-describedby="dialog-description"
        >
            <Backdrop open={openDialog}/>
            <DialogContent>
            <DialogContentText id="dialog-description">
                {t('dialog.content.how-many-items')}
            </DialogContentText>
            <TextField
                    value={ orderedNumber }
                    onChange={ (event) => setOrderedNumber(event.target.value) }
                    variant="outlined"
                    type="number"
                    required
                    fullWidth
                    id="orderedNumber"
                    label={t('basket.edit.orderedNumber')}
                    name="orderedNumber"
                    autoComplete="orderedNumber"
                    error={orderedNumberIsWrong}
            /> 
            </DialogContent>
            <DialogActions>
            <Button onClick={handleConfirm} color="primary" autoFocus startIcon={<DoneIcon/>}>
                {t('button.ok')}
            </Button>
            <Button onClick={handleClose} color="primary" autoFocus startIcon={<CloseIcon/>}>
                {t('button.back')}
            </Button>
            </DialogActions>
        </Dialog>
    </div >
  );
}
export default BasketAddDialog;