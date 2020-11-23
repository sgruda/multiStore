import React from 'react';
import SimpleAlert from './simple/SimpleAlert';

function AlertApiResponseHandler({openWarningAlert, setOpenWarningAlert, openSuccessAlert, setOpenSuccessAlert, alertWarningMessage, alertInfoMessage}) {
  return (
    <div>
        <SimpleAlert
          openAlert={openWarningAlert}
          severityAlert="warning"
          setOpenAlert={setOpenWarningAlert}
          alertMessage={alertWarningMessage}
        />
       <SimpleAlert
          openAlert={openSuccessAlert}
          severityAlert="success"
          setOpenAlert={setOpenSuccessAlert}
          alertMessage={alertInfoMessage}
        />
    </div >
  );
}
export default AlertApiResponseHandler;