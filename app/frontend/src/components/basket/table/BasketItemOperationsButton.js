import React, { useState } from 'react';
import { useAuth } from '../../../context/AuthContext'
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

    const handleDelete = () => {
        checkExpiredJWTAndExecute(deleteItem);
        setOpenConfirmDialog(false);
    }

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
            }
        );
    }

  return (
    <div>
        <Grid container xs={12} justify="center">
            <Grid item xs={6}>
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