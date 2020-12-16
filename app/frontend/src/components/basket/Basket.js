import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext'
import { ROLE_CLIENT, ACTIVE_ROLE } from '../../config/config';
import BasketService from '../../services/BasketService';
import BasketDetails from '../../components/basket/BasketDetails';
import OrderDialog from '../../components/orders/OrderDialog';

import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Fab from '@material-ui/core/Fab';
import Badge from '@material-ui/core/Badge';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import Collapse from '@material-ui/core/Collapse';
import SyncIcon from '@material-ui/icons/Sync';

const useStyles = makeStyles((theme) => ({
    fab: {
        position: 'fixed',
        bottom: theme.spacing(4),
        right: theme.spacing(4),
        width: '80px',
        height: '80px',
        backgroundColor: '#3523ba',
        "&:hover": {
            backgroundColor: "#4dc4ff"
        }
    },
    fabIcon: {
        width: '50px',
        height: '50px',
        color: '#d3ebf8'
    },
    dialog: {
        // minWidth: '2000px',
    },
    details: {
        // minWidth: '650px',
    },
    buttonOrder: {
        minWidth: '100px',
        color: 'white',
        backgroundColor: "#51c953",
        "&:hover": {
          backgroundColor: "#0bb00d"
        }
    },
    buttonClose: {
        minWidth: '100px',
        color: 'white',
        backgroundColor: "#e35656",
        "&:hover": {
            backgroundColor: "#eb1e1e"
        }
    },
    buttonRefresh: {
        minWidth: '100px',
        color: 'white',
        backgroundColor: "#4285F4",
        "&:hover": {
          backgroundColor: "#2c0fab"
        }
    },
}));

function Basket({checkSize, setCheckSize}) {
    const classes = useStyles();
    const { t } = useTranslation();
    const [loadingData, setLoadingData] = useState(true);
    const [basket, setBasket] = useState(Object);
    const [basketSize, setBasketSize] = useState(0);
    const [showDetails, setShowDetails] = useState(false);
    const [showOrderDialog, setShowOrderDialog] = useState(false);
    const { checkExpiredJWTAndExecute } = useAuth(); 
    
    const handleClickBasket = () => {
        setShowDetails(true);
    }
    const handleCloseDetails = () => {
        setShowDetails(false);
        setCheckSize(true);
    }
    const handleOrder = () => {
        setShowOrderDialog(true);
    }
    const handleCloseOrderDialog = () => {
        setShowOrderDialog(false);
        setLoadingData(true);
    }
    const handleRefresh = () => {
        setLoadingData(true);
        setCheckSize(true);
    }


    async function getBasketSize() {
        await BasketService.getBasketSize()
        .then(response => {
            if (response.status === 200) { 
                setBasketSize(response.data.size);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("BasketSize: " + resMessage);
            }
        );
    }

    async function getBasket() {
        await BasketService.getBasket()
        .then(response => {
            if (response.status === 200) { 
                setBasket(response.data);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("Basket: " + resMessage);
            }
        );
    }

    useEffect(() => {
        if (loadingData) {
            setLoadingData(false);
            checkExpiredJWTAndExecute(getBasket);
        }
        if (checkSize) {
            setCheckSize(false);
            checkExpiredJWTAndExecute(getBasketSize);
        }
    }, [loadingData, checkSize]);

    return (
        <div > 
            <Fab color="primary" aria-label="basket" className={classes.fab} onClick={handleClickBasket}> 
                <Badge badgeContent={basketSize} color="error">
                    <ShoppingBasketIcon className={classes.fabIcon}/>
                </Badge>
            </Fab>
            <Dialog
                open={showDetails}
                onClose={handleCloseDetails}
                fullScreen={true}
            >
                <DialogContent>
                    <BasketDetails
                        loadingData={loadingData}
                        setLoadingData={setLoadingData}
                        basket={basket}
                        setBasket={setBasket}
                    />
                    <OrderDialog
                        openDialog={showOrderDialog}
                        handleClose={handleCloseOrderDialog}
                        basket={basket}
                    />
                </DialogContent>
                <DialogActions>
                <Button onClick={handleRefresh} startIcon={<SyncIcon/>} className={classes.buttonRefresh}>
                    {t('button.refresh')}
                </Button>
                <Button onClick={handleOrder} color="primary" autoFocus className={classes.buttonOrder}>
                    {t('button.order')}
                </Button>
                <Button onClick={handleCloseDetails} color="primary" className={classes.buttonClose}>
                    {t('button.close')}
                </Button>
                </DialogActions>
            </Dialog>
        </div >
    );
}
export default Basket;