import React from 'react';
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
                Are you sure?
            </DialogContentText>
            </DialogContent>
            <DialogActions>
            <Button onClick={handleConfirm} color="primary" autoFocus startIcon={<DoneIcon/>}>
                Yes
            </Button>
            <Button onClick={handleOpenConfirmDialog} color="primary" autoFocus startIcon={<CloseIcon/>}>
                No
            </Button>
            </DialogActions>
        </Dialog>
    </div >
  );
}
export default ConfirmDialog;