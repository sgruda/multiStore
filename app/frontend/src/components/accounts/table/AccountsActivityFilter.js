import React from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';

import { Grid } from "@material-ui/core";

import InputLabel from '@material-ui/core/InputLabel';
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
  const { t } = useTranslation();

  const handleChange = (event) => {
    event.target.value === 'all' ? setFilterActiveAccounts(null) : setFilterActiveAccounts(event.target.value);
  }

  return (
    <Grid item xs={6}>
        <FormControl className={classes.formControl}>
        <InputLabel>{t("account.list.table.filter.label")}</InputLabel>
        <NativeSelect
          onChange={(event) => handleChange(event)}
          fullWidth
        >
            <option value={null}>{t("account.list.table.filter.all")}</option>
            <option value={true}>{t("account.list.table.filter.only-active")}</option>
            <option value={false}>{t("account.list.table.filter.only-non-active")}</option>
        </NativeSelect>
      </FormControl>
    </Grid>
  );
}
export default AccountActivityFilter;