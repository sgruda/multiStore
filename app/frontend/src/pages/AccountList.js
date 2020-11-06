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




import AccountService from '../services/AccountService';




const useStyles = makeStyles({
    table: {
      minWidth: 650,
    },


    doneIcon: {
        color: "#0bb00d",
    },
    clearIcon: {
        color: "#eb1e1e"
    },
  });



function AccountList() {
    const classes = useStyles();

    const [loadingData, setLoadingData] = useState(true);
    const [accounts, setAccounts] = useState([]);
    const [page, setPage] = React.useState(1);
    const paginationInfo = [{
        currentPage: 0,
        totalItems: 0,
        totalPages: 0
    }];
    const requestParams = [{
        textToSearch: null,
        page: 0,
        size: 5,
        sort: [],
        active: null
    }];

    const handlePageChange = (params) => {
        setPage(params.page);
    };

    async function getAccounts() {
        // setLoadingData(true);
        await AccountService.getAccounts()
        .then(response => {
            if (response.status === 200) { 
                const accounts = response.data.accounts.map(account => {
                    return {
                      id: account.idHash,
                      firstName: account.firstName,
                      lastName: account.lastName,
                      email: account.email,
                      active: account.active,
                      roles: account.roles
                    };
                });
                setAccounts(accounts);
                paginationInfo.currentPage = response.data.currentPage;
                paginationInfo.totalItems = response.data.totalItems;
                paginationInfo.totalPages = response.data.totalPages;
                // setLoadingData(false);
                console.log("loading " +loadingData)
                console.log("data " + response.data.totalItems)
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
        console.log("loading " +loadingData)
        if (loadingData) {
            setLoadingData(false);
            getAccounts();
        }
    }, [page, accounts]);

  return (
    <TableContainer component={Paper}>
      <Table className={classes.table} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell align="center">First Name</TableCell>
            <TableCell align="center">Last Name</TableCell>
            <TableCell align="center">Email</TableCell>
            <TableCell align="center">Active</TableCell>
            <TableCell align="center">Roles</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {accounts.map((account) => (
            <TableRow key={account.idHash}>
                <TableCell component="th" scope="row">
                    {account.firstName}
                </TableCell>
                <TableCell align="center">{account.lastName}</TableCell>
                <TableCell align="center">{account.email}</TableCell>
                <TableCell align="center">
                  {account.active 
                  ? <DoneIcon className={classes.doneIcon}/> 
                  : <ClearIcon className={classes.clearIcon}/> }
                </TableCell>
                <TableCell align="center">{account.roles}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default AccountList;