import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";

import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import InfoIcon from '@material-ui/icons/Info';
import SearchIcon from '@material-ui/icons/Search';
import Collapse from '@material-ui/core/Collapse';
import { Grid } from "@material-ui/core";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import KeyboardArrowUpIcon from '@material-ui/icons/KeyboardArrowUp';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';

import AccountDetails from '../accounts/AccountDetails';
import { useFields } from '../../hooks/FieldHook';

const useStyles = makeStyles((theme) => ({
    root: {
      paddingLeft: theme.spacing(2),
      paddingRight: theme.spacing(1),
      backgroundColor: "#7cc3eb"
    },
    highlight: {
        backgroundColor: "#7cc3eb",
    },
    title: {
      fontWeight: "fontWeightBold",
    },
    accountText: {
      fontWeight: "fontWeightBold",
      backgroundColor: "#7cc3eb"
    },
    expand: {
      transform: 'rotate(0deg)',
      marginLeft: 'auto',
      transition: theme.transitions.create('transform', {
        duration: theme.transitions.duration.shortest,
      }),
    },
    expandOpen: {
      transform: 'rotate(180deg)',
    },
  }));


function AccountsTableToolbar({selectedAccountMail, selectedAccountName}) {
    const classes = useStyles();
    const aboutAccount = 'Name: ' + selectedAccountName; 
    const [expandedDetails, setExpandedDetails] = useState(false);
    const [expandedSearching, setExpandedSearching] = useState(false);
    // const [textToSearch, setTextToSearch] = useState("");
    const [fields, setFields] = useFields({
      textToSearch: ""
    });
    const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 


    const handleExpandDetails = () => {
      setExpandedDetails(!expandedDetails);
    }
    const handleExpandedSearching = () => {
      setExpandedSearching(!expandedSearching);
    }
    const handleSearch = () => {

    }

    useEffect(() => {
      setExpandedDetails(false);
      setExpandedSearching(false);
    }, [selectedAccountMail]);

    return (
        <Toolbar
          className={clsx(classes.root, {
            [classes.highlight]: selectedAccountMail !== '',
          })}
        >
          <Grid container justify="center">
            <Grid item xs={12}>
              <Typography className={classes.title} variant="h6" id="tableTitle" component="div" align="center">
                Accounts
              </Typography>
            </Grid>
            {selectedAccountMail !== '' ? (
              <Grid item xs={12}>
                <Grid item xs={12}>
                <Collapse in={!expandedDetails} timeout="auto" unmountOnExit>
                  <Typography className={classes.accountText} color="inherit" variant="subtitle1" component="div"  align="center">
                    {aboutAccount}
                  </Typography>
                </Collapse>
                </Grid>
                <Grid item xs={12}>
                <Collapse in={expandedDetails} timeout="auto" unmountOnExit>
                  <AccountDetails
                    selectedAccountMail={selectedAccountMail}
                  />
                </Collapse>
                </Grid>
              </Grid>
            ) : (
              <Grid container justify="center">
                <Collapse in={expandedSearching} timeout="auto" unmountOnExit>
                  <form noValidate onSubmit={handleSubmit(handleSearch)}>
                    <TextField
                      value={ fields.textToSearch }
                      onChange={ setFields }
                      name="textToSearch"
                      required
                      fullWidth
                      id="textToSearch"
                      label="Search"

                      inputRef={register({ required: true,  pattern: /[a-zA-Z0-9!@#$%^*]+/ })}
                      error={errors.textToSearch ? true : false}
                      helperText={errors.textToSearch ? "Incorrect entry." : ""}
                    />
                  <Tooltip title="Search">
                    <IconButton aria-label="Search" type="submit">
                        <SearchIcon />
                    </IconButton>
                    {/* <Button
                  type="submit"
                >
                  Search
                </Button> */}
                  </Tooltip>
                  </form>
                </Collapse>
              </Grid>
            )}
              {selectedAccountMail !== '' ? (
              <Grid  alignItems="center">
                <Tooltip title="Details">
                <IconButton aria-label="Details"
                  className={clsx(classes.expand, {
                    [classes.expandOpen]: expandedDetails,
                  })}
                  onClick={handleExpandDetails}
                  aria-expanded={expandedDetails}
                  >
                  <ExpandMoreIcon/>
                </IconButton>
                </Tooltip>
              </Grid>
               ) : (
              <Grid  alignItems="center">
                <Tooltip title="Search">
                <IconButton aria-label="Expand Search" onClick={handleExpandedSearching}>
                    {!expandedSearching ? <SearchIcon /> : <KeyboardArrowUpIcon/>}
                </IconButton>
                </Tooltip>
              </Grid>
              )}
            </Grid>
        </Toolbar>
    );
}
export default AccountsTableToolbar;