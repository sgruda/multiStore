import  React, { useState } from 'react';
import { Link } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { useForm } from "react-hook-form";

import  SocialButtons from '../SocialButtons';
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL } from '../../config/config';

import Alert from '@material-ui/lab/Alert';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import { makeStyles } from '@material-ui/core/styles';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import CircularProgress from '@material-ui/core/CircularProgress';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';


function AddAccountForm({fields, setFields, register, errors}) {
  return (
    <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
        <TextField
            value={ fields.firstname }
            onChange={ setFields }
            autoComplete="fname"
            name="firstname"
            variant="outlined"
            required
            fullWidth
            id="firstname"
            label="First Name"

            inputRef={register({ required: true,  pattern: /^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+/ })}
            error={errors.firstname ? true : false}
            helperText={errors.firstname ? "Incorrect entry." : ""}
        />
        </Grid>
        <Grid item xs={12} sm={6}>
        <TextField
            value={ fields.lastname }
            onChange={ setFields }
            variant="outlined"
            required
            fullWidth
            id="lastname"
            label="Last Name"
            name="lastname"
            autoComplete="lname"

            inputRef={register({ required: true,  pattern: /^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+/ })}
            error={errors.lastname ? true : false}
            helperText={errors.lastname ? "Incorrect entry." : ""}
        />
        </Grid>
        <Grid item xs={12}>
        <TextField
            value={ fields.email }
            onChange={ setFields }
            variant="outlined"
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"

            inputRef={register({ required: true,  pattern: /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/ })}
            error={errors.email ? true : false}
            helperText={errors.email ? "Incorrect entry." : ""}
        />
        </Grid>
        <Grid item xs={12}>
        <TextField
            value={ fields.username }
            onChange={ setFields }
            variant="outlined"
            required
            fullWidth
            id="username"
            label="Username"
            name="username"
            autoComplete="username"
            
            inputRef={register({ required: true,  pattern: /[a-zA-Z0-9!@#$%^*]+/ })}
            error={errors.username ? true : false}
            helperText={errors.username ? "Incorrect entry." : ""}
        /> 
        </Grid>
        <Grid item xs={12}>
        <TextField
            value={ fields.password }
            onChange={ setFields }
            variant="outlined"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"

            inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/ })}
            error={errors.password ? true : false}
            helperText={errors.password ? "Password is required (must have 8 digits and...)" : ""}
        />
        </Grid>
        <Grid item xs={12}>
        <TextField
            value={ fields.confirmPassword }
            onChange={ setFields }
            variant="outlined"
            required
            fullWidth
            name="confirmPassword"
            label="Confirm password"
            type="password"
            id="confirmPassword"
            autoComplete="current-password"

            inputRef={register({ required: true, minLength: 8, pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/, 
                                validate: confirmPassword => confirmPassword === fields.password})}
            error={errors.confirmPassword ? true : false}
            helperText={errors.confirmPassword ? 
                        errors.confirmPassword?.type === "validate" ? "Both must be the same" : "Password is required (must have 8 digits and...)"
                        : ""}
        />
        </Grid>
    </Grid>
  );
}

export default AddAccountForm;