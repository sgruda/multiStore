import React, { useState, useEffect } from "react";

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


import AccountDetails from '../accounts/AccountDetails';

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



    const handleExpandDetails = () => {
      setExpandedDetails(!expandedDetails);
    }


    useEffect(() => {
      setExpandedDetails(false);
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
            ) : (<div/>) }
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
              <Tooltip title="Search">
              <IconButton aria-label="Search">
                  <SearchIcon />
              </IconButton>
              </Tooltip>
              )}
            </Grid>
        </Toolbar>
    );
}
export default AccountsTableToolbar;