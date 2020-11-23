import React from 'react';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';


function AccessLevelsCheckboxForm({clientRole, employeeRole, adminRole, setClientRole, setEmployeeRole, setAdminRole}) {
  return (
    <FormGroup>
        <FormControlLabel
            control={<Checkbox checked={clientRole} onChange={(event)=>setClientRole(event.target.checked)} name="client" />}
            label="Role Client"
        />
        <FormControlLabel
            control={<Checkbox checked={employeeRole} onChange={(event)=>setEmployeeRole(event.target.checked)} name="employee" />}
            label="Role Employee"
        />
        <FormControlLabel
            control={<Checkbox checked={adminRole} onChange={(event)=>setAdminRole(event.target.checked)} name="admin" />}
            label="Role Admin"
        />
    </FormGroup>
  );
}
export default AccessLevelsCheckboxForm;