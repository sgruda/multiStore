import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext'
import BasketService from '../../services/BasketService';
import BasketTableBody from './table/BasketTableBody';
import BasketTableHeader from './table/BasketTableHeader';
import BasketItemOperationsButton from './table/BasketItemOperationsButton';

import { makeStyles } from '@material-ui/core/styles';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';
import Paper from '@material-ui/core/Paper';
import TableContainer from '@material-ui/core/TableContainer';
import Table from '@material-ui/core/Table';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import { Collapse } from '@material-ui/core';
import OrderService from '../../services/OrderService';

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

function BasketDetails({loadingData, setLoadingData, basket, setBasket}) {
    const classes = useStyles();
    const { t } = useTranslation();
    const [orderedItems, setOrderedItems] = useState([]);
    const [totalPrice, setTotalPrice] = useState(-1);

    const [firstLoading, setFirstLoading] = useState(true);
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
    const {checkExpiredJWTAndExecute} = useAuth();

    const [selectedItem, setSelectedItem] = useState(null);
    const [showOperations, setShowOperations] = useState(false);

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
    }

    const handleRefresh = () => {
        setLoadingData(true);
        handleCloseOperations();
    }

    async function getItems() {
        await BasketService.getBasket()
        .then(response => {
            if (response.status === 200) { 
                const orderedItems = response.data.orderedItemDTOS.map(item => {
                    return {
                      id: item.id,
                      identifier: item.identifier,
                      orderedNumber: item.orderedNumber,
                      orderedProduct: item.orderedProduct,
                      version: item.version,
                      hash: item.hash,
                    };
                });
                setBasket(response.data);
                setOrderedItems(orderedItems);
                getTotalPrice(response.data);
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

    async function getTotalPrice(basket2) {
        await OrderService.getTotalPrice(basket2)
        .then(response => {
            if (response.status === 200) { 
                setTotalPrice(response.data.totalPrice);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("BasketDetails TotalPrice: " + resMessage);
            }
        );
    }

    useEffect(() => {
        if (loadingData || firstLoading) {
            setLoadingData(false);
            setFirstLoading(false);
            checkExpiredJWTAndExecute(getItems);
        }
    }, [loadingData, firstLoading]);

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
                    handleClickItem={handleClickItem}
                    isSelected={isSelectedItem}
                    totalPrice={totalPrice}
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
                    handleCloseSuccessDialog={handleCloseSuccessDialog}
                    handleRefresh={handleRefresh}
                />    
            </Collapse>
        </Paper >
    );
}
export default BasketDetails;