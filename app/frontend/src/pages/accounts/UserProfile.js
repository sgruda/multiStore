import React, { useState, useEffect } from "react";
import { useTranslation } from 'react-i18next';

import AccountService from '../../services/AccountService';
import AuthenticationService from '../../services/AuthenticationService';
import { ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN } from "../../config/config";
import AccountEdit from '../../components/accounts/AccountEdit';
import PasswordChange from '../../components/accounts/PasswordChange';
import RouterRedirectTo from '../../components/simple/RouterRedirectTo';

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
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';

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
    editButton: {
        backgroundColor: "#4285F4",
        "&:hover": {
          backgroundColor: "#2c0fab"
        }
      },
  }));

function UserProfile() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [loadingData, setLoadingData] = useState(true);
    const [account, setAccount] = useState(Object);
    const [roleClientActive, setRoleClientActive] = useState(false);
    const [roleEmployeeActive, setRoleEmployeeActive] = useState(false);
    const [roleAdminActive, setRoleAdminActive] = useState(false);

    const [openEdit, setOpenEdit] = useState(false);
    const [openChangePassword, setOpenChangePassword] = useState(false);

    const [jwtExpiration, setJwtExpiration] = useState(false);

    const handleOpenEdit = () => {
        setOpenEdit(true);
    }
    const handleOpenChangePassword = () => {
        setOpenChangePassword(true);
    }
    const handleCloseEdit = () => {
        setOpenEdit(false);
        setLoadingData(true);
    }
    const handleChangePassword = () => {
        setOpenChangePassword(false);
        setLoadingData(true);
    }
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
            if(AuthenticationService.jwtIsExpired()) {
                setJwtExpiration(true);
              } else {
                getAccount();
              }
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
            {t('pages.titles.profile')}
            </Typography>
            <Grid container xs={12}>
                <Grid xs={12}>
                    <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                        <Grid item xs={12}><Paper className={classes.paperOne} elevation={3}>{t('account.profile.firstName')}: {account.firstName}</Paper></Grid>
                        <Grid item xs={12}><Paper className={classes.paperTwo} elevation={3}>{t('account.profile.lastName')}: {account.lastName}</Paper></Grid>
                        <Grid item xs={12}><Paper className={classes.paperOne} elevation={3}>{t('account.profile.email')}: {account.email}</Paper></Grid>
                        <Grid xs={12}>
                            <Paper className={classes.paperTwo} elevation={3}>
                            {t('account.profile.activity')}: 
                            {account.active 
                                ? <DoneIcon className={classes.doneIcon}/> 
                                : <ClearIcon className={classes.clearIcon}/> }
                            </Paper>
                        </Grid>
                        <Grid item xs={12}><Paper className={classes.paperOne} elevation={3}>{t('account.profile.account-type')}: {account.authProvider}</Paper></Grid>
                        <Grid item xs={12}>
                            {account.authProvider === "system"
                            ?  
                            <Grid container xs={12}>
                                <Grid item xs={12}><Paper className={classes.paperTwo} elevation={3}>{t('account.profile.username')}: {account.authenticationDataDTO.username}</Paper></Grid>
                                <Grid item xs={12}>
                                    <Paper className={classes.paperOne} elevation={3}>
                                    {t('account.profile.email-verified')}: {account.authenticationDataDTO.emailVerified 
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
                            {t('account.profile.roles')}:
                            {t('account.access-level.names.client')}:  { roleClientActive
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> 
                                            }
                            {t('account.access-level.names.employee')}:  { roleEmployeeActive
                                                ? <DoneIcon className={classes.doneIcon}/> 
                                                : <ClearIcon className={classes.clearIcon}/> 
                                                }
                           {t('account.access-level.names.admin')}:  { roleAdminActive
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> 
                                            }
                            </Paper>
                        </Grid>
                        {account.authProvider === 'system' ? 
                        <Grid container xs={12}>
                            <Grid item xs={6}>
                                <Button 
                                    onClick={handleOpenEdit} 
                                    fullWidth 
                                    className={classes.editButton}
                                >{t("button.edit")}</Button>
                            </Grid>
                            <Grid item xs={6}>
                                <Button 
                                    onClick={handleOpenChangePassword} 
                                    fullWidth 
                                    className={classes.editButton}
                                >{t("button.change-password")}</Button>
                            </Grid>
                        </Grid>
                        :<></>}
                        <Dialog
                            open={openEdit}
                            onClose={handleCloseEdit}
                            aria-describedby="dialog-edit"
                        >
                            <DialogContent>
                                <AccountEdit
                                    account={account}
                                    handleClose={handleCloseEdit}
                                    apiMethod={AccountService.editUserAccount}
                                />     
                            </DialogContent>
                        </Dialog>
                        <Dialog
                            open={openChangePassword}
                            onClose={handleChangePassword}
                            aria-describedby="dialog-change-password"
                        >
                            <DialogContent>
                                <PasswordChange
                                    account={account}
                                    handleClose={handleChangePassword}
                                    apiMethod={AccountService.changeOwnPassword}
                                    adminView={false}
                                />     
                            </DialogContent>
                        </Dialog>
                    </Typography>
                </Grid>
            </Grid>
        </div>
        {jwtExpiration ? 
            <RouterRedirectTo 
                dialogContent={t('dialog.content.jwt-expired')}
                page="/signin"
            />
        :<></>}
        </Container>
    );
}

export default UserProfile;