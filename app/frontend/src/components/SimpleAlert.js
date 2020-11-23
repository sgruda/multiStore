import React from 'react';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Alert from '@material-ui/lab/Alert';


function SimpleAlert({openAlert, severityAlert, setOpenAlert, alertMessage}) {
  return (
    <div>
        <Collapse in={openAlert}>
        <Alert severity={severityAlert} action={
                <IconButton aria-label="close" color="inherit" size="small" onClick={() => { setOpenAlert(false); }}>
                <CloseIcon fontSize="inherit" />
                </IconButton>
        }>
            {alertMessage}
        </Alert>
        </Collapse>
    </div >
  );
}
export default SimpleAlert;