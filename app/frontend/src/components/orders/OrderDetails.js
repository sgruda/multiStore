import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import DescriptionIcon from '@material-ui/icons/Description';
import Divider from '@material-ui/core/Divider';

import { useAuth } from '../../context/AuthContext';
import { ROLE_EMPLOYEE } from '../../config/config';
import { Grid } from '@material-ui/core';
import OrderItemCard from './OrderedItemCard';
import OrderedItemCard from './OrderedItemCard';

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
    boldText: {
        fontWeight: 'bold'
    },
});

function OrderDetails({order}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const { activeRole } = useAuth();


  return (
    <div className={classes.root}>
        <CardHeader
            avatar={
            <Avatar aria-label="identificatior" className={classes.avatar}>
                {<DescriptionIcon/>}
            </Avatar>
            }
            title={t('order.details.identifier') + ': ' + order.identifier }
            subheader={t('order.details.orderDate') + ': ' +
                        new Intl.DateTimeFormat("en-GB", {
                        year: 'numeric', month: 'numeric', day: 'numeric',
                        hour: 'numeric', minute: 'numeric',
                        hour12: false,
                        }).format(new Date(order.orderDate))
            }
        />
        <CardContent>
            {/* <Typography gutterBottom variant="h5" component="h2">
                {t('order.details.buyerEmail') + ': ' + product.title}
            </Typography> */}
            <Typography variant="body6" color="textSecondary" component="p">
                {t('order.details.buyerEmail') + ': ' + order.buyerEmail}
            </Typography>  
            <Divider/>     
            <Typography variant="body6" color="textSecondary" component="p" className={classes.boldText}>
                {t('order.details.orderedProducts') + ':'}
            </Typography>     
            <Grid container direction="row" justify="center" spacing={2}>
                {order.orderedItemDTOS.map(item => {
                    return (
                        <OrderedItemCard
                            product={item.orderedProduct}
                            orderedNumber={item.orderedNumber}
                        />
                    );
                })}
            </Grid>
            <Divider/>
            <Typography variant="body6" component="p">
                {t('order.details.totalPrice')}: {order.totalPrice}
            </Typography>
            <Divider/>
            <Typography variant="body6" color="textSecondary" component="p">
                {t('order.details.status') + ': ' + t('order.status.' + order.status)}
            </Typography> 
            <Divider/> 
            <Typography variant="body6" color="textSecondary" component="p">
                {t('order.details.address') + ': ' + order.address}
            </Typography>  
        </CardContent>
    </div>
  );
}
export default OrderDetails;