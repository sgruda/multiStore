import React from 'react';
import { useTranslation } from 'react-i18next';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import { InputLabel } from '@material-ui/core';
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import InputAdornment from '@material-ui/core/InputAdornment';

function AddPromotionForm({fields, setFields, register, errors}) {
  const { t } = useTranslation();

  return (
    <Grid container xs={12} spacing={3}>
        <Grid item xs={12}>
            <TextField
                value={ fields.name }
                onChange={ setFields }
                variant="outlined"
                required
                fullWidth
                id="name"
                label={t('promotion.create.form.name')}
                name="name"
                autoComplete="name"

                inputRef={register({ required: true,  pattern: /[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]+/ })}
                error={errors.name ? true : false}
                helperText={errors.name ? t('validation.message.incorrect.entry') : ""}
            />
        </Grid>
        <Grid item xs={6}>
            <TextField
                value={ fields.discount }
                onChange={ setFields }
                variant="outlined"
                type="number"
                required
                fullWidth
                id="discount"
                label={t('promotion.create.form.discount')}
                name="discount"
                autoComplete="discount"
                InputProps={{
                    endAdornment: <InputAdornment>%</InputAdornment>
                }}

                inputRef={register({ required: true,  pattern: /[0-9.]+/ })}
                error={errors.discount ? true : false}
                helperText={errors.discount ? t('validation.message.incorrect.entry') : ""}
            /> 
        </Grid>
        <Grid item xs={6}>
            <FormControl style={{minWidth: '180px'}} variant="outlined" required> 
                <InputLabel >{t('promotion.create.form.onCategory')}</InputLabel>
                <Select
                    label={t('promotion.create.form.onCategory')}
                    value={ fields.onCategory }
                    onChange={(event) => {
                        event.target.id = "onCategory";
                        setFields(event);
                    }}
                    fullWidth
                >
                    <MenuItem value={'action'}>{t("product.fields.category.action")}</MenuItem>
                    <MenuItem value={'adventure'}>{t("product.fields.category.adventure")}</MenuItem>
                    <MenuItem value={'detective'}>{t("product.fields.category.detective")}</MenuItem>
                    <MenuItem value={'document'}>{t("product.fields.category.document")}</MenuItem>
                    <MenuItem value={'fantasy'}>{t("product.fields.category.fantasy")}</MenuItem>
                    <MenuItem value={'fiction'}>{t("product.fields.category.fiction")}</MenuItem>
                    <MenuItem value={'history'}>{t("product.fields.category.history")}</MenuItem>
                    <MenuItem value={'novel'}>{t("product.fields.category.novel")}</MenuItem>
                    <MenuItem value={'science'}>{t("product.fields.category.science")}</MenuItem>
                </Select>
            </FormControl>
        </Grid>
    </Grid>
  );
}
export default AddPromotionForm;