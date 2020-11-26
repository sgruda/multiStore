
import React from "react";
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';

function AccountsTableBody({accounts, handleClickAccount, isSelected, classes, emptyRows, dense}) {
    return (
        <TableBody>
            {accounts.map((account) => {
              const isItemSelected = isSelected(account.email);
              return (
                <TableRow
                  hover
                  onClick={() => handleClickAccount(account.email, account.firstName + ' ' + account.lastName)}
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