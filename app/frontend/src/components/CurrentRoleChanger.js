import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import InputLabel from '@material-ui/core/InputLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import NativeSelect from '@material-ui/core/NativeSelect';

import AuthenticationService from '../services/AuthenticationService'

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
}));

function CurrentRoleChanger({currentActiveRole, setCurrentActiveRole}) {
  const classes = useStyles();
  const userRoles = AuthenticationService.getParsedJWT().roles;
  const options = userRoles.map((role) =>  
    <option value={role}>{role}</option>
  );

  return (
    <Card className={classes.root} variant="outlined">
      <CardContent>
        <Typography className={classes.title} color="textSecondary" gutterBottom>
          Choose role
        </Typography>
        <FormControl className={classes.formControl}>
        <InputLabel>Role</InputLabel>
        <NativeSelect
          value={currentActiveRole}
          onChange={(event) => {setCurrentActiveRole(event.target.value)}}
        >
          {options}
        </NativeSelect>
        <FormHelperText>Your current role: {currentActiveRole}</FormHelperText>
      </FormControl>
      </CardContent>
      {/* <CardActions>
        <Button size="small">Accept</Button>
      </CardActions> */}
    </Card>
  );
}
export default CurrentRoleChanger;