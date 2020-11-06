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

    const [firstLoading, setFirstLoading] = useState(true);
    const [loadingData, setLoadingData] = useState(true);
    const [accounts, setAccounts] = useState([]);
    // const paginationInfo = [{
    //     totalPages: 0
    // }];
    const [order, setOrder] = useState('asc');
    const [orderBy, setOrderBy] = useState('lastName');
    const [selected, setSelected] = useState([]);
    const [page, setPage] = useState(0);
    const [totalItems, setTotalPages] = useState(0);
    const [dense, setDense] = useState(false);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [textToSearch, setTextToSearch] = useState(null);
    const [filterActiveAccounts, setFilterActiveAccounts] = useState(null);

    const handleRequestSort = (event, property) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
    };
    
    const handleSelectAllClick = (event) => {
        if (event.target.checked) {
            const newSelecteds = accounts.map((n) => n.name);
            setSelected(newSelecteds);
            return;
        }
        setSelected([]);
    };

    const handleClick = (event, name) => {
        const selectedIndex = selected.indexOf(name);
        let newSelected = [];
    
        if (selectedIndex === -1) {
          newSelected = newSelected.concat(selected, name);
        } else if (selectedIndex === 0) {
          newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
          newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
          newSelected = newSelected.concat(
            selected.slice(0, selectedIndex),
            selected.slice(selectedIndex + 1),
          );
        }
    
        setSelected(newSelected);
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

    const isSelected = (name) => selected.indexOf(name) !== -1;


    // const calculateEmptyRows = () => rowsPerPage - Math.min(rowsPerPage, totalItems - page * rowsPerPage);
    const emptyRows = () => rowsPerPage - Math.min(rowsPerPage, accounts.length - page * rowsPerPage);



    
    async function getAccounts() {
        // setLoadingData(true);
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
                // paginationInfo.totalPages = response.data.totalPages;
\            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
            }
        );
    }

    useEffect(() => {
\        if (loadingData) {
\            setLoadingData(false);
            getAccounts();
        }
    }, [page, accounts]);

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
          {/* <EnhancedTableHead
            classes={classes}
            numSelected={selected.length}
            order={order}
            orderBy={orderBy}
            onSelectAllClick={handleSelectAllClick}
            onRequestSort={handleRequestSort}
            rowCount={rows.length}
          /> */}
          <TableBody>
            {/* {stableSort(rows, getComparator(order, orderBy))
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((row, index) => {
                const isItemSelected = isSelected(row.name);
                const labelId = `enhanced-table-checkbox-${index}`; */}

                {/* return (
                  <TableRow
                    hover
                    onClick={(event) => handleClick(event, row.name)}
                    role="checkbox"
                    aria-checked={isItemSelected}
                    tabIndex={-1}
                    key={row.name}
                    selected={isItemSelected}
                  >
                    <TableCell padding="checkbox">
                      <Checkbox
                        checked={isItemSelected}
                        inputProps={{ 'aria-labelledby': labelId }}
                      />
                    </TableCell>
                    <TableCell component="th" id={labelId} scope="row" padding="none">
                      {row.name}
                    </TableCell>
                    <TableCell align="right">{row.calories}</TableCell>
                    <TableCell align="right">{row.fat}</TableCell>
                    <TableCell align="right">{row.carbs}</TableCell>
                    <TableCell align="right">{row.protein}</TableCell>
                  </TableRow>
                );
              })} */}
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
//     <TableContainer component={Paper}>
//       <Table className={classes.table} aria-label="simple table">
//         <TableHead>
//           <TableRow>
//             <TableCell align="center">First Name</TableCell>
//             <TableCell align="center">Last Name</TableCell>
//             <TableCell align="center">Email</TableCell>
//             <TableCell align="center">Active</TableCell>
//             <TableCell align="center">Roles</TableCell>
//           </TableRow>
//         </TableHead>
        // <TableBody>
        //   {accounts.map((account) => (
        //     <TableRow key={account.idHash}>
        //         <TableCell component="th" scope="row">
        //             {account.firstName}
        //         </TableCell>
        //         <TableCell align="center">{account.lastName}</TableCell>
        //         <TableCell align="center">{account.email}</TableCell>
        //         <TableCell align="center">
        //           {account.active 
        //           ? <DoneIcon className={classes.doneIcon}/> 
        //           : <ClearIcon className={classes.clearIcon}/> }
        //         </TableCell>
        //         <TableCell align="center">{account.roles}</TableCell>
        //     </TableRow>
        //   ))}
//         </TableBody>
//       </Table>
//     </TableContainer>
//   );
}

export default AccountList;