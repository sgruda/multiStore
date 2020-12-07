import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';

import { Grid } from "@material-ui/core";

import FormControl from '@material-ui/core/FormControl';
import NativeSelect from '@material-ui/core/NativeSelect';


const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 270,
    },
}));

function ProductTypeFilter({setFilterTypeProducts}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const handleChange = (event) => {
    event.target.value === 'all' ? setFilterTypeProducts(null) : setFilterTypeProducts(event.target.value);
  }

  useEffect(() => {
    setFilterTypeProducts(null);  
  }, []);

  return (
    <Grid item xs={6}>
        <FormControl className={classes.formControl}>
        <NativeSelect
          onChange={(event) => handleChange(event)}
          fullWidth
        >
            <option value={null}>{t("product.list.filter.all")}</option>
            <option value={'book'}>{t("product.list.filter.only-books")}</option>
            <option value={'movie'}>{t("product.list.filter.only-movies")}</option>
        </NativeSelect>
      </FormControl>
    </Grid>
  );
}
export default ProductTypeFilter;