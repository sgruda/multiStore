import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';

import { Grid } from "@material-ui/core";

import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import NativeSelect from '@material-ui/core/NativeSelect';


const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 270,
    },
}));

function ProductActivityFilter({setFilterActiveProducts}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const handleChange = (event) => {
    event.target.value === 'all' ? setFilterActiveProducts(null) : setFilterActiveProducts(event.target.value);
  }

  useEffect(() => {
    setFilterActiveProducts(null);  
  }, []);

  return (
    <Grid item xs={4}>
        <FormControl className={classes.formControl} fullWidth>
        <InputLabel>{t("product.list.filter.label.activity")}</InputLabel>
        <NativeSelect
          onChange={(event) => handleChange(event)}
          fullWidth
        >
            <option value={null}>{t("product.list.filter.all")}</option>
            <option value={true}>{t("product.list.filter.only-active")}</option>
            <option value={false}>{t("product.list.filter.only-non-active")}</option>
        </NativeSelect>
      </FormControl>
    </Grid>
  );
}
export default ProductActivityFilter;