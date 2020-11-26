import React from 'react';
import { useTranslation } from 'react-i18next';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';


function AccessLevelsCheckboxForm({clientRole, employeeRole, adminRole, setClientRole, setEmployeeRole, setAdminRole}) {
  const { t } = useTranslation();
  return (
    <FormGroup>
        <FormControlLabel
            control={<Checkbox checked={clientRole} onChange={(event)=>setClientRole(event.target.checked)} name="client" />}
            label={t("account.access-level.names.client")}
        />
        <FormControlLabel
            control={<Checkbox checked={employeeRole} onChange={(event)=>setEmployeeRole(event.target.checked)} name="employee" />}
            label={t("account.access-level.names.employee")}
        />
        <FormControlLabel
            control={<Checkbox checked={adminRole} onChange={(event)=>setAdminRole(event.target.checked)} name="admin" />}
            label={t("account.access-level.names.admin")}
        />
    </FormGroup>
  );
}
export default AccessLevelsCheckboxForm;