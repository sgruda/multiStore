import React, {useState, useEffect} from "react";
import { Button } from '@material-ui/core';
import { DataGrid } from '@material-ui/data-grid';
import { useAuth } from "../context/AuthContext";
import { useFields } from '../hooks/FieldHook';





import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import TablePagination from '@material-ui/core/TablePagination';
import Checkbox from '@material-ui/core/Checkbox';
import PropTypes from 'prop-types';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';


import AccountService from '../services/AccountService';

import AccountsTableHeader from '../components/table/AccountsTableHeader'



const useStyles = makeStyles({
    table: {
      minWidth: 650,
    },
    tableCellHeader: {
      fontSize: 15,
      color: "#000000",
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
        backgroundColor: "#9cd2f0",
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



function AccountList() {
    const classes = useStyles();

    const [firstLoading, setFirstLoading] = useState(true);
    const [loadingData, setLoadingData] = useState(true);
    const [accounts, setAccounts] = useState([]);
    const [order, setOrder] = useState('asc');
    const [orderBy, setOrderBy] = useState('lastName');
    const [selectedId, setSelectedId] = useState('');
    const [page, setPage] = useState(0);
    const [totalItems, setTotalPages] = useState(0);
    const [dense, setDense] = useState(false);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [textToSearch, setTextToSearch] = useState(null);
    const [filterActiveAccounts, setFilterActiveAccounts] = useState(null);

    
    const handleClick = (event, id) => {
      id === selectedId ? setSelectedId('') : setSelectedId(id);
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

    const isSelected = (name) => selectedId === name ? true : false;
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
            }
        );
    }

    useEffect(() => {
        if (loadingData) {
            setLoadingData(false);
            getAccounts();
        }
    }, [page, rowsPerPage, accounts, order, orderBy]);


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
      {/* <EnhancedTableToolbar numSelected={selected.length} /> */}
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
          <TableBody>
            {accounts.map((account) => {
              const isItemSelected = isSelected(account.id);
              return (
                <TableRow key={account.id}
                  hover
                  onClick={(event) => handleClick(event, account.id)}
                  aria-checked={isItemSelected}
                  tabIndex={-1}
                  key={account.id}
                  selected={isItemSelected}
                  classes={{ hover: classes.hover }}
                  className={classes.tableRow}
                >
                    <TableCell align="center" className={classes.tableCell}>
                        {account.firstName}
                    </TableCell>
                    <TableCell align="center" className={classes.tableCell}>{account.lastName}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{account.email}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>
                        {account.active 
                        ? <DoneIcon className={classes.doneIcon}/> 
                        : <ClearIcon className={classes.clearIcon}/> }
                        </TableCell>
                    <TableCell className={classes.tableCell} align="center">{account.roles}</TableCell>
                </TableRow>
                );
            })}
            {emptyRows > 0 && (
              <TableRow style={{ height: (dense ? 33 : 53) * emptyRows }}>
                <TableCell colSpan={6} />
              </TableRow>
            )}
          </TableBody>
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
      control={<Switch checked={dense} onChange={handleChangeDense} />}
      label="Dense padding"
    />
  </div>
  );
}

export default AccountList;