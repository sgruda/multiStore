import  React, { useState } from 'react';
import { useForm } from "react-hook-form";
import { useTranslation } from 'react-i18next';

import AccountService from '../services/AccountService';
import { useFields } from '../hooks/FieldHook';
import {ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN} from '../config/config';

import AddAccountForm from '../components/accounts/AddAccountForm';
import AlertApiResponseHandler from '../components/AlertApiResponseHandler';
import AccessLevelsCheckboxForm from '../components/simple/AccessLevelCheckboxForm';

import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import CircularProgress from '@material-ui/core/CircularProgress';

import FormLabel from '@material-ui/core/FormLabel';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';

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

function AddAccount() {
  const classes = useStyles();
  const { t } = useTranslation();
  const [fields, setFields] = useFields({
    firstName: "",
    lastName: "",
    email: "",
    username: "",
    password: "",
    confirmPassword: ""
  });
  const { register, handleSubmit, errors } = useForm({mode: "onSubmit"}); 
  const [loading, setLoading] = useState(false);
  const [openWarningAlert, setOpenWarningAlert] = useState(false);
  const [alertWarningMessage, setAlertWarningMessage] = useState('');
  const [openSuccessAlert, setOpenSuccessAlert] = useState(false);
  const [alertInfoMessage, setAlertInfoMessage] = useState('');
  const [clientRole, setClientRole] = useState(false);
  const [employeeRole, setEmployeeRole] = useState(false);
  const [adminRole, setAdminRole] = useState(false);

  const convertRolesToList = () => {
    let rolesArray = [];
    if(clientRole)
      rolesArray.push(ROLE_CLIENT);
    if(employeeRole)
      rolesArray.push(ROLE_EMPLOYEE);
    if(adminRole)
      rolesArray.push(ROLE_ADMIN);
    return rolesArray;
  }

  const handleCreateAccount = () => {
    setLoading(true);
    createAccount(convertRolesToList());
  }

  const checkError = [clientRole, employeeRole, adminRole].filter((v) => v).length < 1;
  const checkErrors = () => {
    if(checkError || Object.keys(errors).length > 0)
      return true ;
    return false;
  }

  const disabledSubmit = checkErrors();

  async function createAccount(roles) {
    await AccountService.createAccount(fields, roles)
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
            console.error("AddAccount: " + resMessage);
            setAlertWarningMessage(t(error.response.data.message.toString()));
            setOpenWarningAlert(true);
        }
      );
      setLoading(false);
  }


  return (
    <div>
        <div>
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
              <Avatar className={classes.avatar}>
                <PersonAddIcon />
              </Avatar>
              <Typography component="h1" variant="h5">
                {t('pages.titles.account.add')}
              </Typography>
              <form className={classes.form} noValidate onSubmit={handleSubmit(handleCreateAccount)}>
                <AddAccountForm
                  fields={fields}
                  setFields={setFields}
                  register={register}
                  errors={errors}
                />
                <FormControl 
                  required 
                  error={checkError} 
                >
                {t("account.add.roles")}:
                <FormLabel component="legend">{t('validation.message.checkbox.roles')}</FormLabel>
                  <AccessLevelsCheckboxForm
                      clientRole={clientRole}
                      employeeRole={employeeRole}
                      adminRole={adminRole}
                      setClientRole={setClientRole}
                      setEmployeeRole={setEmployeeRole}
                      setAdminRole={setAdminRole}
                  />
                </FormControl>
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
      </div >
    </div>
  );
}

export default AddAccount;