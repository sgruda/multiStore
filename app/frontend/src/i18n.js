import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
 
import Backend from 'i18next-xhr-backend';
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
  .use(Backend)
  .use(initReactI18next)
  .use(LanguageDetector)
  .init({
    debug: true,
 
    fallbackLng: 'en',
    whitelist: ['en', 'pl'],
 
    // interpolation: {
    //   escapeValue: false, // not needed for react as it escapes by default
    // },

    backend: {
        loadPath: '/locales/{{lng}}/{{ns}}.json',
    },
  });
 
export default i18n;