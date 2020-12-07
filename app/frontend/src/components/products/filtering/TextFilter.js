import React from 'react';
import { useTranslation } from 'react-i18next';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';


function TextFilter({fields, setFields, register, errors}) {
  const { t } = useTranslation();

  return (
    <Grid item xs={12} >
        <TextField
        value={ fields.textToSearch }
        onChange={ setFields }
        name="textToSearch"
        fullWidth
        id="textToSearch"
        label={t('product.list.filter.search')}

        inputRef={register({ pattern: /[a-zA-Z0-9!@#$%^* ]+/ })}
        error={errors.textToSearch ? true : false}
        helperText={errors.textToSearch ? t('validation.message.incorrect.entry') : ""}
        />
    </Grid>
  );
}
export default TextFilter;