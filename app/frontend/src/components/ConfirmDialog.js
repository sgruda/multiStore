import React from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import { Backdrop } from '@material-ui/core';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';

function ConfirmDialog({openConfirmDialog, setOpenConfirmDialog, handleConfirmAction}) {
  const { t } = useTranslation();

  const handleOpenConfirmDialog = () => {
    setOpenConfirmDialog(!openConfirmDialog);
  }

  const handleConfirm = () => {
    handleConfirmAction()
    setOpenConfirmDialog(!openConfirmDialog);
  }


  return (
    <div>
        <Dialog
            open={openConfirmDialog}
            onClose={handleOpenConfirmDialog}
            aria-describedby="dialog-description"
        >
            <Backdrop open={openConfirmDialog}/>
            <DialogContent>
            <DialogContentText id="dialog-description">
                {t('dialog.content.confirm')}
            </DialogContentText>
            </DialogContent>
            <DialogActions>
            <Button onClick={handleConfirm} color="primary" autoFocus startIcon={<DoneIcon/>}>
                {t('button.yes')}
            </Button>
            <Button onClick={handleOpenConfirmDialog} color="primary" autoFocus startIcon={<CloseIcon/>}>
                {t('button.no')}
            </Button>
            </DialogActions>
        </Dialog>
    </div >
  );
}
export default ConfirmDialog;