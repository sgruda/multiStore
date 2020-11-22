import React, {useState, useEffect} from "react";
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import TablePagination from '@material-ui/core/TablePagination';
import SyncIcon from '@material-ui/icons/Sync';

import AccountService from '../services/AccountService';
import AccountsTableHeader from '../components/accounts/table/AccountsTableHeader';
import AccountsTableBody from '../components/accounts/table/AccountsTableBody';
import AccountsTableToolbar from '../components/accounts/table/AccountsTableToolbar';
import { Button } from "@material-ui/core";
import zIndex from "@material-ui/core/styles/zIndex";

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
    toolbar: {
      backgroundColor: 'red',
      minWidth: 650,
    },
});



function AccountsList() {
    const classes = useStyles();

    const [loadingData, setLoadingData] = useState(true);
    const [accounts, setAccounts] = useState([]);
    const [order, setOrder] = useState('asc');
    const [orderBy, setOrderBy] = useState('lastName');
    const [selectedEmail, setSelectedEmail] = useState('');
    const [selectedName, setSelectedName] = useState('');
    const [page, setPage] = useState(0);
    const [totalItems, setTotalPages] = useState(0);
    const [dense, setDense] = useState(false);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [textToSearch, setTextToSearch] = useState(null);
    const [filterActiveAccounts, setFilterActiveAccounts] = useState(null);

    
    const handleClick =  (email, name) => {
      if(email === selectedEmail) {
        setSelectedEmail('');
        setSelectedName('');
      } else {
        setSelectedEmail(email);
        setSelectedName(name);
      }
    };


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

    const handleRequestSort = (event, property) => {
      const isAsc = orderBy === property && order === 'asc';
      setOrder(isAsc ? 'desc' : 'asc');
      setOrderBy(property);
      setLoadingData(true);
    };
    
    const handleSearch = (text, activity) => {
      setTextToSearch(text);
      setFilterActiveAccounts(activity);
      setPage(0);
      setLoadingData(true);
    }

    const handleRefresh = () => {
      setOrder('asc');
      setOrderBy('lastName');
      setSelectedName('');
      setSelectedEmail('');
      setDense(false);
      setTextToSearch(null);
      setRowsPerPage(5);
      setPage(0);
      setFilterActiveAccounts(null);
      setLoadingData(true);
    }

    const isSelected = (mail) => selectedEmail === mail ? true : false;
    const emptyRows = rowsPerPage - Math.min(rowsPerPage, totalItems - page * rowsPerPage);


    
    async function getAccounts() {
        await AccountService.getAccounts(textToSearch, page, rowsPerPage, orderBy + '-' + order, filterActiveAccounts)
        .then(response => {
            if (response.status === 200) { 
                const accounts = response.data.accounts.map(account => {
                    const _roles = [];
                    account.roles.map(role => {
                        let parts = role.split("_");
                        _roles.push(parts[1] + ", ");
                    });
                    return {
                      id: account.idHash,
                      firstName: account.firstName,
                      lastName: account.lastName,
                      email: account.email,
                      active: account.active,
                      roles: _roles
                    };
                });
                setAccounts(accounts);
                setPage(response.data.currentPage);
                setTotalPages( response.data.totalItems);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("AccountList: " + resMessage);
            }
        );
    }

    useEffect(() => {
        if (loadingData) {
            setLoadingData(false);
            getAccounts();
        }
    }, [loadingData]);


    const headerCells = [
      { id: 'firstName', disablePadding: true, label: 'First Name' },
      { id: 'lastName', disablePadding: false, label: 'Last Name' },
      { id: 'email', disablePadding: false, label: 'E-Mail' },
      { id: 'active', disablePadding: false, label: 'Active' },
      { id: 'userRoles',  disablePadding: false, label: 'User Roles' },
    ];

  return (
    <div className={classes.root}>
    <Paper className={classes.paper}>
      <AccountsTableToolbar 
        selectedAccountMail={selectedEmail}
        selectedAccountName={selectedName}
        handleSearch={handleSearch}
        setLoadingAccountList={setLoadingData}
        setSelectedEmail={setSelectedEmail}
      />
      <TableContainer>
        <Table
          className={classes.table}
          aria-labelledby="tableTitle"
          size={dense ? 'small' : 'medium'}
          aria-label="enhanced table"
        >
          <AccountsTableHeader
            headerCells={headerCells}
            classes={classes}
            order={order}
            orderBy={orderBy}
            onRequestSort={handleRequestSort}
            fieldToIgnoreSorting='userRoles'
          />
          <AccountsTableBody
            accounts={accounts}
            handleClickAccount={handleClick}
            isSelected={isSelected}
            classes={classes}
            emptyRows={emptyRows}
            dense={dense}
          />
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[5, 10, 25]}
        component="div"
        count={totalItems}
        rowsPerPage={rowsPerPage}
        page={page}
        onChangePage={handleChangePage}
        onChangeRowsPerPage={handleChangeRowsPerPage}
      />
    </Paper>
    <FormControlLabel
      control={<Switch color="primary" checked={dense} onChange={handleChangeDense} />}
      label="Dense padding"
    />
    <Button
      startIcon={<SyncIcon size="large" color="primary"/>}
      onClick={handleRefresh}
    >
      Refresh data
    </Button>
  </div>
  );
}

export default AccountsList;