import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext'
import { ROLE_CLIENT, ACTIVE_ROLE } from '../config/config';
import BasketService from '../services/BasketService';

import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Backdrop from '@material-ui/core/Backdrop';
import Fab from '@material-ui/core/Fab';
import Badge from '@material-ui/core/Badge';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';

const useStyles = makeStyles((theme) => ({
    // root: {
    //   '& > *': {
    //     margin: theme.spacing(1),
    //   },
    // },
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
    const { checkExpiredJWTAndExecute } = useAuth(); 
    
    const handleClickBasket = () => {
        alert("hello, to twój koszyk")
    }


    async function getBasketSize() {
        await BasketService.getBasketSize()
        .then(response => {
            if (response.status === 200) { 
                console.log(response.data)
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
        </div >
    );
}
export default Basket;