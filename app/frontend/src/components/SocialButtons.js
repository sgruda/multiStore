import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';

import { Button } from "@material-ui/core";
import GOOGLE_LOGO from "../images/google-logo.svg";
import FACEBOOK_LOGO from "../images/facebook-logo.svg";


const useStyles = makeStyles(theme => ({
    imageIcon: {
      boxSizing: "content-box",
      height: 40,
      backgroundColor: "white",
      borderRadius: 2,
    //   marginLeft: '-200%',
    //   alignContent: "flex-start",
    },
    buttonRoot: {
      margin: theme.spacing(1, 0, 1),
      padding: 2,
      paddingRight: 10,
      paddingLeft: 6,
      backgroundColor: "#4285F4",
      color: "white",
      "&:hover": {
        backgroundColor: "#2c0fab"
      }
    }
  }));

  
function SocialSignup({GOOGLE_AUTH_URL, GOOGLE_TEXT, FACEBOOK_AUTH_URL, FACEBOOK_TEXT}) {
    const classes = useStyles();

    const GoogleIcon = () => {
        const classes = useStyles();
        return <img className={classes.imageIcon} src={GOOGLE_LOGO} />;
    };
    const FacebookIcon = () => {
        const classes = useStyles();
        return <img className={classes.imageIcon} src={FACEBOOK_LOGO} />;
    };  
    
    return (
        <div>
            <Grid container spacing={1} xs={12} alignItems="flex-end">
                    <Button onClick={ () => window.location.href = GOOGLE_AUTH_URL } className={classes.buttonRoot}  variant="contained" fullWidth>                    
                        <GoogleIcon/>
                        <Grid item xs={12} justify="center">
                            <Typography  variant="body2" component="h2">
                                {GOOGLE_TEXT}
                            </Typography>    
                        </Grid>            
                    </Button>
                    <Button onClick={ () => window.location.href = FACEBOOK_AUTH_URL } className={classes.buttonRoot}  variant="contained" fullWidth>                    
                        <FacebookIcon/>
                        <Grid item xs={12} justify="center">
                            <Typography  variant="body2" component="h2">
                                {FACEBOOK_TEXT}
                            </Typography>
                        </Grid>  
                    </Button>
                </Grid>
        </div>
    );
}
export default SocialSignup;