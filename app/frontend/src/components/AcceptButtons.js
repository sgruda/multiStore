import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Collapse from '@material-ui/core/Collapse';
import Grid from '@material-ui/core/Grid';
import SyncIcon from '@material-ui/icons/Sync';


const useStyles = makeStyles(({
      buttonEdit: {
        backgroundColor: "#51c953",
        "&:hover": {
          backgroundColor: "#0bb00d"
        }
      },
      buttonCancel: {
        backgroundColor: "#e35656",
        "&:hover": {
          backgroundColor: "#eb1e1e"
        }
      },
      buttonRefresh: {
        backgroundColor: "#4285F4",
        "&:hover": {
          backgroundColor: "#2c0fab"
        }
      },
}));

function AcceptButtons({submitButtonTitle, handleClose, showRefreshButton}) {
  const classes = useStyles();
  return (
    <div>
        <Grid container xs={12}>
            <Grid item xs={6}>
                <Button
                type="submit"
                variant="contained"
                fullWidth
                color="primary"
                className={classes.buttonEdit}
                >
                {submitButtonTitle}
                </Button>
            </Grid>
            <Grid item xs={6}>
                <Button
                onClick={handleClose}
                variant="contained"
                color="primary"
                fullWidth
                className={classes.buttonCancel}
                >
                Cancel
                </Button>
            </Grid>
            <Grid item xs={12}>
                <Collapse in={showRefreshButtons}>
                    <Button
                    onClick={handleClose}
                    variant="contained"
                    color="primary"
                    fullWidth
                    className={classes.buttonRefresh}
                    startIcon={<SyncIcon size="large" color="primary"/>}
                    >
                    Refresh data
                    </Button>
                </Collapse>
            </Grid>
        </Grid>
    </div >
  );
}
export default AcceptButtons;