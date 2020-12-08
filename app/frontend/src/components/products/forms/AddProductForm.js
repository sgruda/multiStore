import  React from 'react';
import { useTranslation } from 'react-i18next';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import FormControl from '@material-ui/core/FormControl';
import NativeSelect from '@material-ui/core/NativeSelect';
import { InputLabel } from '@material-ui/core';


function AddProductForm({fields, setFields, register, errors}) {
    const { t } = useTranslation(); 
  return (
    <Grid container spacing={2}>
        <Grid item xs={12}>
        <TextField
            value={ fields.title }
            onChange={ setFields }
            autoComplete="title"
            name="title"
            variant="outlined"
            required
            fullWidth
            id="title"
            label={t('product.create.form.title')}

            inputRef={register({ required: true,  pattern: /[0-9a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ?!:., ]+/ })}
            error={errors.title ? true : false}
            helperText={errors.title ? t('validation.message.incorrect.entry') : ""}
        />
        </Grid>
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
            label={t('product.create.form.description')}
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
            label={t('product.create.form.inStore')}
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
            label={t('product.create.form.price')}
            name="price"
            autoComplete="price"
            
            inputRef={register({ required: true,  pattern: /[0-9.]+/ })}
            error={errors.price ? true : false}
            helperText={errors.price ? t('validation.message.incorrect.entry') : ""}
        /> 
        </Grid>
        <Grid item xs={6}>
            <FormControl style={{minWidth: '180px'}}>
                <InputLabel>{t('product.create.form.type.label')}</InputLabel>
                <NativeSelect
                    value={ fields.type }
                    onChange={ setFields }
                    id="type"
                    required
                    fullWidth
                >
                    <option value={'book'}>{t("product.create.form.type.book")}</option>
                    <option value={'movie'}>{t("product.create.form.type.movie")}</option>
                </NativeSelect>
            </FormControl>
        </Grid>
        <Grid item xs={6}>
            <FormControl style={{minWidth: '180px'}} variant="outlined">
                <InputLabel>{t('product.create.form.category')}</InputLabel>
                <NativeSelect
                    value={ fields.category }
                    onChange={setFields}
                    id="category"
                    required
                    fullWidth
                >
                    <option value={'action'}>{t("product.fields.category.action")}</option>
                    <option value={'adventure'}>{t("product.fields.category.adventure")}</option>
                    <option value={'detective'}>{t("product.fields.category.detective")}</option>
                    <option value={'document'}>{t("product.fields.category.document")}</option>
                    <option value={'fantasy'}>{t("product.fields.category.fantasy")}</option>
                    <option value={'fiction'}>{t("product.fields.category.fiction")}</option>
                    <option value={'history'}>{t("product.fields.category.history")}</option>
                    <option value={'novel'}>{t("product.fields.category.novel")}</option>
                    <option value={'science'}>{t("product.fields.category.science")}</option>

                </NativeSelect>
            </FormControl>
        </Grid>
    </Grid>
  );
}

export default AddProductForm;