import React, {useState, useEffect} from "react";
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';
import Typography from '@material-ui/core/Typography';



import AccountService from '../../services/AccountService';
import { ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN } from "../../config/config";

const useStyles = makeStyles({
    doneIcon: {
        color: "#0bb00d",
    },
    clearIcon: {
        color: "#eb1e1e"
    },
    text: {
        backgroundColor: "#7cc3eb"
    },
});


function AccountDetails({selectedAccountMail}) {
    const classes = useStyles();
    const [account, setAccount] = useState(Object);
    const [loadingData, setLoadingData] = useState(true);
    const [roleClientActive, setRoleClientActive] = useState(false);
    const [roleEmployeeActive, setRoleEmployeeActive] = useState(false);
    const [roleAdminActive, setRoleAdminActive] = useState(false);

    async function getAccount() {
        await AccountService.getSingleAccount(selectedAccountMail)
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
                console.error("SelectedAccountDetails: " + resMessage);
            }
        );
    }

    useEffect(() => {
        setLoadingData(true);
        if (loadingData) {
            setLoadingData(false);
            getAccount();
        }
    }, [selectedAccountMail]);

    return (
        <Grid container  justify="center" alignItems="stretch">
            <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center"></Typography>
            <Grid item xs={6}>
                <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                    First Name: {account.firstName}
                </Typography>
            </Grid>
            <Grid item xs={6}>
                <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                    Last Name: {account.lastName}
                </Typography>
            </Grid>
            <Grid item xs={6}>
                <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                    E-mail: {account.email}
                </Typography>
            </Grid>
            <Grid item xs={6}>
                <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                    Active: 
                    {account.active 
                        ? <DoneIcon className={classes.doneIcon}/> 
                        : <ClearIcon className={classes.clearIcon}/> }
                </Typography>
            </Grid>
            <Grid conrainer xs={12} justify="flex-start">
                <Grid item xs={6}>
                    <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                        Roles:
                    </Typography>
                    <Typography className={classes.text} color="inherit" variant="subtitle1" component="div"  align="center">
                        {ROLE_CLIENT}:  { roleClientActive
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> }
                        {ROLE_EMPLOYEE}:  { roleEmployeeActive
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> }
                        {ROLE_ADMIN}:  { roleAdminActive
                                            ? <DoneIcon className={classes.doneIcon}/> 
                                            : <ClearIcon className={classes.clearIcon}/> }
                    </Typography>
                </Grid>
            </Grid>
        </Grid>
    );
}
export default AccountDetails;