import React, {useState, useEffect} from "react";
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';



import AccountService from '../../services/AccountService';


const useStyles = makeStyles({
    doneIcon: {
        color: "#0bb00d",
    },
    clearIcon: {
        color: "#eb1e1e"
    },

});


function AccountDetails({selectedAccountMail}) {
    const classes = useStyles();
    const [account, setAccount] = useState(Object);
    const [loadingData, setLoadingData] = useState(true);

    async function getAccount() {
        await AccountService.getSingleAccount(selectedAccountMail)
        .then(response => {
            if (response.status === 200) { 
                setAccount(response.data);
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
            <Grid item xs={6}>
                First Name: {account.firstName}
            </Grid>
            <Grid item xs={6}>
                Last Name: {account.lastName}
            </Grid>
            <Grid item xs={6}>
                E-mail: {account.email}
            </Grid>
            <Grid item xs={6} alignItems="center">
                Active: 
                {account.active 
                    ? <DoneIcon className={classes.doneIcon}/> 
                    : <ClearIcon className={classes.clearIcon}/> }
            </Grid>
            <Grid item xs={12}>
                Roles: {account.roles}
            </Grid>
        </Grid>
    );
}
export default AccountDetails;