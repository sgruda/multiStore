import React, {useState, useEffect} from "react";
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext';

import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableContainer from '@material-ui/core/TableContainer';
import Paper from '@material-ui/core/Paper';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import TablePagination from '@material-ui/core/TablePagination';
import SyncIcon from '@material-ui/icons/Sync';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import PlayArrowIcon from '@material-ui/icons/PlayArrow';
import Collapse from '@material-ui/core/Collapse';

import OrderService from '../../services/OrderService';
import OrdersTableHeader from '../../components/orders/table/OrdersTableHeader';
import OrdersTableBody from '../../components/orders/table/OrdersTableBody';
import { Button } from "@material-ui/core";
import { ROLE_EMPLOYEE } from "../../config/config";
import OrderDetails from '../../components/orders/OrderDetails';
import AlertApiResponseHandler from '../../components/AlertApiResponseHandler';

const useStyles = makeStyles({
    table: {
      minWidth: 650,
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

    doneIcon: {
        color: "#0bb00d",
    },
    clearIcon: {
        color: "#eb1e1e"
    },
    details: {
        backgroundColor: '#e6f3fa',
    },
    buttonRefresh: {
      backgroundColor: "#4285F4",
      "&:hover": {
        backgroundColor: "#2c0fab"
      }
    },
});



function OrderList() {
    const classes = useStyles();
    const { t } = useTranslation();

    const [openWarningAlert, setOpenWarningAlert] = useState(false);
    const [alertWarningMessage, setAlertWarningMessage] = useState('');
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
    const [alertInfoMessage, setAlertInfoMessage] = useState('');
    const [showRefreshButton, setShowRefreshButton] = useState(false);

    const [loadingData, setLoadingData] = useState(true);
    const [refreshDetails, setRefreshDetails] = useState(false);
    const [orders, setOrders] = useState([]);
    const [selectedOrder, setSelectedOrder] = useState(Object);
    const [page, setPage] = useState(0);
    const [totalItems, setTotalPages] = useState(0);
    const [dense, setDense] = useState(false);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    
    const [showDetails, setShowDetails] = useState(false);
    const { activeRole, checkExpiredJWTAndExecute } = useAuth();
  
    const handleClick =  (order) => {
      if(selectedOrder != null && order.id === selectedOrder.id) {
        setSelectedOrder(null);
        setShowDetails(false);
      } else {
        setSelectedOrder(order);
        setShowDetails(true);
      }
    };
    const handleCloseDetails = () => {
      setShowDetails(false);
      setLoadingData(true);
      // setSelectedOrder(null);
    }


    const handleChangePage = (event, newPage) => {
        setLoadingData(true);
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setLoadingData(true);
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleChangeDense = (event) => {
        setDense(event.target.checked);
    };

    const handleRefresh = () => {
      setSelectedOrder(null);
      setDense(false);
      setRowsPerPage(5);
      setPage(0);
      setLoadingData(true);
    }

    const handleRefreshDetails = () => {
      checkExpiredJWTAndExecute(getOrder);
      setShowRefreshButton(false);
    }

    const handleChangeStatus = () => {
      checkExpiredJWTAndExecute(changeStatus);
      setRefreshDetails(true);
    }

    const getNextStatus = (presentStatus) => {
      if(presentStatus === "submitted") return 'prepared';
      else if(presentStatus === "prepared") return 'send';
      else if(presentStatus === "send") return 'delivered';
      else if(presentStatus === "delivered") return '';
    }

    const isSelected = (id) => selectedOrder != null && selectedOrder.id === id ? true : false;
    const emptyRows = rowsPerPage - Math.min(rowsPerPage, totalItems - page * rowsPerPage);

    
    async function getOrders() {
        await OrderService.getAllOrders(page, rowsPerPage)
        .then(response => {
            if (response.status === 200) { 
                const orders = response.data.orders.map(order => {
                    return {
                        id: order.id,
                        identifier: order.identifier,
                        orderDate: order.orderDate,
                        buyerEmail: order.buyerEmail,
                        orderedItemDTOS: order.orderedItemDTOS,
                        totalPrice: order.totalPrice,
                        status: order.status,
                        address: order.address,
                        version: order.version,
                        hash: order.hash,
                    };
                });
                setOrders(orders);
                setPage(response.data.currentPage);
                setTotalPages( response.data.totalItems);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("OrderList: " + resMessage);
            }
        );
    }
    async function getClientOrders() {
        await OrderService.getClientOrders(page, rowsPerPage)
        .then(response => {
            if (response.status === 200) { 
                const orders = response.data.orders.map(order => {
                    return {
                        id: order.id,
                        identifier: order.identifier,
                        orderDate: order.orderDate,
                        buyerEmail: order.buyerEmail,
                        orderedItemDTOS: order.orderedItemDTOS,
                        totalPrice: order.totalPrice,
                        status: order.status,
                        address: order.address,
                        version: order.version,
                        hash: order.hash,
                    };
                });
                setOrders(orders);
                setPage(response.data.currentPage);
                setTotalPages( response.data.totalItems); 
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("OrderList: " + resMessage);
            }
        );
    }
    async function getOrder() {
      await OrderService.getOrder(selectedOrder.identifier)
      .then(response => {
          if (response.status === 200) { 
                  const order = {
                      id: response.data.id,
                      identifier: response.data.identifier,
                      orderDate: response.data.orderDate,
                      buyerEmail: response.data.buyerEmail,
                      orderedItemDTOS: response.data.orderedItemDTOS,
                      totalPrice: response.data.totalPrice,
                      status: response.data.status,
                      address: response.data.address,
                      version: response.data.version,
                      hash: response.data.hash,
                  };
              setSelectedOrder(order);
          }
      },
          (error) => {
          const resMessage =
              (error.response && error.response.data && error.response.data.message) 
              || error.message || error.toString();
              console.error("OrderList: " + resMessage);
          }
      );
  }
    async function changeStatus() {
      await OrderService.changeStatus(selectedOrder)
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
              console.error("OrderList: " + resMessage);
              setAlertWarningMessage(t(error.response.data.message.toString()));
              setOpenWarningAlert(true);
              setShowRefreshButton(true);
          }
      );
  }

    useEffect(() => {
        if (loadingData) {
            if(activeRole === ROLE_EMPLOYEE) {
                checkExpiredJWTAndExecute(getOrders);
                setLoadingData(false);
            }
            else {
                checkExpiredJWTAndExecute(getClientOrders);
                setLoadingData(false);
            }
        }
        if(refreshDetails) {
          setRefreshDetails(false);
          checkExpiredJWTAndExecute(getOrder);
        }
    }, [loadingData, refreshDetails]);


    const headerCells = [
      { id: 'identifier', disablePadding: true, label: t('order.list.table.header.identifier') },
      { id: 'orderDate', disablePadding: false, label: t('order.list.table.header.orderDate') },
      { id: 'buyerEmail', disablePadding: false, label: t('order.list.table.header.buyerEmail') },
      { id: 'orderedTitles', disablePadding: false, label: t('order.list.table.header.orderedTitles') },
      { id: 'totalPrice',  disablePadding: false, label: t('order.list.table.header.totalPrice'), numeric: true },
      { id: 'address',  disablePadding: false, label: t('order.list.table.header.address') },
      { id: 'status',  disablePadding: false, label: t('order.list.table.header.status') },
    ];

  return (
    <div className={classes.root}>
    <Paper className={classes.paper}>
      <TableContainer>
        <Table
          className={classes.table}
          aria-labelledby="tableTitle"
          size={dense ? 'small' : 'medium'}
          aria-label="enhanced table"
        >
          <OrdersTableHeader
            headerCells={headerCells}
            classes={classes}
          />
          <OrdersTableBody
            orders={orders}
            handleClickOrder={handleClick}
            isSelected={isSelected}
            classes={classes}
            emptyRows={emptyRows}
            dense={dense}
          />
          <Dialog
            open={showDetails}
            onClose={handleCloseDetails}
            aria-describedby="dialog-description"
          >
            <DialogContent className={classes.details}>
              <DialogContentText id="dialog-description">
                <OrderDetails
                  order={selectedOrder}
                />
                <AlertApiResponseHandler
                  openWarningAlert={openWarningAlert}
                  setOpenWarningAlert={setOpenWarningAlert}
                  openSuccessAlert={openSuccessAlert}
                  setOpenSuccessAlert={setOpenSuccessAlert}
                  alertWarningMessage={alertWarningMessage}
                  alertInfoMessage={alertInfoMessage}
                />
              </DialogContentText>
            </DialogContent>
            <DialogActions className={classes.details}>
          { activeRole === ROLE_EMPLOYEE && selectedOrder != null && selectedOrder.status != 'delivered'
            ?
              <div>
                <Button onClick={handleChangeStatus} color="primary" autoFocus startIcon={<PlayArrowIcon/>}>
                  {t('button.change-status') + ' "' + t('order.status.' + getNextStatus(selectedOrder.status)) + '"'}
                </Button>
                <Collapse in={showRefreshButton}>
                  <Button
                    onClick={handleRefreshDetails}
                    variant="contained"
                    color="primary"
                    fullWidth
                    className={classes.buttonRefresh}
                    startIcon={<SyncIcon size="large" color="primary"/>}
                  >
                    {t('button.refresh')}
                  </Button>
                </Collapse>
              </div>
            :
              <></>
          }
          <Button onClick={handleCloseDetails} color="primary" autoFocus startIcon={<ExitToAppIcon/>}>
            {t('button.close')}
          </Button>
        </DialogActions>
          </Dialog>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[5, 10, 25]}
        component="div"
        count={totalItems}
        rowsPerPage={rowsPerPage}
        labelRowsPerPage={t('account.list.table.pagination.rows-per-page')}
        page={page}
        onChangePage={handleChangePage}
        onChangeRowsPerPage={handleChangeRowsPerPage}
      />
    </Paper>
    <FormControlLabel
      control={<Switch color="primary" checked={dense} onChange={handleChangeDense} />}
      label={t('densePadding')}
    />
    <Button
      startIcon={<SyncIcon size="large" color="primary"/>}
      onClick={handleRefresh}
    >
      {t('refreshData')}
    </Button>
  </div>
  );
}

export default OrderList;