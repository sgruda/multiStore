import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext'
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Collapse from '@material-ui/core/Collapse';
import Grid from '@material-ui/core/Grid';
import SyncIcon from '@material-ui/icons/Sync';
import PromotionService from '../../services/PromotionService';
import ConfirmDialog from '../ConfirmDialog';
import AlertApiResponseHandler from '../AlertApiResponseHandler';
import DeleteIcon from '@material-ui/icons/Delete';

const useStyles = makeStyles((theme) => ({
      buttonSubmit: {
        backgroundColor: "#51c953",
        "&:hover": {
          backgroundColor: "#0bb00d"
        }
      },
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
    circularProgress: {
        position: 'absolute',
        top: '42%',
        left: '47%',
        margin: theme.spacing(3, 0, 2),
        color: "#4285F4",
      },
}));


function PromotionOperationsButtons({promotion, handleClose}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const {checkExpiredJWTAndExecute} = useAuth();
  
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [showRefresh, setShowRefresh] = useState(false);

    const handleChangeActivity = () => {
        promotion.active ? checkExpiredJWTAndExecute(blockPromotion) : checkExpiredJWTAndExecute(unblockPromotion);
    }

    const handleDelete = () => {
        checkExpiredJWTAndExecute(deletePromotion);
        setOpenConfirmDialog(false);
    }

    async function blockPromotion() {
        await PromotionService.block(promotion)
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
                console.error("PromotionOperationButtonsBlock: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
                setShowRefresh(true);
            }
        );
    }
    async function unblockPromotion() {
        await PromotionService.unblock(promotion)
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
                    console.error("PromotionOperationButtonsUnlock: " + resMessage);
                    setAlertWarningMessage(t(error.response.data.message.toString()));
                    setOpenWarningAlert(true);
                    setShowRefresh(true);
                }
            );
    }
    async function deletePromotion() {
        await PromotionService.delete(promotion)
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
                    console.error("PromotionOperationButtonDelete: " + resMessage);
                    setAlertWarningMessage(t(error.response.data.message.toString()));
                    setOpenWarningAlert(true);
                    setShowRefresh(true);
                }
            );
    }

  return (
    <div>
        <Grid container xs={12} justify="center">
            <Grid item xs={6}>
                <Button
                    onClick={handleChangeActivity}
                    variant="contained"
                    fullWidth
                    color="primary"
                >
                    {promotion != null && promotion.active 
                        ? t('button.block')
                        : t('button.unblock')
                    }
                </Button>
            </Grid>
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
export default PromotionOperationsButtons;