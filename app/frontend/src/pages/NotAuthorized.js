import React from "react"; 
import { useTranslation } from 'react-i18next';
import LockIcon from '@material-ui/icons/Lock';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import { Typography } from "@material-ui/core";
import Grid from '@material-ui/core/Grid';

const useStyles = makeStyles((theme) => ({
  root: {
    marginTop: 120,
    margin: theme.spacing(2),
    position: 'center',
  },
  icon: {
    fontSize: 200,
    color: '#002563',
  },
  title: {
    fontWeight: "fontWeightBold",
    fontWeightBold: "fontWeightBold",
    color: "#002563"
  },
}));

function NotAuthorized() { 
  const classes = useStyles();
  const { t } = useTranslation();

  return ( 
    <Grid container className={classes.root} alignItems="center" justify="center" xs={12}> 
      <Grid item xs={12} style={{textAlign: "center"}}><LockIcon className={classes.icon}/></Grid> 
      <Grid item xs={12} style={{textAlign: "center"}}><Typography className={classes.title}>{t('error.http.403')}</Typography></Grid> 
    </Grid> 
  ); 
} 
export default NotAuthorized;