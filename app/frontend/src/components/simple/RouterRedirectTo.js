import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useHistory } from "react-router-dom";
import { useAuth } from '../../context/AuthContext';
import SimpleDialog from './SimpleDialog';
import AuthenticationService from '../../services/AuthenticationService';

function RouterRedirectTo({dialogContent, page}) {
    const { t } = useTranslation();
    const [openDialog, setOpenDialog] = useState(true);
    const history = useHistory();
    const {setUserIsAuthenticated} = useAuth();

    const handleClose = () => {
        AuthenticationService.signOut();
        setUserIsAuthenticated(false)
        setOpenDialog(false);
        history.push(page);
    }
    
    return (
        <SimpleDialog
            openDialog={openDialog}
            handleCloseDialog={handleClose}
            dialogContent={dialogContent}
            buttonText={t('button.ok')}
        />
    );
}
export default RouterRedirectTo;