import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext'
import { ROLE_CLIENT, ACTIVE_ROLE } from '../../config/config';
import BasketService from '../../services/BasketService';
import BasketTableBody from './table/BasketTableBody';
import BasketTableHeader from './table/BasketTableHeader';

import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Backdrop from '@material-ui/core/Backdrop';
import Fab from '@material-ui/core/Fab';
import Badge from '@material-ui/core/Badge';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';
import Paper from '@material-ui/core/Paper';
import TableContainer from '@material-ui/core/TableContainer';
import Table from '@material-ui/core/Table';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';

const useStyles = makeStyles((theme) => ({
    paper: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
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
    details: {
        backgroundColor: '#e6f3fa',
    },
    table: {
        minWidth: 450,
    },
    tableCellHeader: {
        fontSize: 15,
        backgroundColor: "#66bae8",
    },
    tableRow: {
        "&.Mui-selected, &.Mui-selected:hover": {
            backgroundColor: "#7cc3eb",
            "& > .MuiTableCell-root": {
            }
    }
    },
    tableCell: {
        "$hover:hover &": {
            backgroundColor: "#d3ebf8",
        }
    },
    hover: {},
    avatar: {
        margin: theme.spacing(2),
        width: theme.spacing(7),
        height: theme.spacing(7),
        backgroundColor: theme.palette.primary.main,
      },

}));

function BasketDetails() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [loadingData, setLoadingData] = useState(true);   
    const [orderedItems, setOrderedItems] = useState([]);

    const {checkExpiredJWTAndExecute} = useAuth();

    async function getItems() {
        await BasketService.getBasket()
        .then(response => {
            if (response.status === 200) { 
                const orderedItems = response.data.orderedItemDTOS.map(item => {
                    return {
                      id: item.idHash,
                      identifier: item.identifier,
                      orderedNumber: item.orderedNumber,
                      orderedProduct: item.orderedProduct,
                      version: item.version,
                      signature: item.signature,
                    };
                });
                setOrderedItems(orderedItems);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("BasketDetails: " + resMessage);
            }
        );
    }
    
    useEffect(() => {
        if (loadingData) {
            setLoadingData(false);
            checkExpiredJWTAndExecute(getItems);
        }
    }, [loadingData]);

    const headerCells = [
        { id: 'title', disablePadding: false, label: t('basket.header.title') },
        { id: 'type', disablePadding: false, label: t('basket.header.type.label') },
        { id: 'category', disablePadding: false, label: t('basket.header.category') },
        { id: 'price',  disablePadding: false, label: t('basket.header.price'), numeric: true },
        { id: 'orderedNumber', disablePadding: true, label: t('basket.header.orderedNumber'), numeric: true},
      ];

    return (
        <Paper className={classes.paper}>
            <Avatar className={classes.avatar}>
              <ShoppingBasketIcon className={classes.fabIcon}/>
            </Avatar>
            <Typography component="h1" variant="h5">
              {t('pages.titles.basket.content')}
            </Typography>
            <TableContainer>
                <Table
                    className={classes.table}
                    aria-labelledby="tableTitle"
                    aria-label="enhanced table"
                >
                <BasketTableHeader
                    headerCells={headerCells}
                    classes={classes}
                />
                <BasketTableBody
                    orderedItems={orderedItems}
                    classes={classes}
                />
                </Table>
            </TableContainer>
        </Paper >
    );
}
export default BasketDetails;