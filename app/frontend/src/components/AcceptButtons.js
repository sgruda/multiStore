import React from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Collapse from '@material-ui/core/Collapse';
import Grid from '@material-ui/core/Grid';
import SyncIcon from '@material-ui/icons/Sync';


const useStyles = makeStyles(({
      buttonSubmit: {
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

function AcceptButtons({submitButtonTitle, handleClose, handleRefresh, showRefreshButton, setShowRefreshButton}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const handleRefreshButtons = () => {
    handleRefresh != null ? handleRefresh() : handleClose();
    if(setShowRefreshButton != null)
     setShowRefreshButton(false);
  }

  return (
    <div>
        <Grid container xs={12}>
            <Grid item xs={6}>
                <Button
                type="submit"
                variant="contained"
                fullWidth
                color="primary"
                className={classes.buttonSubmit}
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
                {t('button.cancel')}
                </Button>
            </Grid>
            <Grid item xs={12}>
                <Collapse in={showRefreshButton}>
                    <Button
                    onClick={handleRefreshButtons}
                    variant="contained"
                    color="primary"
                    fullWidth
                    className={classes.buttonRefresh}
                    startIcon={<SyncIcon size="large" color="primary"/>}
                    >
                    {t('button.refresh')}
                    </Button>
                </Collapse>
            </Grid>
        </Grid>
    </div >
  );
}
export default AcceptButtons;