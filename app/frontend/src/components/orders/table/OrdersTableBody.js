
import React from "react";
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';

function OrdersTableBody({orders, handleClickOrder, isSelected, classes, emptyRows, dense}) {
  const handleOrderedTitles = (order) => {
    let orderedTitles = '';
    order.orderedItemDTOS.map(item => {
      orderedTitles += item.orderedProduct.title + ', ';
    })
    return orderedTitles.slice(0, orderedTitles.length);
  }
    return (
        <TableBody>
            {orders.map((order) => {
              const isItemSelected = isSelected(order.id);
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
                        {order.identifier}
                    </TableCell>
                    <TableCell align="center" className={classes.tableCell}>{order.orderDate}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{order.buyerEmail}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{handleOrderedTitles}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{order.totalPrice}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{order.address}</TableCell>
                    <TableCell align="center" className={classes.tableCell}>{order.status}</TableCell>
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