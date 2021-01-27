import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import BookIcon from '@material-ui/icons/Book';
import DesktopWindowsIcon from '@material-ui/icons/DesktopWindows';
import SignalCellularConnectedNoInternet0BarIcon from '@material-ui/icons/SignalCellularConnectedNoInternet0Bar';
import SignalCellular1BarIcon from '@material-ui/icons/SignalCellular1Bar';
import SignalCellular2BarIcon from '@material-ui/icons/SignalCellular2Bar';
import SignalCellular3BarIcon from '@material-ui/icons/SignalCellular3Bar';
import SignalCellular4BarIcon from '@material-ui/icons/SignalCellular4Bar';
import Divider from "@material-ui/core/Divider";

import { useAuth } from '../../context/AuthContext';
import ProductActivitySwitch from './forms/ProductActivitySwitch';
import { ROLE_EMPLOYEE } from '../../config/config';
import { Grid } from '@material-ui/core';

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

function ProductDetails({product, handleLoadingData}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const { activeRole } = useAuth();

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


  return (
    <div className={classes.root}>
        <CardHeader
            avatar={
            <Avatar aria-label="productType" className={classes.avatar}>
                {product.type === 'book' ? <BookIcon/> : <DesktopWindowsIcon/> }
            </Avatar>
            }
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
                <Grid container xs={12} alignItems="center">
                    <Grid item xs={6}>
                        {product.active 
                            ? t('product.details.active') + ': ' + t('product.fields.active')
                            : t('product.details.active') + ': ' + t('product.fields.inactive')
                        }
                    </Grid>
                    <Grid item xs={6}>
                        {activeRole === ROLE_EMPLOYEE
                            ?
                                <ProductActivitySwitch
                                    classes={classes}
                                    product={product}
                                    handleLoadingData={handleLoadingData}
                                />
                            : <></>
                        }
                    </Grid>
                </Grid>
            </Typography>
            <Divider/>
            <Typography variant="body6" className={classes.priceText} component="p">
                {t('product.fields.price')}: {product.price + " PLN"}
            </Typography>
        </CardContent>
    </div>
  );
}
export default ProductDetails;