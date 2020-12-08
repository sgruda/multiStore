
import React from "react";
import { useTranslation } from 'react-i18next';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';

function PromotionsTableBody({promotions, handleClickPromotion, isSelected, classes, emptyRows, dense}) {
    const { t } = useTranslation();
    return (
        <TableBody>
            {promotions.map((promotion) => {
              const isItemSelected = isSelected(promotion);
              return (
                <TableRow
                  hover
                  onClick={() => handleClickPromotion(promotion)}
                  aria-checked={isItemSelected}
                  tabIndex={-1}
                  key={promotion.id}
                  selected={isItemSelected}
                  classes={{ hover: classes.hover }}
                  className={classes.tableRow}
                >
                    <TableCell align="center" className={classes.tableCell}>
                        {promotion.name}
                    </TableCell>
                    <TableCell align="center" className={classes.tableCell}>{promotion.discount}%</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{t('product.fields.category.' + promotion.onCategory)}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>
                        {promotion.active 
                        ? <DoneIcon className={classes.doneIcon}/> 
                        : <ClearIcon className={classes.clearIcon}/> }
                        </TableCell>
                </TableRow>
                );
            })}
            {emptyRows > 0 && (
              <TableRow style={{ height: (dense ? 33 : 53) * emptyRows }}>
                <TableCell colSpan={6} />
              </TableRow>
            )}
          </TableBody>
    );
}
export default PromotionsTableBody;