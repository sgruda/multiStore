import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext'
import { ROLE_CLIENT, ACTIVE_ROLE } from '../config/config';

import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Backdrop from '@material-ui/core/Backdrop';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';
import Fab from '@material-ui/core/Fab';

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

function Basket({fabPosition}) {
    const classes = useStyles();
    const { t } = useTranslation();
    // const { checkExpiredJWTAndExecute } = useAuth(); 
    
    const handleClickBasket = () => {
        alert("hello, to twój koszyk")
    }


    return (
        <div> 
            <Fab color="primary" aria-label="basket" className={classes.fab} onClick={handleClickBasket}>
                <ShoppingBasketIcon className={classes.fabIcon}/>
            </Fab>
        </div >
    );
}
export default Basket;