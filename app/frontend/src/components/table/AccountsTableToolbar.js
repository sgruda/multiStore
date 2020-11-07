import React from "react";

import clsx from 'clsx';
import { lighten, makeStyles } from '@material-ui/core/styles';

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import Checkbox from '@material-ui/core/Checkbox';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import DeleteIcon from '@material-ui/icons/Delete';
import FilterListIcon from '@material-ui/icons/FilterList';


const useStyles = makeStyles((theme) => ({
    root: {
      paddingLeft: theme.spacing(2),
      paddingRight: theme.spacing(1),
      backgroundColor: "#4285F4"
    },
    highlight: {
        backgroundColor: "red",
    },
    title: {
      flex: '1 1 100%',
      fontWeight: "fontWeightBold",
    },
  }));


function AccountsTableToolbar({selectedAccountId}) {
    const classes = useStyles();
    return (
        <Toolbar
          className={clsx(classes.root, {
            [classes.highlight]: selectedAccountId !== '',
          })}
        >
            <Typography className={classes.title} variant="h6" id="tableTitle" component="div" >
            Accounts
            {selectedAccountId !== '' ? (
                <Typography className={classes.title} color="inherit" variant="subtitle1" component="div">
                {selectedAccountId} selected
                </Typography>
            ) : (<div/>) }
            </Typography>

        {selectedAccountId !== '' ? (
            <Tooltip title="Delete">
            <IconButton aria-label="delete">
                <DeleteIcon />
            </IconButton>
            </Tooltip>
        ) : (
            <Tooltip title="Filter list">
            <IconButton aria-label="filter list">
                <FilterListIcon />
            </IconButton>
            </Tooltip>
        )}
        </Toolbar>
    );
}
export default AccountsTableToolbar;