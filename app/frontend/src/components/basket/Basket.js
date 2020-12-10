import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext'
import { ROLE_CLIENT, ACTIVE_ROLE } from '../../config/config';
import BasketService from '../../services/BasketService';
import BasketDetails from '../../components/basket/BasketDetails';

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
}));

function Basket() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [loadingData, setLoadingData] = useState(false);
    const [checkBasketSize, setCheckBasketSize] = useState(true);
    const [basketSize, setBasketSize] = useState(0);
    const [showDetails, setShowDetails] = useState(false);
    const { checkExpiredJWTAndExecute } = useAuth(); 
    
    const handleClickBasket = () => {
        setShowDetails(true);
    }
    const handleCloseDetails = () => {
        setShowDetails(false);
        setCheckBasketSize(true);
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

    useEffect(() => {
        if (loadingData) {
            setLoadingData(false);
            // checkExpiredJWTAndExecute();
        }
        if (checkBasketSize) {
            setCheckBasketSize(false);
            checkExpiredJWTAndExecute(getBasketSize);
        }
    }, [loadingData, checkBasketSize]);

    return (
        <div> 
            <Fab color="primary" aria-label="basket" className={classes.fab} onClick={handleClickBasket}> 
                <Badge badgeContent={basketSize} color="error">
                    <ShoppingBasketIcon className={classes.fabIcon}/>
                </Badge>
            </Fab>
            <Dialog
                open={showDetails}
                onClose={handleCloseDetails}
                aria-describedby="dialog-description"
            >
                <DialogContent className={classes.details}>
                <DialogContentText id="dialog-description">
                    <BasketDetails/>
                </DialogContentText>
                </DialogContent>
                <DialogActions className={classes.details}>
                <Button onClick={handleCloseDetails} color="primary" autoFocus>
                    {t('button.close')}
                </Button>
                </DialogActions>
            </Dialog>
        </div >
    );
}
export default Basket;