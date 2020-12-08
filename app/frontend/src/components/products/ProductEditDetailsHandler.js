import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import ProductService from '../../services/ProductService';

import ProductDetails from './ProductDetails';
import ProductEdit from './ProductEdit';

function ProductEditDetailsHelper({productTitle, showEdit, handleClose}) {
  const [product, setProduct] = useState(Object);
  const [loadingData, setLoadingData] = useState(true);
  const {checkExpiredJWTAndExecute} = useAuth();


  async function getProduct() {
    if(productTitle != null) {
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
  }

  useEffect(() => {
    checkExpiredJWTAndExecute(getProduct);
    setLoadingData(false);
  }, [productTitle, loadingData]);  

  return (
    <div>
        {showEdit
            ?
                <ProductEdit
                    product={product}
                    handleClose={handleClose}
                    handleRefresh={setLoadingData}
                />
            :
                <ProductDetails
                    product={product}
                    loadingData={loadingData}
                />
        }
    </div>
  );
}
export default ProductEditDetailsHelper;