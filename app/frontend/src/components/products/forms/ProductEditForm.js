import React from 'react';
import { useTranslation } from 'react-i18next';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';


function ProductEditForm({fields, setFields, register, errors}) {
  const { t } = useTranslation();

  return (
    <Grid container xs={12} spacing={2}>
        <Grid item xs={12}>
            <TextField
                value={ fields.description }
                onChange={ setFields }
                variant="outlined"
                required
                fullWidth
                multiline
                rows={10}
                id="description"
                label={t('product.edit.form.description')}
                name="description"
                autoComplete="description"

                inputRef={register({ required: true,  pattern: /[0-9a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ?!:., ]+/ })}
                error={errors.description ? true : false}
                helperText={errors.description ? t('validation.message.incorrect.entry') : ""}
            />
        </Grid>
        <Grid item xs={6}>
            <TextField
                value={ fields.inStore }
                onChange={ setFields }
                variant="outlined"
                type="number"
                required
                fullWidth
                id="inStore"
                label={t('product.edit.form.inStore')}
                name="inStore"
                autoComplete="inStore"

                inputRef={register({ required: true,  pattern: /[0-9.]+/ })}
                error={errors.inStore ? true : false}
                helperText={errors.inStore ? t('validation.message.incorrect.entry') : ""}
            />
        </Grid>
        <Grid item xs={6}>
            <TextField
                value={ fields.price }
                onChange={ setFields }
                variant="outlined"
                type="number"
                required
                fullWidth
                id="price"
                label={t('product.edit.form.price')}
                name="price"
                autoComplete="price"
                
                inputRef={register({ required: true,  pattern: /[0-9.]+/ })}
                error={errors.price ? true : false}
                helperText={errors.price ? t('validation.message.incorrect.entry') : ""}
            /> 
        </Grid>
    </Grid>
  );
}
export default ProductEditForm;