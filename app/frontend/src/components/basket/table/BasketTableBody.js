import React from "react";
import { useTranslation } from 'react-i18next';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';


function BasketTableBody({orderedItems, handleClickItem, isSelected, classes, totalPrice}) {
    const { t } = useTranslation();

    return (
        <TableBody>
            {orderedItems.map((orderedItem) => {
                const isItemSelected = isSelected(orderedItem.id);
              return (
                <TableRow
                  hover
                  tabIndex={-1}
                  key={orderedItem.id}
                  classes={{ hover: classes.hover }}
                  className={classes.tableRow}
                  onClick={() => handleClickItem(orderedItem)}
                  selected={isItemSelected}
                >
                    <TableCell align="center" className={classes.tableCell}>{orderedItem.orderedProduct.title}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{t('product.fields.type.' + orderedItem.orderedProduct.type)}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{t('product.fields.category.' + orderedItem.orderedProduct.category)}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{orderedItem.orderedProduct.price}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{orderedItem.orderedNumber}</TableCell>
                </TableRow>
                );
            })}
                    <TableRow>
                        <TableCell colSpan={4} align="right">{t('order.total-price') + ": " + totalPrice + " PLN"}</TableCell>
                    </TableRow>
          </TableBody>
    );
}
export default BasketTableBody;