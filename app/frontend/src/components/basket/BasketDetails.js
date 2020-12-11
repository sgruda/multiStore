import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext'
import { ROLE_CLIENT, ACTIVE_ROLE } from '../../config/config';
import BasketService from '../../services/BasketService';
import BasketTableBody from './table/BasketTableBody';
import BasketTableHeader from './table/BasketTableHeader';
import BasketItemOperationsButton from './table/BasketItemOperationsButton';

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
import { Collapse } from '@material-ui/core';

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
    table: {
        minWidth: 550,
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
    const [basket, setBasket] = useState(Object);
    const [orderedItems, setOrderedItems] = useState([]);

    const [openWarningAlert, setOpenWarningAlert] = useState(false);
    const [alertWarningMessage, setAlertWarningMessage] = useState('');
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
    const [alertInfoMessage, setAlertInfoMessage] = useState('');
    const [showRefresh, setShowRefresh] = useState(false);
    const {checkExpiredJWTAndExecute} = useAuth();

    const [selectedItem, setSelectedItem] = useState(null);
    const [showOperations, setShowOperations] = useState(false);

    const handleDeleteItem = () => {
        checkExpiredJWTAndExecute(deleteItem);
        // setLoadingData(true);
    }
    const handleCloseSuccessDialog = () => {
        setSelectedItem(null);
        setShowOperations(false)
        setLoadingData(true);
    }

    const handleClickItem = (item) => {
        if(selectedItem != null && selectedItem.id === item.id) {
            setSelectedItem(null);
            setShowOperations(false)
        } else {
            setSelectedItem(item);
            setShowOperations(true)
        }
    }
    
    const isSelectedItem = (id) => {
        if(selectedItem != null)
            return selectedItem.id === id ? true : false;
        else 
            return false;
    }

    const handleCloseOperations = () => {
        setShowOperations(false);
        setLoadingData(true);
    }

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
                setBasket(response.data);
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


    async function deleteItem() {
        await BasketService.deleteItemFromBasket(basket, selectedItem)
        .then(response => {
            if (response.status === 200) { 
                setOpenSuccessAlert(true);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("BasketTableBodyDelete: " + resMessage);
                setAlertWarningMessage(t(error.response.data.message.toString()));
                setOpenWarningAlert(true);
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
                    // handleDeleteItem={handleDeleteItem}
                    handleClickItem={handleClickItem}
                    isSelected={isSelectedItem}
                />
                </Table>
            </TableContainer>
            <Collapse
                in={showOperations}
            >
                <BasketItemOperationsButton
                    basket={basket}
                    item={selectedItem}
                    handleClose={handleCloseOperations}
                    deleteItem={handleDeleteItem}
                    handleCloseSuccessDialog={handleCloseSuccessDialog}
                />    
            </Collapse>
        </Paper >
    );
}
export default BasketDetails;