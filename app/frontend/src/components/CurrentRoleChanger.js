import React from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import InputLabel from '@material-ui/core/InputLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import NativeSelect from '@material-ui/core/NativeSelect';

import AuthenticationService from '../services/AuthenticationService'
import { Container } from '@material-ui/core';

import {ACTIVE_ROLE} from '../config/config';


const useStyles = makeStyles((theme) => ({
  root: {
    // minWidth: 275,
    flexGrow: 1,
  },
  title: {
    fontSize: 14,
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
  buttons: {
    marginLeft: theme.spacing(6),
  },
}));

function CurrentRoleChanger({currentActiveRole, setCurrentActiveRole, handleClosePopper}) {
  const classes = useStyles();
  const { t } = useTranslation();
  const userRoles = AuthenticationService.getParsedJWT().roles;
  const options = userRoles.map((role) =>  
    <option value={role}>{t('account.access-level.names.' + role.split("_")[1].toLowerCase())}</option>
  );

  const setActiveRole = (role) => {
    setCurrentActiveRole(role);
    localStorage.setItem(ACTIVE_ROLE, role); 
  }

  return (
    <Card className={classes.root} variant="outlined" justify="center">
      <CardContent>
        <Typography className={classes.title} color="textSecondary" gutterBottom>
          {t('pages.titles.account.access-level.choose')}
        </Typography>
        <FormControl className={classes.formControl}>
        <InputLabel>{t('account.access-level.choose.role')}</InputLabel>
        <NativeSelect
          value={currentActiveRole}
          onChange={(event) => {setActiveRole(event.target.value)}}
        >
          {options}
        </NativeSelect>
        <FormHelperText>{t('account.access-level.choose.current')}: {t('account.access-level.names.' + currentActiveRole.split("_")[1].toLowerCase())}</FormHelperText>
      </FormControl>
      </CardContent>
      <CardActions>
        <Container justify="center" className={classes.buttons}>
           <Button onClick={(event) => handleClosePopper(event)} size="small">{t('button.close')}</Button>
        </Container>
      </CardActions>
    </Card>
  );
}
export default CurrentRoleChanger;