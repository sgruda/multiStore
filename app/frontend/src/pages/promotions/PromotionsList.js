import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../../context/AuthContext';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import CircularProgress from '@material-ui/core/CircularProgress';
import Pagination from '@material-ui/lab/Pagination';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import Paper from '@material-ui/core/Paper';
import Collapse from '@material-ui/core/Collapse';
import Switch from '@material-ui/core/Switch';
import SyncIcon from '@material-ui/icons/Sync';
import TableContainer from '@material-ui/core/TableContainer';
import Table from '@material-ui/core/Table';
import FormControlLabel from '@material-ui/core/FormControlLabel';

import PromotionsTableBody from '../../components/promotions/table/PromotionsTableBody';
import PromotionsTableHeader from '../../components/promotions/table/PromotionsTableHeader';
import PromotionService from '../../services/PromotionService';

const useStyles = makeStyles((theme) => ({
  table: {
    minWidth: 650,
  },
  tableCellHeader: {
    fontSize: 15,
    backgroundColor: "#66bae8",
  },
  tableRow: {
    "&.Mui-selected, &.Mui-selected:hover": {
      backgroundColor: "#7cc3eb",
      "& > .MuiTableCell-root": {
      }
    }
  },
  tableCell: {
    "$hover:hover &": {
      backgroundColor: "#d3ebf8",
    }
  },
  hover: {},

  doneIcon: {
      color: "#0bb00d",
  },
  clearIcon: {
      color: "#eb1e1e"
  },
}));

function PromotionsList() {
  const classes = useStyles();
  const { t } = useTranslation();

  const [loadingData, setLoadingData] = useState(true);
  const [promotions, setPromotions] = useState([]);
  const [selectedName, setSelectedName] = useState('');
  const [page, setPage] = useState(0);
  const [totalItems, setTotalPages] = useState(0);
  const [dense, setDense] = useState(false);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  
  const {checkExpiredJWTAndExecute} = useAuth();

  const handleClick =  (name) => {
    name === selectedName ? setSelectedName('') : setSelectedName(name);
  };


  const handleChangePage = (event, newPage) => {
      setLoadingData(true);
      setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
      setLoadingData(true);
      setRowsPerPage(parseInt(event.target.value, 10));
      setPage(0);
  };

  const handleChangeDense = (event) => {
      setDense(event.target.checked);
  };

  const handleRefresh = () => {
    setSelectedName('');
    setDense(false);
    setRowsPerPage(5);
    setPage(0);
    setLoadingData(true);
  }

  const isSelected = (name) => selectedName === name ? true : false;
  const emptyRows = rowsPerPage - Math.min(rowsPerPage, totalItems - page * rowsPerPage);

  
  async function getPromotions() {
      await PromotionService.getPromotions(page, rowsPerPage)
      .then(response => {
          if (response.status === 200) { 
              const promotions = response.data.promotions.map(promotion => {
                  return {
                    id: promotion.idHash,
                    name: promotion.name,
                    discount: promotion.discount,
                    onCategory: promotion.onCategory,
                    active: promotion.active,
                    version: promotion.version,
                    signature: promotion.signature
                  };
              });
              setPromotions(promotions);
              setPage(response.data.currentPage);
              setTotalPages( response.data.totalItems);
          }
      },
          (error) => {
          const resMessage =
              (error.response && error.response.data && error.response.data.message) 
              || error.message || error.toString();
              console.error("PromotionsList: " + resMessage);
          }
      );
  }

  useEffect(() => {
      if (loadingData) {
          setLoadingData(false);
          checkExpiredJWTAndExecute(getPromotions);
      }
  }, [loadingData]);


  const headerCells = [
    { id: 'name', disablePadding: true, label: t('promotion.list.table.header.name')},
    { id: 'discount', disablePadding: false, label: t('promotion.list.table.header.discount'), numeric: true },
    { id: 'onCategory', disablePadding: false, label: t('promotion.list.table.header.onCategory') },
    { id: 'active', disablePadding: false, label: t('promotion.list.table.header.activity') },
  ];


  return (
    <div className={classes.root}>
    <Paper className={classes.paper}>
      <TableContainer>
        <Table
          className={classes.table}
          aria-labelledby="tableTitle"
          size={dense ? 'small' : 'medium'}
          aria-label="enhanced table"
        >
          <PromotionsTableHeader
            headerCells={headerCells}
            classes={classes}
          />
          <PromotionsTableBody
            promotions={promotions}
            handleClickPromotion={handleClick}
            isSelected={isSelected}
            classes={classes}
            emptyRows={emptyRows}
            dense={dense}
          />
        </Table>
      </TableContainer>
      {/* <TablePagination
        rowsPerPageOptions={[5, 10, 25]}
        component="div"
        count={totalItems}
        rowsPerPage={rowsPerPage}
        labelRowsPerPage={t('account.list.table.pagination.rows-per-page')}
        page={page}
        onChangePage={handleChangePage}
        onChangeRowsPerPage={handleChangeRowsPerPage}
      /> */}
    </Paper>
    <FormControlLabel
      control={<Switch color="primary" checked={dense} onChange={handleChangeDense} />}
      label={t('densePadding')}
    />
    <Button
      startIcon={<SyncIcon size="large" color="primary"/>}
      onClick={handleRefresh}
    >
      {t('refreshData')}
    </Button>
  </div>
  );
}
export default PromotionsList;