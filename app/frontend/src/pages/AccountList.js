import React, {useState, useEffect} from "react";
import { Button } from '@material-ui/core';
import { DataGrid } from '@material-ui/data-grid';
import { useAuth } from "../context/AuthContext";
import { useFields } from '../hooks/FieldHook';

import ClearIcon from '@material-ui/icons/Clear';
import DoneIcon from '@material-ui/icons/Done';

import AccountService from '../services/AccountService';


    const columns = [
        { field: 'idHash', hide: true, },
        { field: 'firstName', headerName: 'First name', width: 130 },
        { field: 'lastName', headerName: 'Last name', width: 130 },
        { field: 'email', headerName: 'Email', width: 200, },
        { field: 'active', headerName: 'Active', width: 90,},
        { field: 'roles', headerName: 'Roles', width: 300, },
        // { field: 'fullName', headerName: 'Full name', sortable: false, width: 160, },
        // description: 'This column has a value getter and is not sortable.', 
        // valueGetter: (params) =>
        //     `${params.getValue('firstName') || ''} ${params.getValue('lastName') || ''}`,
        // },
     ];
  
    // const accounts = [
    //     { id: 1, lastName: 'Snow', firstName: 'Jon', email: 'jon.snow@gmail.com', roles: ["CLIENT", "EMPLOYEE", "ADMIN"], active: true }
    // ]

function AccountList() {
    const [loadingData, setLoadingData] = useState(true);
    const [accounts, setAccounts] = useState([]);
    const [page, setPage] = React.useState(1);
    const paginationInfo = [{
        currentPage: 0,
        totalItems: 0,
        totalPages: 0
    }];
    const requestParams = [{
        textToSearch: null,
        page: 0,
        size: 5,
        sort: [],
        active: null
    }];

    const handlePageChange = (params) => {
        setPage(params.page);
    };

    async function getAccounts() {
        // setLoadingData(true);
        await AccountService.getAccounts()
        .then(response => {
            if (response.status === 200) { 
                const accounts = response.data.accounts.map(account => {
                    return {
                      id: account.idHash,
                      firstName: account.firstName,
                      lastName: account.lastName,
                      email: account.email,
                      active: account.active,
                      roles: account.roles
                    };
                });
                setAccounts(accounts);
                paginationInfo.currentPage = response.data.currentPage;
                paginationInfo.totalItems = response.data.totalItems;
                paginationInfo.totalPages = response.data.totalPages;
                // setLoadingData(false);
                console.log("loading " +loadingData)
                console.log("data " + response.data.totalItems)
            }
        },
            (error) => {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) 
                || error.message || error.toString();
            }
        );
    }

    useEffect(() => {
        console.log("loading " +loadingData)
        if (loadingData) {
            setLoadingData(false);
            getAccounts();
        }
    }, [page, accounts]);

  return (
    <div style={{ height: 400, width: '90%' }}>
      <DataGrid rows={accounts} 
                columns={columns}  
                pagination
                pageSize={requestParams.size} 
                rowsPerPageOptions={[5,10,100]}
                paginationMode='server'
                sortingMode='server'
                onPageChange={handlePageChange}
                loading={loadingData}
                rowCount={paginationInfo.totalItems}
        />
    </div>
  );
}

export default AccountList;