import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import CircularProgress from '@material-ui/core/CircularProgress';
import Pagination from '@material-ui/lab/Pagination';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import Backdrop from '@material-ui/core/Backdrop';

import ProductService from '../../services/ProductService';
import ProductCard from '../../components/products/ProductCard';
import ProductCardsSettingsView from '../../components/products/ProductCardsSettingsView';
import ProductDetails from '../../components/products/ProductDetails';
import ProductEdit from '../../components/products/ProductEdit';
import { ROLE_EMPLOYEE } from '../../config/config';
import ProductEditDetailsHelper from '../../components/products/ProductEditDetailsHandler';

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
    details: {
        backgroundColor: '#e6f3fa',
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
  const [cardsPerPage, setCardsPerPage] = useState(7);
  const [textToSearch, setTextToSearch] = useState(null);
  const [filterActiveProducts, setFilterActiveProducts] = useState(null);
  const [filterType, setFilterType] = useState(null);

  const [selectedProductTitle, setSelectedProductTitle] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const [showEdit, setShowEdit] = useState(false);

  const {activeRole, checkExpiredJWTAndExecute} = useAuth();

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
    setLoadingData(true);
  }

  const emptyCards = cardsPerPage - Math.abs(Math.min(cardsPerPage, totalItems  - (page - 1) * cardsPerPage));

  const handleSearch = (text) => {
      setTextToSearch(text);
      setPage(1);
      setLoadingData(true);
  }

  const handleRefresh = () => {
    setFilterType(null);
    setFilterActiveProducts(null);
    setTextToSearch(null);
    setCardsPerPage(7);
    setPage(1);
    setLoadingData(true);
  }

  const handleCloseDetails = () => {
    setShowDetails(false);
    setShowEdit(false);
    setSelectedProductTitle(null);
  }


  async function getProducts() {
    await ProductService.getProducts(textToSearch === '' ? null : textToSearch, page - 1, cardsPerPage, filterType, filterActiveProducts)
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
  console.log("activeRole")
    if (loadingData) {
        setLoadingData(false);
        checkExpiredJWTAndExecute(getProducts);
    }
}, [loadingData]);

  return (
    <div>
        {!loadingData ? (
        <Grid container spacing={3} className={classes.gridContainer} justify="center">
          <ProductCardsSettingsView
            activeRole={activeRole}
            setFilterActiveProducts={setFilterActiveProducts}
            setFilterTypeProducts={setFilterType}
            handleSearch={handleSearch}
            handleRefresh={handleRefresh}
          />
          {products.map(
            (product) =>
              <ProductCard
                product={product}
                setSelectedProduct={setSelectedProductTitle}
                setShowDetails={setShowDetails}
                showBackdrop={showDetails}
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
      <Backdrop in={showDetails}/>
      <Dialog
        open={showDetails}
        onClose={handleCloseDetails}
        aria-describedby="dialog-description"
       >
        <DialogContent className={classes.details}>
          <DialogContentText id="dialog-description">
            <ProductEditDetailsHelper
              productTitle={selectedProductTitle}
              showEdit={showEdit}
            />
          </DialogContentText>
        </DialogContent>
        <DialogActions className={classes.details}>
          { activeRole === ROLE_EMPLOYEE 
            ?
              <Button onClick={() => setShowEdit(true)} color="primary" autoFocus>
                {t('button.edit')}
              </Button>
            :
              <></>
          }
          <Button onClick={handleCloseDetails} color="primary" autoFocus>
            {t('button.close')}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
export default ProductList;