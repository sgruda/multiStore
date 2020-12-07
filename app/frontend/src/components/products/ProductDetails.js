import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import Avatar from '@material-ui/core/Avatar';
import BookIcon from '@material-ui/icons/Book';
import MovieIcon from '@material-ui/icons/Movie';
import SignalCellularConnectedNoInternet0BarIcon from '@material-ui/icons/SignalCellularConnectedNoInternet0Bar';
import SignalCellular1BarIcon from '@material-ui/icons/SignalCellular1Bar';
import SignalCellular2BarIcon from '@material-ui/icons/SignalCellular2Bar';
import SignalCellular3BarIcon from '@material-ui/icons/SignalCellular3Bar';
import SignalCellular4BarIcon from '@material-ui/icons/SignalCellular4Bar';
import Divider from "@material-ui/core/Divider";

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

function ProductDetails({productTitle}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const [product, setProduct] = useState(Object);

  const {checkExpiredJWTAndExecute} = useAuth();

  const getIconForNumberInStore = (inStore) => {
    if (inStore === 0)
        return <SignalCellularConnectedNoInternet0BarIcon className={classes.inStoreIcon}/>
    else if(inStore < 20)
        return <SignalCellular1BarIcon className={classes.inStoreIcon}/>
    else if(inStore < 50)
        return <SignalCellular2BarIcon className={classes.inStoreIcon}/>
    else if(inStore < 100)
        return <SignalCellular3BarIcon className={classes.inStoreIcon}/>
    else if(inStore >= 100)
        return <SignalCellular4BarIcon className={classes.inStoreIcon}/>    
  }

  async function getProduct() {
    await ProductService.getProduct(productTitle)
    .then(response => {
        if (response.status === 200) { 
            setProduct(response.data);               
        }
    },
        (error) => {
        const resMessage =
            (error.response && error.response.data && error.response.data.message) 
            || error.message || error.toString();
            console.error("ProductDetails: " + resMessage);
            // if(resMessage === "error.product.not.exists") {
            //     handleHardRefresh();
            // }
        }
    );
  }

  useEffect(() => {
    checkExpiredJWTAndExecute(getProduct);
  }, [productTitle]);  

  return (
    <div className={classes.root}>
        <CardHeader
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
            <Typography variant="body6" color="textSecondary" component="p">
                {t('product.details.inStore') + ': '}
                {getIconForNumberInStore(product.inStore)}
            </Typography>  
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
        </CardContent>
        <CardActions>
                <Grid item xs={12}>
                   
                </Grid>
        </CardActions>
    </div>
  );
}
export default ProductDetails;