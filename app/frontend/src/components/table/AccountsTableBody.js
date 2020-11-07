
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

function AccountsTableBody({accounts, handleClickAccount, isSelected, classes, emptyRows, dense}) {
    return (
        <TableBody>
            {accounts.map((account) => {
              const isItemSelected = isSelected(account.id);
              return (
                <TableRow key={account.id}
                  hover
                  onClick={(event) => handleClickAccount(event, account.id)}
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
    );
}
export default AccountsTableBody;