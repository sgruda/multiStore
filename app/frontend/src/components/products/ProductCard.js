import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';

const useStyles = makeStyles({
    card: {
        width: 300,
        height: 200,
        backgroundColor: '#d3ebf8',
    },
    hover:{},
    cardContent: {
        textAlign: "center",
        height: 120,
    },
});

function ProductCard({product}) {
  const classes = useStyles();
  const { t } = useTranslation();

  return (
    <Grid item xs={3} key={product.id}>
        <Card className={classes.card}>
        <CardActionArea>
            <CardContent className={classes.cardContent}>
            <Typography gutterBottom variant="h5" component="h2">
                {product.title}
            </Typography>
            <Typography variant="body2" color="textSecondary" component="p">
                {product.description}
            </Typography>
            </CardContent>
        </CardActionArea>
        <CardActions>
            <Button size="small" color="primary">
            Share
            </Button>
            <Button size="small" color="primary">
            Learn More
            </Button>
        </CardActions>
        </Card>
    </Grid>
  );
}
export default ProductCard;