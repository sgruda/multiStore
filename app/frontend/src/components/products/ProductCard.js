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

function ProductCard({product}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const handleClickCard = () => {
      alert("hej dodwanie do koszyka się robi")
      ////Todo
  }

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
    <Grid item xs={12} sm={6} md={3} key={product.id}>
        <Card
            className={clsx(classes.card, {
                [classes.inactiveCard]: !product.active,
            })}
        >
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
            title={t('product.fields.type.' + product.type)}
            subheader={t('product.fields.category.' + product.category)}
        />
        <CardActionArea disabled={!product.active} onClick={handleClickCard}>
            <CardContent className={classes.cardContent}>
                <Typography gutterBottom variant="h5" component="h2">
                    {product.title}
                </Typography>
                {product.active ? 
                <div>    
                    <Typography variant="body6" color="textSecondary" component="p">
                        W magazynie: {getIconForNumberInStore(product.inStore)}
                    </Typography>           
                    <Typography variant="body2" color="textSecondary" component="p">
                        { product.description.length <= 197 
                            ? product.description 
                            : product.description.substring(0, 197) + '...'
                        }
                    </Typography>
                </div>
                :
                    <Typography className={classes.inactiveText}>
                        {t('product.fields.inactive')}
                    </Typography>
                }
            </CardContent>
        </CardActionArea>
        <CardActions>
                <Grid item xs={12}>
                    <Typography variant="body6" className={classes.priceText} component="p">
                        {t('product.fields.price')}: {product.price}
                    </Typography>
                </Grid>
        </CardActions>
        </Card>
    </Grid>
  );
}
export default ProductCard;