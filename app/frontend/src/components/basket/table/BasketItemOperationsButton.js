import React, { useState, useEffect } from 'react';
import { useAuth } from '../../../context/AuthContext';
import { useForm } from "react-hook-form";

import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Collapse from '@material-ui/core/Collapse';
import Grid from '@material-ui/core/Grid';
import SyncIcon from '@material-ui/icons/Sync';
import BasketService from '../../../services/BasketService';
import ConfirmDialog from '../../ConfirmDialog';
import AlertApiResponseHandler from '../../AlertApiResponseHandler';
import DeleteIcon from '@material-ui/icons/Delete';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import TextField from '@material-ui/core/TextField';

const useStyles = makeStyles((theme) => ({
      buttonDelete: {
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
      buttonEdit: {
        backgroundColor: "#51c953",
        "&:hover": {
          backgroundColor: "#0bb00d"
        }
      },
}));


function BasketItemOperationsButton({basket, item, handleClose, handleCloseSuccessDialog}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const {checkExpiredJWTAndExecute} = useAuth();
  
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [showRefresh, setShowRefresh] = useState(false);

  const [orderedNumber, setOrderedNumber] = useState(0);

    const handleDelete = () => {
        checkExpiredJWTAndExecute(deleteItem);
        setOpenConfirmDialog(false);
    }

    const handleChangeNumber = () => {
        item.orderedNumber = orderedNumber;
        checkExpiredJWTAndExecute(editOrderedNumberItem)
    }

    const checkErrors = () => {
        if(orderedNumber <= 0)
          return true ;
        return false;
      }
    
    const orderedNumberIsWrong = checkErrors();

    async function deleteItem() {
        await BasketService.deleteItemFromBasket(basket, item)
        .then(response => {
            if (response.status === 200) { 
                setAlertInfoMessage(t('response.ok'))
                setOpenSuccessAlert(true);
                handleCloseSuccessDialog();
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("BasketTableBodyDelete: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
                setShowRefresh(true);
            }
        );
    }
    async function editOrderedNumberItem() {
        await BasketService.editItemInBasket(item)
        .then(response => {
            if (response.status === 200) { 
                setAlertInfoMessage(t('response.ok'))
                setOpenSuccessAlert(true);
                handleCloseSuccessDialog();
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("BasketTableBodyEdit: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
                setShowRefresh(true);
            }
        );
    }

    useEffect(() => {
        item != null ? setOrderedNumber(item.orderedNumber) : setOrderedNumber(0);
    }, [item]);

  return (
    <div>
        <Grid container xs={12} justify="center" direction="column" spacing={2}>
            <Grid item xs={12}>
                <Button
                    onClick={() => setOpenConfirmDialog(true)}
                    variant="contained"
                    color="primary"
                    fullWidth
                    className={classes.buttonDelete}
                    startIcon={<DeleteIcon size="large"/>}
                >
                {t('button.delete')}
                </Button>
            </Grid>
            <Grid item xs={12}>
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
            </Grid>
            <Grid item xs={12}>
                <Button
                    onClick={handleChangeNumber}
                    variant="contained"
                    color="primary"
                    fullWidth
                    disabled={orderedNumberIsWrong}
                    className={classes.buttonEdit}
                    // startIcon={<DeleteIcon size="large"/>}
                >
                {t('button.edit')}
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
                    {t('button.refresh')}
                    </Button>
                </Collapse>
            </Grid>
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
                handleConfirmAction={handleDelete}
            />
        </Grid>
    </div >
  );
}
export default BasketItemOperationsButton;