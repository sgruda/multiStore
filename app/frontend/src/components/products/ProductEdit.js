import React, { useState, useEffect } from 'react';
import { useForm } from "react-hook-form";
import { useFields } from '../../hooks/FieldHook';

import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import BookIcon from '@material-ui/icons/Book';
import MovieIcon from '@material-ui/icons/Movie';
import CircularProgress from '@material-ui/core/CircularProgress';
import AlertApiResponseHandler from '../../components/AlertApiResponseHandler';
import { useAuth } from '../../context/AuthContext';
import ProductService from '../../services/ProductService';
import ProductEditFrom from './forms/ProductEditForm';
import AcceptButtons from '../../components/AcceptButtons';
import ConfirmDialog from '../../components/ConfirmDialog';

const useStyles = makeStyles((theme) => ({
    root: {
        minWidth: 500,
        minHeight: 400,
    },
    avatar: {
        backgroundColor: '#432deb',
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
        backgroundColor: "#4285F4",
        "&:hover": {
            backgroundColor: "#2c0fab"
        }
    },
    circularProgress: {
        position: 'absolute',
        top: '42%',
        left: '47%',
        margin: theme.spacing(3, 0, 2),
        color: "#4285F4",
    },
}));

function ProductEdit({product, handleClose, handleRefresh}) {
  const classes = useStyles();
  const { t } = useTranslation();
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [loading, setLoading] = useState(false);

  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [showRefresh, setShowRefresh] = useState(false);
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);

  const {checkExpiredJWTAndExecute} = useAuth();

  const [fields, setFields] = useFields({
    description: product.description,
    inStore: product.inStore,
    price: product.price,
  });

  const handleConfirmDialog = () => {
    setOpenConfirmDialog(!openConfirmDialog);
  }

  const handleEditProduct = () => {
    setLoading(true);
    checkExpiredJWTAndExecute(editProduct);
    setLoading(false);
    handleConfirmDialog();
  }


  const convertValidationMessage = (message) => {
    if(message.startsWith("error")) return t(message);
    let retMessage = '';
    message = message.replace('{', '').replace('}', '')
    let parts = message.split(", ");
    parts.map(part => {
      let fieldError = part.split('=');
      let code = fieldError[1] + '.' + fieldError[0];
      retMessage += t(code) + ' ';
    })
    return retMessage;
  }

  async function editProduct() {
    await ProductService.editProduct(product, fields)
    .then(response => {
        if (response.status === 200) { 
            setAlertInfoMessage(t('response.ok'));
            setOpenSuccessAlert(true);             
        }
    },
        (error) => {
        const resMessage =
            (error.response && error.response.data && error.response.data.message) 
            || error.message || error.toString();
            console.error("ProductEdit: " + resMessage);
            setAlertWarningMessage(convertValidationMessage(error.response.data.message.toString()));
            setOpenWarningAlert(true);
            setShowRefresh(true);
        }
    );
  }

  useEffect(() => {
    fields.description = product.description;
    fields.price = product.price;
    fields.inStore = product.inStore;
  }, [product]);  

  return (
    <div className={classes.root}>
        <CardHeader
            avatar={
            <Avatar aria-label="productType" className={classes.avatar}>
                {product.type === 'book' ? <BookIcon/> : <MovieIcon/> }
            </Avatar>
            }
            title={t('product.details.type') + ': ' + t('product.fields.type.' + product.type)}
            subheader={t('product.details.category') + ': ' + t('product.fields.category.' + product.category)}
        />
        <CardContent>
            <Typography gutterBottom variant="h5" component="h2">
                {t('product.details.title') + ': ' + product.title}
            </Typography>
            <form noValidate onSubmit={handleSubmit(handleConfirmDialog)} className={classes.form}>
                <ProductEditFrom
                     fields={fields}
                     setFields={setFields}
                     register={register}
                     errors={errors}
                />
                <AlertApiResponseHandler
                    openWarningAlert={openWarningAlert}
                    setOpenWarningAlert={setOpenWarningAlert}
                    openSuccessAlert={openSuccessAlert}
                    setOpenSuccessAlert={setOpenSuccessAlert}
                    alertWarningMessage={alertWarningMessage}
                    alertInfoMessage={alertInfoMessage}
                />
                <AcceptButtons
                    submitButtonTitle={t('button.edit')}
                    handleClose={handleClose}
                    handleRefresh={handleRefresh}
                    showRefreshButton={showRefresh}
                    setShowRefreshButton={setShowRefresh}
                />
                <ConfirmDialog
                  openConfirmDialog={openConfirmDialog}
                  setOpenConfirmDialog={setOpenConfirmDialog}
                  handleConfirmAction={handleEditProduct}
                />
                { loading && <CircularProgress size={70} className={classes.circularProgress} />}
            </form>
        </CardContent>
    </div>
  );
}
export default ProductEdit;