import React, { useState } from "react";

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
      // flex: '1 1 100%',
      fontWeight: "fontWeightBold",
    },
    accountText: {
      fontWeight: "fontWeightBold",
      backgroundColor: "#26a315"
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
                  <Typography paragraph  align="center">
                    Heat oil in a (14- to 16-inch) paella pan or a large, deep skillet over medium-high
                    heat. Add chicken, shrimp and chorizo, and cook, stirring occasionally until lightly
                    browned, 6 to 8 minutes. Transfer shrimp to a large plate and set aside, leaving chicken
                    and chorizo in the pan. Add pimentón, bay leaves, garlic, tomatoes, onion, salt and
                    pepper, and cook, stirring often until thickened and fragrant, about 10 minutes. Add
                    saffron broth and remaining 4 1/2 cups chicken broth; bring to a boil.
                  </Typography>
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