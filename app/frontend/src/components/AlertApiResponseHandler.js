import React from 'react';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Alert from '@material-ui/lab/Alert';


function AlertApiResponseHandler({openWarningAlert, setOpenWarningAlert, openSuccessAlert, setOpenSuccessAlert, alertWarningMessage, alertInfoMessage}) {
  return (
    <div>
        <Collapse in={openWarningAlert}>
        <Alert severity="warning" action={
                <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenWarningAlert(false); }}>
                <CloseIcon fontSize="inherit" />
                </IconButton>
        }>
            {alertWarningMessage}
        </Alert>
        </Collapse>
        <Collapse in={openSuccessAlert}>
        <Alert severity="success" action={
                <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenSuccessAlert(false); }}>
                <CloseIcon fontSize="inherit" />
                </IconButton>
        }>
            {alertInfoMessage}
        </Alert>
        </Collapse>
    </div >
  );
}
export default AlertApiResponseHandler;