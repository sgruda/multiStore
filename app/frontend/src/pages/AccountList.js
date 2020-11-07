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



import AccountService from '../services/AccountService';




const useStyles = makeStyles({
    table: {
      minWidth: 650,
    },
    tableRow: {
      "&.Mui-selected, &.Mui-selected:hover": {
        backgroundColor: "#b8ddf2",
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

    const handleRequestSort = (event, property) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
    };
    
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
    }, [page, accounts]);






    function descendingComparator(a, b, orderBy) {
      if (b[orderBy] < a[orderBy]) {
        return -1;
      }
      if (b[orderBy] > a[orderBy]) {
        return 1;
      }
      return 0;
    }
    
    function getComparator(order, orderBy) {
      return order === 'desc'
        ? (a, b) => descendingComparator(a, b, orderBy)
        : (a, b) => -descendingComparator(a, b, orderBy);
    }
    
    function stableSort(array, comparator) {
      const stabilizedThis = array.map((el, index) => [el, index]);
      stabilizedThis.sort((a, b) => {
        const order = comparator(a[0], b[0]);
        if (order !== 0) return order;
        return a[1] - b[1];
      });
      return stabilizedThis.map((el) => el[0]);
    }










const handleSelectAllClick = () => {
  console.log("SIEEEEEEEEEEMAhandleSelectAllClick ")
}

    const headerCells = [
      { id: 'firstName', numeric: false, disablePadding: true, label: 'First Name' },
      { id: 'lastName', numeric: false, disablePadding: false, label: 'Last Name' },
      { id: 'email', numeric: false, disablePadding: false, label: 'E-Mail' },
      { id: 'active', numeric: false, disablePadding: false, label: 'Active' },
      { id: 'userRoles', numeric: false, disablePadding: false, label: 'User Roles' },
    ];

    function EnhancedTableHead(props) {
      const { classes, onSelectAllClick, order, orderBy, numSelected, rowCount, onRequestSort } = props;
      const createSortHandler = (property) => (event) => {
      onRequestSort(event, property);
    };
  
    return (
      <TableHead>
        <TableRow>
          {headerCells.map((headCell) => (
            <TableCell
              key={headCell.id}
              align="center"
              padding={headCell.disablePadding ? 'none' : 'default'}
              // sortDirection={orderBy === headCell.id ? order : false}
            >
              {/* <TableSortLabel
                active={orderBy === headerCells.id}
                direction={orderBy === headerCells.id ? order : 'asc'}
                onClick={createSortHandler(headerCells.id)}
              > */}
                {headCell.label}
                {/* {orderBy === headerCells.id ? (
                  <span className={classes.visuallyHidden}>
                    {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                  </span>
                ) : null}
              </TableSortLabel> */}
            </TableCell>
          ))}
        </TableRow>
      </TableHead>
    );
  }
  
  EnhancedTableHead.propTypes = {
    classes: PropTypes.object.isRequired,
    // numSelected: PropTypes.number.isRequired,
    // onRequestSort: PropTypes.func.isRequired,
    // onSelectAllClick: PropTypes.func.isRequired,
    // order: PropTypes.oneOf(['asc', 'desc']).isRequired,
    // orderBy: PropTypes.string.isRequired,
    // rowCount: PropTypes.number.isRequired,
  };











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
          <EnhancedTableHead
            classes={classes}
            // numSelected={5}
            // order={order}
            // orderBy={orderBy}
            // onSelectAllClick={handleSelectAllClick}
            // onRequestSort={handleRequestSort}
            // rowCount={totalItems}
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
                    <TableCell component="th" scope="row" className={classes.tableCell}>
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