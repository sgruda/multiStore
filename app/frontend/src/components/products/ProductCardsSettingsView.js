import React from 'react';
import { useForm } from "react-hook-form";
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Avatar from '@material-ui/core/Avatar';
import SearchIcon from '@material-ui/icons/Search';
import SyncIcon from '@material-ui/icons/Sync';
import Divider from "@material-ui/core/Divider";
import SettingsIcon from '@material-ui/icons/Settings';

import TextFilter from './filtering/TextFilter';
import ProductActivityFilter from './filtering/ProductActivityFilter';
import ProductTypeFilter from './filtering/ProductTypeFilter'
import { useFields } from '../../hooks/FieldHook';

const useStyles = makeStyles({
    avatar: {
        backgroundColor: '#3523ba',
    },
    card: {
        width: 300,
        height: 300,
        backgroundColor: '#4dc4ff',
    },
    cardContent: {
        textAlign: "center",
        height: 150,
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
    <Grid item xs={12} sm={6} md={3}>
        <Card className={classes.card}>
            <CardHeader
                avatar={
                <Avatar aria-label="settingsIcon" className={classes.avatar}>
                    <SettingsIcon/>
                </Avatar>
                }
                title={t('product.list.view-settings')}
            />
            <CardContent className={classes.cardContent} >
                <form noValidate onSubmit={handleSubmit(handleSearchProducts)}>
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
                    <Grid item xs={12}>
                        <Button aria-label="Search" type="submit" fullWidth startIcon={<SearchIcon/>}>
                            {t('button.search')}
                        </Button>
                    </Grid>
                </form>
            </CardContent>
            <CardActions>
                <Grid item xs={12}>
                    <Button aria-label="Refresh" fullWidth startIcon={<SyncIcon/>} onClick={handleRefresh}>
                        {t('button.refresh')}
                    </Button>
                </Grid>
            </CardActions>
        </Card>
    </Grid>
  );
}
export default ProductCardsSettingsView;