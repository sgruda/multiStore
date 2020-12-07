import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import BookIcon from '@material-ui/icons/Book';
import MovieIcon from '@material-ui/icons/Movie';
import SignalCellularConnectedNoInternet0BarIcon from '@material-ui/icons/SignalCellularConnectedNoInternet0Bar';
import SignalCellular1BarIcon from '@material-ui/icons/SignalCellular1Bar';
import SignalCellular2BarIcon from '@material-ui/icons/SignalCellular2Bar';
import SignalCellular3BarIcon from '@material-ui/icons/SignalCellular3Bar';
import SignalCellular4BarIcon from '@material-ui/icons/SignalCellular4Bar';
import Divider from "@material-ui/core/Divider";
import Grid from '@material-ui/core/Grid';

import { useAuth } from '../../context/AuthContext';
import ProductService from '../../services/ProductService';

const useStyles = makeStyles({
    root: {
        minWidth: 500,
        minHeight: 400,
    },
    avatar: {
        backgroundColor: '#432deb',
    },
    priceText: {
        color: '#0bb00d',
        textAlign:'center',
        fontSize: 17,
    },
    inStoreIcon: {
        color: '#432deb',
    },
});

function ProductEdit({product}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const {checkExpiredJWTAndExecute} = useAuth();


//   async function getProduct() {
//     await ProductService.getProduct(productTitle)
//     .then(response => {
//         if (response.status === 200) { 
//             setProduct(response.data);               
//         }
//     },
//         (error) => {
//         const resMessage =
//             (error.response && error.response.data && error.response.data.message) 
//             || error.message || error.toString();
//             console.error("ProductEdit: " + resMessage);
//             // if(resMessage === "error.product.not.exists") {
//             //     handleHardRefresh();
//             // }
//         }
//     );
//   }

//   useEffect(() => {
//     checkExpiredJWTAndExecute(getProduct);
//   }, [productTitle]);  

  return (
    <Grid container spacing={2} className={classes.root}>
        {/* <CardHeader
            avatar={
            <Avatar aria-label="productType" className={classes.avatar}>
                {product.type === 'book' ? <BookIcon/> : <MovieIcon/> }
            </Avatar>
            }
            // action={
            // // <IconButton aria-label="settings">
            // //     <MoreVertIcon />
            // // </IconButton>
            // }
            title={t('product.details.type') + ': ' + t('product.fields.type.' + product.type)}
            subheader={t('product.details.category') + ': ' + t('product.fields.category.' + product.category)}
        />
        <CardContent>
            <Typography gutterBottom variant="h5" component="h2">
                {t('product.details.title') + ': ' + product.title}
            </Typography>
            
            <Grid item xs={6}>
                <TextField
                    value={ fields.inStore }
                    onChange={ setFields }
                    variant="outlined"
                    type="number"
                    required
                    fullWidth
                    id="inStore"
                    label={t('product.create.form.inStore')}
                    name="inStore"
                    autoComplete="inStore"

                    inputRef={register({ required: true,  pattern: /[0-9.]+/ })}
                    error={errors.inStore ? true : false}
                    helperText={errors.inStore ? t('validation.message.incorrect.entry') : ""}
                />
            </Grid>

            <Divider/>         
            <Typography color="textSecondary" component="p">
                { t('product.details.description') + ': ' + product.description }
            </Typography>
            <Divider/>
            <Typography className={classes.inactiveText}>
                {t('product.details.active') + ': ' + t('product.fields.inactive')}
            </Typography>
            <Divider/>
            <Typography variant="body6" className={classes.priceText} component="p">
                {t('product.fields.price')}: {product.price}
            </Typography>
        </CardContent> */}
    </Grid>
  );
}
export default ProductEdit;