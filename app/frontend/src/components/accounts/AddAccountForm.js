import  React from 'react';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';



function AddAccountForm({fields, setFields, register, errors}) {
  return (
    <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
        <TextField
            value={ fields.firstName }
            onChange={ setFields }
            autoComplete="fname"
            name="firstName"
            variant="outlined"
            required
            fullWidth
            id="firstName"
            label="First Name"

            inputRef={register({ required: true,  pattern: /^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+/ })}
            error={errors.firstName ? true : false}
            helperText={errors.firstName ? "Incorrect entry." : ""}
        />
        </Grid>
        <Grid item xs={12} sm={6}>
        <TextField
            value={ fields.lastName }
            onChange={ setFields }
            variant="outlined"
            required
            fullWidth
            id="lastName"
            label="Last Name"
            name="lastName"
            autoComplete="lname"

            inputRef={register({ required: true,  pattern: /^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+/ })}
            error={errors.lastName ? true : false}
            helperText={errors.lastName ? "Incorrect entry." : ""}
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