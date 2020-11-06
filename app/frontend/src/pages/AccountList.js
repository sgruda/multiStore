import React, {useState, useEffect} from "react";
import { Button } from '@material-ui/core';
import { DataGrid } from '@material-ui/data-grid';
import { useAuth } from "../context/AuthContext";
import { useFields } from '../hooks/FieldHook';


import AccountService from '../services/AccountService';


    const columns = [
        { field: 'idHash', headerName: 'ID', width: 70 },
        // { field: 'idHash', hide: true, },
        { field: 'firstName', headerName: 'First name', width: 130 },
        { field: 'lastName', headerName: 'Last name', width: 130 },
        { field: 'email', headerName: 'Email', type: 'email', width: 200, },
        { field: 'active', headerName: 'Active', type: 'boolean', width: 80,},
        { field: 'roles', headerName: 'Roles', width: 300, },
        // { field: 'fullName', headerName: 'Full name', sortable: false, width: 160,
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
    const paginationInfo = [{
        currentPage: 0,
        totalItems: 0,
        totalPages: 0
    }];

    async function getAccounts() {
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
                // setAccounts(response.data.accounts);
                paginationInfo.currentPage = response.data.currentPage;
                paginationInfo.totalItems = response.data.totalItems;
                paginationInfo.totalPages = response.data.totalPages;
                setLoadingData(false)
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
        if (loadingData) {
            getAccounts();
        }
    }, []);

  return (
    <div style={{ height: 400, width: '100%' }}>
      <DataGrid rows={accounts} columns={columns} key="idHash" pageSize={5} checkboxSelection />
    </div>
    // <div><Button onClick={getAccounts}> reafsgx</Button></div>
  );
}

export default AccountList;