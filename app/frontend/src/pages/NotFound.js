import React from "react";
import { useTranslation } from 'react-i18next';
 
function NotFound() { 
  const { t } = useTranslation();
  return ( 
    <div> 
      <h3>{t('error.http.404')}</h3> 
    </div> 
  ); 
} 
export default NotFound;