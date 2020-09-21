import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';

// import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL } from '../config/config';

import { Button, Container } from "@material-ui/core";
import GOOGLE_LOGO from "../images/google-logo.svg";
import FACEBOOK_LOGO from "../images/facebook-logo.svg";


const useStyles = makeStyles(theme => ({
    imageIcon: {
      boxSizing: "content-box",
      height: 40,
      backgroundColor: "white",
      borderRadius: 2,
      marginRight: 12,
      marginLeft: 0,
    },
    buttonRoot: {
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
            <Container>
                <Grid container spacing={2}>
                    <Grid item xs={12} sm={6}>
                        <Button href={GOOGLE_AUTH_URL} className={classes.buttonRoot}  startIcon={<GoogleIcon />} variant="contained">                    
                            <Typography  variant="body2" component="h2">
                                {GOOGLE_TEXT}
                            </Typography>                
                        </Button>
                        </Grid>
                        <Grid item xs={12} sm={6}>
                        <Button href={FACEBOOK_AUTH_URL} className={classes.buttonRoot}  startIcon={<FacebookIcon />} variant="contained">                    
                            <Typography  variant="body2" component="h2">
                                {FACEBOOK_TEXT}
                            </Typography>
                        </Button>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}
export default SocialSignup;