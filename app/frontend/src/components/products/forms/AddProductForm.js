import  React from 'react';
import { useTranslation } from 'react-i18next';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Select from "@material-ui/core/Select";
import NativeSelect from '@material-ui/core/NativeSelect';
import { InputLabel } from '@material-ui/core';
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from '@material-ui/core/FormControl';


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
            <FormControl style={{minWidth: '190px'}} variant="outlined" required> 
                <InputLabel>{t('product.create.form.type.label')}</InputLabel>
                <Select
                    label={t('product.create.form.type.label')}
                    value={ fields.type }
                    onChange={(event) => {
                        event.target.id = "type";
                        setFields(event);
                    }}
                    id="type"
                    required
                    fullWidth
                >
                    <MenuItem value={'book'}>{t("product.create.form.type.book")}</MenuItem>
                    <MenuItem value={'movie'}>{t("product.create.form.type.movie")}</MenuItem>
                </Select>
            </FormControl>
        </Grid>
        <Grid item xs={6}>
            <FormControl style={{minWidth: '190px'}} variant="outlined" required> 
                <InputLabel >{t('product.create.form.category')}</InputLabel>
                <Select
                    label={t('product.create.form.category')}
                    value={ fields.category }
                    onChange={(event) => {
                        event.target.id = "category";
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

export default AddProductForm;