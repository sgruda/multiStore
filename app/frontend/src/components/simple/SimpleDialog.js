import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';


function SimpleDialog({openDialog, handleCloseDialog, dialogContent, buttonText}) {
  return (
    <Dialog
      open={openDialog}
      onClose={handleCloseDialog}
      aria-describedby="dialog-description"
    >
        <DialogContent>
          <DialogContentText id="dialog-description">
            {dialogContent}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary" autoFocus>
            {buttonText}
          </Button>
        </DialogActions>
    </Dialog>
  );
}
export default SimpleDialog;