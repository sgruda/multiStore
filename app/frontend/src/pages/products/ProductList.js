import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import CircularProgress from '@material-ui/core/CircularProgress';
import Pagination from '@material-ui/lab/Pagination';

import ProductService from '../../services/ProductService';
import ProductCard from '../../components/products/ProductCard';

const useStyles = makeStyles((theme) => ({
    gridContainer: {
        paddingTop: "20px",
        paddingLeft: "50px",
        paddingRight: "50px",
    },
    pagination: {
        paddingLeft: "43%",
        paddingRight: "43%",
        '& > *': {
            marginTop: theme.spacing(3),
        },
    },
}));

function ProductList() {
  const classes = useStyles();
  const { t } = useTranslation();

  const [loadingData, setLoadingData] = useState(true);   
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [cardsPerPage, setCardsPerPage] = useState(8);
  const [textToSearch, setTextToSearch] = useState(null);
  const [filterActiveProducts, setFilterActiveProducts] = useState(null);
  const [filterType, setFilterType] = useState(null);

  const {checkExpiredJWTAndExecute} = useAuth();

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
    setLoadingData(true);
  }

  const emptyCards = cardsPerPage - Math.abs(Math.min(cardsPerPage, totalItems - (page - 1) * cardsPerPage));

  async function getProducts() {
    await ProductService.getProducts(textToSearch, page - 1, cardsPerPage, filterType, filterActiveProducts)
    .then(response => {
        if (response.status === 200) { 
            const products = response.data.products.map(product => {
                return {
                  id: product.idHash,
                  title: product.title,
                  description: product.description,
                  inStore: product.inStore,
                  price: product.price,
                  type: product.type,
                  category: product.category,
                  active: product.active,
                };
            });
            setProducts(products);

            setPage(response.data.currentPage + 1);
            setTotalPages(response.data.totalPages);
            setTotalItems(response.data.totalItems);
        }
    },
        (error) => {
        const resMessage =
            (error.response && error.response.data && error.response.data.message) 
            || error.message || error.toString();
            console.error("ProductList: " + resMessage);
        }
    );
}

useEffect(() => {
    if (loadingData) {
        setLoadingData(false);
        checkExpiredJWTAndExecute(getProducts);
    }
}, [loadingData]);

  return (
    <div>
        {!loadingData ? (
        <Grid container spacing={3} className={classes.gridContainer} justify="center">
          {products.map(
            (product) =>
              <ProductCard
                product={product}
              /> 
          )}
          {emptyCards > 0 && (
              <Grid item  style={{ height: 100 * emptyCards }}/>
            )}
          <Grid item xs={12}>
            <Pagination
                className={classes.pagination} 
                color="primary"
                count={totalPages}
                page={page}
                onChange={handleChangePage}
            />
          </Grid>
        </Grid>
      ) : (
        <CircularProgress />
      )}
    </div>
  );
}
export default ProductList;