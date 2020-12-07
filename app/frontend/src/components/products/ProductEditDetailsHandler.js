import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import ProductService from '../../services/ProductService';

import ProductDetails from './ProductDetails';
import ProductEdit from './ProductEdit';

function ProductEditDetailsHelper({productTitle, showEdit}) {
  const [product, setProduct] = useState(Object);
  const {checkExpiredJWTAndExecute} = useAuth();


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
    <div>
        {showEdit
            ?
                <ProductEdit
                    product={product}
                />
            :
                <ProductDetails
                    product={product}
                />
        }
    </div>
  );
}
export default ProductEditDetailsHelper;