import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useAuth } from '../../context/AuthContext';
import { useTranslation } from 'react-i18next';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import Avatar from '@material-ui/core/Avatar';
import BookIcon from '@material-ui/icons/Book';
import MovieIcon from '@material-ui/icons/Movie';

const useStyles = makeStyles({
    avatar: {
        backgroundColor: '#432deb',
    },
    category: {
        backgroundColor: '#0bb00d',
    },
    card: {
        width: 250,
        height: 120,
    },
    cardHeader: {
        height: '15%',
    },
    cardContent: {
        textAlign: "center",
        height: '75%',
    },
});

function OrderedItemCard({product, orderedNumber}) {
  const classes = useStyles();
  const { t } = useTranslation();

  const { activeRole } = useAuth();


  return (
    <Grid item xs={12} sm={8} md={6} key={product.id}>
        <Card className={classes.card}>
        <CardHeader
            avatar={
            <Avatar aria-label="avatar" className={classes.avatar}>
                {product.type === 'book' ? <BookIcon /> : <MovieIcon/> }
            </Avatar>
            }
            title={t('order.details.product.title') + ': ' + product.title}
            className={classes.cardHeader}
        />
        <CardContent className={classes.cardContent}>
                <Typography variant="body6" color="textSecondary" component="p">
                    {t('order.details.product.orderedNumber') + ': ' + orderedNumber}
                </Typography>   
                <Typography variant="body6" color="textSecondary" component="p">
                    {t('order.details.product.price') + ': ' + product.price + " PLN"}
                </Typography>            
        </CardContent>
        </Card>
    </Grid>
  );
}
export default OrderedItemCard;