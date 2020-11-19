import React, { useState, useEffect } from "react";

import AccountService from '../services/AccountService';
import { ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN } from "../config/config";


import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import Grid from '@material-ui/core/Grid';
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import { Button } from "@material-ui/core";
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';
import Paper from '@material-ui/core/Paper';

const useStyles = makeStyles((theme) => ({
    paper: {
      marginTop: theme.spacing(8),
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
    },
    avatar: {
      margin: theme.spacing(2),
      width: theme.spacing(7),
      height: theme.spacing(7),
      backgroundColor: theme.palette.primary.main,
    },
    paperOne: {
        backgroundColor: "#b7e1f7",
        margin: `${theme.spacing(1)}px auto`,
        padding: theme.spacing(1),
    },
    paperTwo: {
        backgroundColor: "#d3ebf8",
        margin: `${theme.spacing(1)}px auto`,
        padding: theme.spacing(1),
    },
    doneIcon: {
        color: "#0bb00d",
        height: 18,
    },
    clearIcon: {
        color: "#eb1e1e",
        height: 18,
    },
  }));

function UserProfile() {
    const classes = useStyles();
    const [loadingData, setLoadingData] = useState(true);
    const [account, setAccount] = useState(Object);
    const [roleClientActive, setRoleClientActive] = useState(false);
    const [roleEmployeeActive, setRoleEmployeeActive] = useState(false);
    const [roleAdminActive, setRoleAdminActive] = useState(false);

    async function getAccount() {
        await AccountService.getUserAccount()
        .then(response => {
            if (response.status === 200) { 
                setAccount(response.data);
                if(response.data.roles.includes(ROLE_CLIENT))
                    setRoleClientActive(true);
                if(response.data.roles.includes(ROLE_EMPLOYEE))
                    setRoleEmployeeActive(true);
                if(response.data.roles.includes(ROLE_ADMIN))
                    setRoleAdminActive(true);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("UserProfile: " + resMessage);
            }
        );
    }

    useEffect(() => {
        if (loadingData) {
            setLoadingData(false);
            getAccount();
        }
    }, [loadingData]);

    return (
    <Container component="main" maxWidth="sm">
        <CssBaseline />
        <div className={classes.paper}>
            <Avatar className={classes.avatar}>
                <AccountBoxIcon fontSize="large"/>
            </Avatar>
            <Typography component="h1" variant="h5">
            Your Profile
            </Typography>
            <Grid container xs={12}>
                <Grid xs={12}>
                    <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                        <Grid item xs={12}><Paper className={classes.paperOne} elevation={3}>First name: {account.firstName}</Paper></Grid>
                        <Grid item xs={12}><Paper className={classes.paperTwo} elevation={3}>Last name: {account.lastName}</Paper></Grid>
                        <Grid item xs={12}><Paper className={classes.paperOne} elevation={3}>E-mail: {account.email}</Paper></Grid>
                        <Grid xs={12}>
                            <Paper className={classes.paperTwo} elevation={3}>
                            Active: 
                            {account.active 
                                ? <DoneIcon className={classes.doneIcon}/> 
                                : <ClearIcon className={classes.clearIcon}/> }
                            </Paper>
                        </Grid>
                        <Grid item xs={12}><Paper className={classes.paperOne} elevation={3}>Account type: {account.authProvider}</Paper></Grid>
                        <Grid item xs={12}>
                            {account.authProvider === "system"
                            ?  
                            <Grid container xs={12}>
                                <Grid item xs={12}><Paper className={classes.paperTwo} elevation={3}>Username: {account.authenticationDataDTO.username}</Paper></Grid>
                                <Grid item xs={12}>
                                    <Paper className={classes.paperOne} elevation={3}>
                                        E-mail verified: {account.authenticationDataDTO.emailVerified 
                                                        ? <DoneIcon className={classes.doneIcon}/> 
                                                        : <ClearIcon className={classes.clearIcon}/> 
                                                        }
                                    </Paper>
                                </Grid> 
                            </Grid>
                            :
                            <></>         
                            }
                        </Grid>
                        <Grid item xs={12}>
                            <Paper className={classes.paperTwo} elevation={3}>
                            Roles:
                            {ROLE_CLIENT}:  { roleClientActive
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> 
                                            }
                            {ROLE_EMPLOYEE}:  { roleEmployeeActive
                                                ? <DoneIcon className={classes.doneIcon}/> 
                                                : <ClearIcon className={classes.clearIcon}/> 
                                                }
                            {ROLE_ADMIN}:  { roleAdminActive
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> 
                                            }
                            </Paper>
                        </Grid>
                    </Typography>
                </Grid>
            </Grid>
        </div>
        </Container>
    );
}

export default UserProfile;