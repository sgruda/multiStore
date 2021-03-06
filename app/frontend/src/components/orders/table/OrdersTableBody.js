
import React from "react";
import { useTranslation } from 'react-i18next';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';

function OrdersTableBody({orders, handleClickOrder, isSelected, classes, emptyRows, dense}) {
  const { t } = useTranslation();

  const handleOrderedTitles = (order) => {
    let orderedTitles = '';
    order.orderedItemDTOS.map(item => {
      orderedTitles += item.orderedProduct.title + ', ';
    })
    return orderedTitles.slice(0, orderedTitles.length - 2);
  }
    return (
        <TableBody>
            {orders.map((order) => {
              const isItemSelected = isSelected(order.id);
              const orderedTitles = handleOrderedTitles(order);
              return (
                <TableRow
                  hover
                  onClick={() => handleClickOrder(order)}
                  aria-checked={isItemSelected}
                  tabIndex={-1}
                  key={order.id}
                  selected={isItemSelected}
                  classes={{ hover: classes.hover }}
                  className={classes.tableRow}
                >
                    <TableCell align="center" className={classes.tableCell}>
                        {order.identifier.slice(0, 8) + '...'}
                    </TableCell>
                    <TableCell align="center" className={classes.tableCell}>
                      {
                        new Intl.DateTimeFormat(t('language'), {
                          year: 'numeric', month: 'numeric', day: 'numeric',
                          hour: 'numeric', minute: 'numeric',
                          hour12: t('language') === "en-US" ? true : false,
                        }).format(new Date(order.orderDate))
                      }
                    </TableCell>
                    <TableCell align="center" className={classes.tableCell}>{order.buyerEmail}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{orderedTitles}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{order.totalPrice}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{order.address}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{t('order.status.' + order.status)}</TableCell>
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
export default OrdersTableBody;