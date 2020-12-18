import React from "react";

import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';

function PromotionsTableHeader({headerCells, classes}) {
  return (
    <TableHead>
      <TableRow>
        {headerCells.map((headCell) => (
          <TableCell className={classes.tableCellHeader}
            key={headCell.id}
            align="center"
          >
            <Typography>
              <Box fontWeight="fontWeightBold" m={1}>
                 {headCell.label}
              </Box>
            </Typography>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}
export default PromotionsTableHeader;