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

import AccountService from '../../services/AccountService';
import AccountsTableHeader from '../../components/accounts/table/AccountsTableHeader';
import AccountsTableBody from '../../components/accounts/table/AccountsTableBody';
import AccountsTableToolbar from '../../components/accounts/table/AccountsTableToolbar';
import { Button } from "@material-ui/core";

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
});



function AccountsList() {
    const classes = useStyles();
    const { t } = useTranslation();

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
    
    const {checkExpiredJWTAndExecute} = useAuth();
  
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
        await AccountService.getAccounts(textToSearch === '' ? null : textToSearch, page, rowsPerPage, orderBy + '-' + order, filterActiveAccounts)
        .then(response => {
            if (response.status === 200) { 
                const accounts = response.data.accounts.map(account => {
                    const _roles = [];
                    account.roles.map(role => {
                        let parts = role.split("_");
                        _roles.push(t('account.access-level.names.' + parts[1].toLowerCase()) + ", ");
                    });
                    return {
                      id: account.id,
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
            checkExpiredJWTAndExecute(getAccounts);
        }
    }, [loadingData]);


    const headerCells = [
      { id: 'firstName', disablePadding: true, label: t('account.list.table.header.firstName') },
      { id: 'lastName', disablePadding: false, label: t('account.list.table.header.lastName') },
      { id: 'email', disablePadding: false, label: t('account.list.table.header.email') },
      { id: 'active', disablePadding: false, label: t('account.list.table.header.activity') },
      { id: 'userRoles',  disablePadding: false, label: t('account.list.table.header.roles') },
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

export default AccountsList;