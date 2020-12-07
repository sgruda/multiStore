import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import Avatar from '@material-ui/core/Avatar';
import BookIcon from '@material-ui/icons/Book';
import MovieIcon from '@material-ui/icons/Movie';
import clsx from 'clsx';
import { Collapse } from '@material-ui/core';
import SignalCellularConnectedNoInternet0BarIcon from '@material-ui/icons/SignalCellularConnectedNoInternet0Bar';
import SignalCellular1BarIcon from '@material-ui/icons/SignalCellular1Bar';
import SignalCellular2BarIcon from '@material-ui/icons/SignalCellular2Bar';
import SignalCellular3BarIcon from '@material-ui/icons/SignalCellular3Bar';
import SignalCellular4BarIcon from '@material-ui/icons/SignalCellular4Bar';
import Backdrop from '@material-ui/core/Backdrop';


const useStyles = makeStyles({
    avatar: {
        backgroundColor: '#432deb',
    },
    category: {
        backgroundColor: '#0bb00d',
    },
    card: {
        width: 300,
        height: 300,
        backgroundColor: '#d3ebf8',
        "&:hover": {
            backgroundColor: "#7cc3eb"
        }
    },
    inactiveCard: {
        width: 300,
        height: 300,
        backgroundColor: '#859299',
        "&:hover": {
            backgroundColor: "#859299"
        }
    },
    detailsAtFront: {
        backgroundColor: '#4e5457',
        color: '#5c6469',
    },
    inactiveText: {
        displayBlock: "true",
        transform: 'rotate(45deg)',
        fontSize: 37,
        textAlign:'center'
    },
    priceText: {
        color: '#0bb00d',
        textAlign:'center',
        fontSize: 17,
    },
    cardContent: {
        textAlign: "center",
        height: 150,
    },
    inStoreIcon: {
        color: '#432deb',
    },
});

function ProductCard({product, setSelectedProduct, setShowDetails, showBackdrop}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const handleClickCard = () => {
    setSelectedProduct(product.title);
    setShowDetails(true);
  }

  const getIconForNumberInStore = (inStore) => {
    if (inStore === 0)
        return <SignalCellularConnectedNoInternet0BarIcon className={clsx({[classes.detailsAtFront]: showBackdrop, [classes.inStoreIcon]: !showBackdrop})}/>
    else if(inStore < 20)
        return <SignalCellular1BarIcon className={clsx({[classes.detailsAtFront]: showBackdrop, [classes.inStoreIcon]: !showBackdrop})}/>
    else if(inStore < 50)
        return <SignalCellular2BarIcon className={clsx({[classes.detailsAtFront]: showBackdrop, [classes.inStoreIcon]: !showBackdrop})}/>
    else if(inStore < 100)
        return <SignalCellular3BarIcon className={clsx({[classes.detailsAtFront]: showBackdrop, [classes.inStoreIcon]: !showBackdrop})}/>
    else if(inStore >= 100)
        return <SignalCellular4BarIcon className={clsx({[classes.detailsAtFront]: showBackdrop, [classes.inStoreIcon]: !showBackdrop})}/>
}

  return (
    <Grid item xs={12} sm={6} md={3} key={product.id}>
        <Card
            className={clsx(classes.card, {
                [classes.inactiveCard]: !product.active,
                [classes.detailsAtFront]: showBackdrop
            })}
        >
        <CardHeader
            avatar={
            <Avatar aria-label="productType" 
                className={clsx(classes.avatar, {
                    [classes.detailsAtFront]: showBackdrop
                })}
            >
                {product.type === 'book' 
                ? <BookIcon 
                    className={clsx({
                        [classes.detailsAtFront]: showBackdrop
                    })}
                    /> 
                : <MovieIcon
                        className={clsx({
                            [classes.detailsAtFront]: showBackdrop
                        })}
                    /> 
                }
            </Avatar>
            }
            title={t('product.fields.type.' + product.type)}
            subheader={t('product.fields.category.' + product.category)}
        />
        <CardActionArea disabled={!product.active} onClick={handleClickCard}>
            <CardContent 
                className={clsx(classes.cardContent, {
                    [classes.detailsAtFront]: showBackdrop
                })}
            >
                <Typography gutterBottom variant="h5" component="h2"  className={clsx({[classes.detailsAtFront]: showBackdrop})}>
                    {product.title}
                </Typography>
                {product.active ? 
                <div>    
                    <Typography variant="body6" color="textSecondary" component="p" className={clsx({[classes.detailsAtFront]: showBackdrop})}>
                        {t('product.details.inStore') + ' '}
                        {getIconForNumberInStore(product.inStore)}
                    </Typography>           
                    <Typography variant="body2" color="textSecondary" component="p" className={clsx({[classes.detailsAtFront]: showBackdrop})}>
                        { product.description.length <= 197 
                            ? product.description 
                            : product.description.substring(0, 197) + '...'
                        }
                    </Typography>
                </div>
                :
                    <Typography className={classes.inactiveText} className={clsx({[classes.detailsAtFront]: showBackdrop})}>
                        {t('product.fields.inactive')}
                    </Typography>
                }
            </CardContent>
        </CardActionArea>
        <CardActions>
                <Grid item xs={12}>
                    <Typography variant="body6" className={clsx({[classes.detailsAtFront]: showBackdrop, [classes.priceText]: !showBackdrop})} component="p">
                        {t('product.fields.price')}: {product.price}
                    </Typography>
                </Grid>
        </CardActions>
        </Card>
    </Grid>
  );
}
export default ProductCard;