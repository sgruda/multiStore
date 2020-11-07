import React from "react";


import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';

function AccountsTableHeader({headerCells, classes, order, orderBy, onRequestSort, fieldToIgnoreSorting}) {
    const createSortHandler = (property) => (event) => {
       onRequestSort(event, property);
    };

  return (
    <TableHead>
      <TableRow>
        {headerCells.map((headCell) => (
          <TableCell className={classes.tableCellHeader}
            key={headCell.id}
            align="center"
          >
            <TableSortLabel 
              active={orderBy === headCell.id && headCell.id !== fieldToIgnoreSorting}
              direction={orderBy === headCell.id ? order : 'asc'}
              hideSortIcon={headCell.id === fieldToIgnoreSorting}
              onClick={createSortHandler(headCell.id)}
            >
            <Typography>
              <Box fontWeight="fontWeightBold" m={1}>
                 {headCell.label}
              </Box>
            </Typography>
            </TableSortLabel>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}
export default AccountsTableHeader;