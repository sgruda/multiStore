import { Button } from "@material-ui/core";
import React, {useState, useEffect} from "react";
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';

import AccountService from '../../services/AccountService';

function SelectedAccountDetails({classes, selectedAccountMail}) {
    const [account, setAccount] = useState(null);
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
        // <Box>
            <Grid container spacing={3} direction="column" justify="center" alignItems="stretch">
                <Grid item xs={6} sm={3}> fafaf
                
                </Grid>
                <Grid item xs={6} sm={3}>ssafasf
                </Grid>
                <Grid item xs={6} sm={3}>fasfasf
                </Grid>
            </Grid>
        // </Box>
    );
}
export default SelectedAccountDetails;