import React, {useState, useEffect} from "react";
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../../context/AuthContext';
import AlertApiResponseHandler from '../../AlertApiResponseHandler';

import { Button } from "@material-ui/core";
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Collapse from '@material-ui/core/Collapse';
import SyncIcon from '@material-ui/icons/Sync';
import { makeStyles } from '@material-ui/core/styles';
import ProductService from "../../../services/ProductService";

const useStyles = makeStyles(({
    buttonRefresh: {
      backgroundColor: "#4285F4",
      "&:hover": {
        backgroundColor: "#2c0fab"
      }
    },
}));

function ProductActivitySwitch({classes, product, setLoadingData}) {
    const buttonStyleClasses = useStyles();
    const { t } = useTranslation();
    const [productActivity, setProductActivity] = useState(false);

    const [openWarningAlert, setOpenWarningAlert] = useState(false);
    const [alertWarningMessage, setAlertWarningMessage] = useState('');
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
    const [alertInfoMessage, setAlertInfoMessage] = useState('');
    const [showRefresh, setShowRefresh] = useState(false);

    const {checkExpiredJWTAndExecute} = useAuth();

    const handleActivityChange = () => {
        setProductActivity((prev) => !prev)
        if(productActivity)
            checkExpiredJWTAndExecute(blockProduct);
        else
            checkExpiredJWTAndExecute(unblockProduct);
    };

    const handleRefresh = () => {
        setLoadingData(true);
        setShowRefresh(false);
    }

    async function blockProduct() {
        await ProductService.block(product)
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
                console.error("ProductActivitySwitch: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
                setShowRefresh(true);
            }
        );
    }
    async function unblockProduct() {
        await ProductService.unblock(product)
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
                console.error("Activity ProductActivitySwitch: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
                setShowRefresh(true);
            }
        );
    }

    useEffect(() => {
        setProductActivity(product.active);
    }, [product]);

    return (
        <h>
            <FormControlLabel
                control={<Switch color="primary" checked={productActivity} onChange={handleActivityChange} />}
            />
            <AlertApiResponseHandler
                openWarningAlert={openWarningAlert}
                setOpenWarningAlert={setOpenWarningAlert}
                openSuccessAlert={openSuccessAlert}
                setOpenSuccessAlert={setOpenSuccessAlert}
                alertWarningMessage={alertWarningMessage}
                alertInfoMessage={alertInfoMessage}
            />
            <Collapse in={showRefresh}>
                <Button
                onClick={ handleRefresh }
                variant="contained"
                color="primary"
                fullWidth
                className={buttonStyleClasses.buttonRefresh}
                startIcon={<SyncIcon size="large" color="primary"/>}
                >
                {t("button.refresh")}
                </Button>
            </Collapse>
         </h>
    );
}
export default ProductActivitySwitch;