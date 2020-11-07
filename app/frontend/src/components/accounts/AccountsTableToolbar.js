import React from "react";

import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import DeleteIcon from '@material-ui/icons/Delete';
import FilterListIcon from '@material-ui/icons/FilterList';
import Paper from '@material-ui/core/Paper';


import SelectedAccountDetails from '../accounts/SelectedAccountDetails';

const useStyles = makeStyles((theme) => ({
    root: {
      paddingLeft: theme.spacing(2),
      paddingRight: theme.spacing(1),
      backgroundColor: "#4285F4"
    },
    highlight: {
        backgroundColor: "#26a315",
    },
    title: {
      flex: '1 1 100%',
      fontWeight: "fontWeightBold",
    },
  }));


function AccountsTableToolbar({selectedAccountMail}) {
    const classes = useStyles();
    return (
        <Toolbar
          className={clsx(classes.root, {
            [classes.highlight]: selectedAccountMail !== '',
          })}
        >
            <Typography className={classes.title} variant="h6" id="tableTitle" component="div" >
            Accounts
            {selectedAccountMail !== '' ? (
                <Paper>
                    <Typography className={classes.title} color="inherit" variant="subtitle1" component="div">
                    Selected: {selectedAccountMail}
                  </Typography>
                </Paper>
            ) : (<div/>) }
            </Typography>

        {selectedAccountMail !== '' ? (
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