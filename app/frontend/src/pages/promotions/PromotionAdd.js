import  React, { useState, useEffect } from 'react';
import { useForm } from "react-hook-form";
import { useTranslation } from 'react-i18next';

import AccountService from '../../services/AccountService';
import AuthenticationService from '../../services/AuthenticationService';
import { useFields } from '../../hooks/FieldHook';
import {ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN} from '../../config/config';

import AddAccountForm from '../../components/accounts/AddAccountForm';
import AlertApiResponseHandler from '../../components/AlertApiResponseHandler';
import AccessLevelsCheckboxForm from '../../components/simple/AccessLevelCheckboxForm';
import RouterRedirectTo from '../../components/simple/RouterRedirectTo';

import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import CircularProgress from '@material-ui/core/CircularProgress';

import AddProductForm from '../../components/products/AddProductForm';
import ProductService from '../../services/ProductService';

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(3),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.primary.main,
  },
  socialButtons: {
    padding: 10,
    width: '47%'
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(2),
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

function PromotionAdd() {
  const classes = useStyles();
  const { t } = useTranslation();
  const [fields, setFields] = useFields({
    title: "",
    description: "",
    inStore: 1.0,
    price: 1.0,
    type: 'book',
    category: "action"
  });
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [loading, setLoading] = useState(false);
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');

  const [jwtExpiration, setJwtExpiration] = useState(false);


  const handleCreateProduct = () => {
    setLoading(true);
    createProduct();
  }

  const checkErrors = () => {
    if(Object.keys(errors).length > 0)
      return true ;
    return false;
  }

  const disabledSubmit = checkErrors();

  const convertValidationMessage = (message) => {
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

  async function createProduct() {
    await ProductService.createProduct(fields)
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
            console.error("AddProduct: " + resMessage);
            setAlertWarningMessage(convertValidationMessage(error.response.data.message.toString()));
            setOpenWarningAlert(true);
        }
      );
      setLoading(false);
  }
  useEffect(() => {
    if (loading) {
        setLoading(false);
        if(AuthenticationService.jwtIsExpired()) {
          setJwtExpiration(true);
        }
    }
  }, [loading]);

  return (
    <div>
        {/* <div>
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
              <Avatar className={classes.avatar}>
                <PersonAddIcon />
              </Avatar>
              <Typography component="h1" variant="h5">
                {t('pages.titles.product.create')}
              </Typography>
              <form className={classes.form} noValidate onSubmit={handleSubmit(handleCreateProduct)}>
                <AddProductForm
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
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  color="primary"
                  className={classes.submit}
                  disabled={disabledSubmit}
                >
                  {t('button.add.default')}
                </Button>
                { loading && <CircularProgress size={70} className={classes.circularProgress} />}
              </form>
            </div>
          </Container>
       </div > */}
      {jwtExpiration ? 
        <RouterRedirectTo 
          dialogContent={t('dialog.content.jwt-expired')}
          page="/signin"
        />
    :<></>}
    </div>
  );
}

export default PromotionAdd;