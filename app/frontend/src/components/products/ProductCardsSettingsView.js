import React from 'react';
import { useForm } from "react-hook-form";
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import SearchIcon from '@material-ui/icons/Search';
import SyncIcon from '@material-ui/icons/Sync';
import Divider from "@material-ui/core/Divider";

import TextFilter from './filtering/TextFilter';
import ProductActivityFilter from './filtering/ProductActivityFilter';
import ProductTypeFilter from './filtering/ProductTypeFilter'
import { useFields } from '../../hooks/FieldHook';

const useStyles = makeStyles({
    card: {
        minWidth: '100%',
        minHeight: 100,
        backgroundColor: '#4dc4ff',
        paddingLeft: '2%',
        paddingRight: '2%'
    },
});

function ProductCardsSettingsView({activeRole, setFilterActiveProducts, setFilterTypeProducts, handleSearch, handleRefresh}) {
  const classes = useStyles();
  const { t } = useTranslation();
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [fields, setFields] = useFields({
    textToSearch: ""
  });

  const handleSearchProducts = () => {
    handleSearch(fields.textToSearch);
  }

  return (
    <form noValidate onSubmit={handleSubmit(handleSearchProducts)}>
    <Grid container xs={12} sm={6} md={3} justify="center" className={classes.card}>
        <TextFilter
            fields={fields}
            setFields={setFields}
            register={register}
            errors={errors}
        />
        <Divider/>
        <ProductActivityFilter
            setFilterActiveProducts={setFilterActiveProducts}
        />
        <ProductTypeFilter
            setFilterTypeProducts={setFilterTypeProducts}
        />
        <Grid item xs={3} justify="center">
            <Button aria-label="Search" type="submit"  startIcon={<SearchIcon/>}>
                {t('button.search')}
            </Button>
            <Button aria-label="Refresh"  startIcon={<SyncIcon/>} onClick={handleRefresh}>
                {t('button.refresh')}
            </Button>
        </Grid>
    </Grid>                
    </form>

  );
}
export default ProductCardsSettingsView;