import React, {useState, useEffect} from "react";
import AccountEdit from '../../components/accounts/AccountEdit';
import PasswordChange from './PasswordChange';
import AccessLevelsEditor from './AccessLevelsEditor';
import ActivitySwitch from './ActivitySwitch';
import SendConfirmEmailButton from './SendConfirmEmailButton';
import RemoveUnconfirmedAccountButton from './RemoveUnconfirmedAccountButton';
import AccountService from '../../services/AccountService';
import { ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN } from "../../config/config";

import CssBaseline from '@material-ui/core/CssBaseline';
import Grid from '@material-ui/core/Grid';
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
    doneIcon: {
        color: "#0bb00d",
        height: 18,
    },
    clearIcon: {
        color: "#eb1e1e",
        height: 18,
    },
    text: {
        backgroundColor: "#7cc3eb"
    },
    paper: {
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
    removeButton: {
        backgroundColor: "#e35656",
        "&:hover": {
        backgroundColor: "#eb1e1e"
        }
    },
}));


function AccountDetails({selectedAccountMail, handleHardRefresh}) {
    const classes = useStyles();
    const [account, setAccount] = useState(Object);
    const [loadingData, setLoadingData] = useState(true);
    const [clientRole, setClientRole] = useState(false);
    const [employeeRole, setEmployeeRole] = useState(false);
    const [adminRole, setAdminRole] = useState(false);

    const [openEdit, setOpenEdit] = useState(false);
    const [openChangePassword, setOpenChangePassword] = useState(false);
    const [openAddAccessLevel, setOpenAddAccessLevel] = useState(false);
    const [openRemoveAccessLevel, setOpenRemoveAccessLevel] = useState(false);


    const handleOpenEdit = () => {
        setOpenEdit(true);
    }
    const handleOpenChangePassword = () => {
        setOpenChangePassword(true);
    }
    const handleOpenAddAccessLevel = () => {
        setOpenAddAccessLevel(true);
    }
    const handleOpenRemoveAccessLevel = () => {
        setOpenRemoveAccessLevel(true);
    }
    const handleCloseAddAccessLevel = () => {
        setOpenAddAccessLevel(false);
        setLoadingData(true);
    }
    const handleCloseRemoveAccessLevel = () => {
        setOpenRemoveAccessLevel(false);
        setLoadingData(true);
    }
    const handleCloseEdit = () => {
        setOpenEdit(false);
        setLoadingData(true);
    }
    const handleChangePassword = () => {
        setOpenChangePassword(false);
        setLoadingData(true);
    }

    const treeButtons = (yes) => {
        if(yes)
            return 4;
        else
            return 6;
    }

    async function getAccount() {
        await AccountService.getSingleAccount(selectedAccountMail)
        .then(response => {
            if (response.status === 200) { 
                setAccount(response.data);               
                response.data.roles.includes(ROLE_CLIENT) ? setClientRole(true) : setClientRole(false);
                response.data.roles.includes(ROLE_EMPLOYEE) ? setEmployeeRole(true) : setEmployeeRole(false);
                response.data.roles.includes(ROLE_ADMIN) ? setAdminRole(true) : setAdminRole(false);
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
                console.error("SelectedAccountDetails: " + resMessage);
                if(resMessage === "error.account.not.exists") {
                    handleHardRefresh();
                }
            }
        );
    }

    useEffect(() => {
        if (loadingData) {
            setLoadingData(false);
            getAccount();
        }
    }, [selectedAccountMail, loadingData]);

    return (
        <Container component="main" maxWidth="sm">
        <CssBaseline />
        <div className={classes.paper}>
            <Grid container xs={12}>
                <Grid xs={12}>
                    <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                        <Grid item xs={12}><Paper className={classes.paperOne} elevation={3}>First name: {account.firstName}</Paper></Grid>
                        <Grid item xs={12}><Paper className={classes.paperTwo} elevation={3}>Last name: {account.lastName}</Paper></Grid>
                        <Grid item xs={12}><Paper className={classes.paperOne} elevation={3}>E-mail: {account.email}</Paper></Grid>
                        <Grid item xs={12}>
                            <ActivitySwitch
                                classes={classes}
                                account={account}
                                setLoadingData={setLoadingData}
                            />
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
                                                        : <div> 
                                                            <ClearIcon className={classes.clearIcon}/> 
                                                            <SendConfirmEmailButton
                                                                account={account}                                                            />
                                                          </div>
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
                            {/* {account.roles} */}
                            {ROLE_CLIENT}:  { clientRole
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> 
                                            }
                            {ROLE_EMPLOYEE}:  { employeeRole
                                                ? <DoneIcon className={classes.doneIcon}/> 
                                                : <ClearIcon className={classes.clearIcon}/> 
                                                }
                            {ROLE_ADMIN}:  { adminRole
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> 
                                            }
                            </Paper>
                        </Grid>
                        <Grid container xs={12} spacing={1} justify="center">
                        {account.authProvider === 'system' ? 
                        <Grid container xs={12} spacing={1} justify="center">
                            <Grid item xs={treeButtons(!account.authenticationDataDTO.emailVerified)}>
                                <Button 
                                    onClick={handleOpenEdit} 
                                    fullWidth 
                                    className={classes.editButton}
                                >Edit</Button>
                            </Grid>
                            <Grid item xs={treeButtons(!account.authenticationDataDTO.emailVerified)}>
                                <Button 
                                    onClick={handleOpenChangePassword} 
                                    fullWidth 
                                    className={classes.editButton}
                                >Change password</Button>
                            </Grid>
                            <Grid item xs={4}>
                            {!account.authenticationDataDTO.emailVerified ?
                                    <RemoveUnconfirmedAccountButton
                                        account={account}
                                        buttonStyle={classes.removeButton}
                                        afterDeleteAccount={handleHardRefresh}
                                        handleRefresh={ () => setLoadingData(true)}
                                    /> 
                            :<></>}
                            </Grid>
                        </Grid>
                        :<></>}
                            <Grid item xs={6}>
                                <Button 
                                    onClick={handleOpenAddAccessLevel} 
                                    fullWidth 
                                    className={classes.editButton}
                                >Add acccess level</Button>
                            </Grid>
                            <Grid item xs={6}>
                                <Button 
                                    onClick={handleOpenRemoveAccessLevel} 
                                    fullWidth 
                                    className={classes.editButton}
                                >Remove acccess level</Button>
                            </Grid>
                        </Grid>
                        <Dialog
                            open={openEdit}
                            onClose={handleCloseEdit}
                            aria-describedby="dialog-edit"
                        >
                            <DialogContent>
                                <AccountEdit
                                    account={account}
                                    handleClose={handleCloseEdit}
                                    apiMethod={AccountService.editAccount}
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
                                    apiMethod={AccountService.changePassword}
                                    adminView={true}
                                />     
                            </DialogContent>
                        </Dialog>
                        <Dialog
                            open={openAddAccessLevel}
                            onClose={handleCloseAddAccessLevel}
                            aria-describedby="dialog-add"
                        >
                            <DialogContent>
                                <AccessLevelsEditor
                                    account={account}
                                    handleClose={handleCloseAddAccessLevel}
                                    apiMethod={AccountService.addAccessLevel}
                                    operationTitle="Add"
                                    clientRole={clientRole}
                                    employeeRole={employeeRole}
                                    adminRole={adminRole}
                                    setClientRole={setClientRole}
                                    setEmployeeRole={setEmployeeRole}
                                    setAdminRole={setAdminRole}
                                />     
                            </DialogContent>
                        </Dialog>
                        <Dialog
                            open={openRemoveAccessLevel}
                            onClose={handleCloseRemoveAccessLevel}
                            aria-describedby="dialog-remove"
                        >
                            <DialogContent>
                                <AccessLevelsEditor
                                    account={account}
                                    handleClose={handleCloseRemoveAccessLevel}
                                    apiMethod={AccountService.removeAccessLevel}
                                    operationTitle="Remove"
                                    clientRole={clientRole}
                                    employeeRole={employeeRole}
                                    adminRole={adminRole}
                                    setClientRole={setClientRole}
                                    setEmployeeRole={setEmployeeRole}
                                    setAdminRole={setAdminRole}
                                />     
                            </DialogContent>
                        </Dialog>
                    </Typography>
                </Grid>
            </Grid>
        </div>
        </Container>
    );
}
export default AccountDetails;