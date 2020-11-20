import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';

import Typography from '@material-ui/core/Typography';
import { Grid } from "@material-ui/core";

import InputLabel from '@material-ui/core/InputLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import NativeSelect from '@material-ui/core/NativeSelect';


const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 200,
    },
}));

function AccountActivityFilter({setFilterActiveAccounts}) {
  const classes = useStyles();

  const handleChange = (event) => {
    event.target.value === 'all' ? setFilterActiveAccounts(null) : setFilterActiveAccounts(event.target.value);
  }

  return (
    <Grid item xs={6}>
        <FormControl className={classes.formControl}>
        <InputLabel>Show accounts</InputLabel>
        <NativeSelect
          onChange={(event) => handleChange(event)}
          fullWidth
        >
            <option value={null}>all</option>
            <option value={true}>Only active</option>
            <option value={false}>Only non active</option>
        </NativeSelect>
      </FormControl>
    </Grid>
  );
}
export default AccountActivityFilter;