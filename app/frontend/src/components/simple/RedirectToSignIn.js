import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Route } from "react-router-dom";
import SignIn from '../../pages/SignIn';
import SimpleDialog from './SimpleDialog';
import AuthenticationService from '../../services/AuthenticationService';

function RedirectToSignIn() {
    const { t } = useTranslation();
    const [openDialog, setOpenDialog] = useState(true);
    AuthenticationService.signOut();
    return (
        <div>
            <SimpleDialog
                openDialog={openDialog}
                handleCloseDialog={() => setOpenDialog(false)}
                dialogContent={t('dialog.content.jwt-expired')}
                buttonText={t('button.ok')}
            />
            <Route component={SignIn} />
        </div>
    );
}
export default RedirectToSignIn;