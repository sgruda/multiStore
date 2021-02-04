package pl.lodz.p.it.inz.sgruda.multiStore.utils;

public class ResourceBundle {
    private final String PL_RESET_PASSWORD_BODY = "Oto twoj token do resetu hasla w systemie.";
    private final String EN_RESET_PASSWORD_BODY = "That is password reset token.";
    private final String PL_RESET_PASSWORD_SUBJECT = "Reset hasła.";
    private final String EN_RESET_PASSWORD_SUBJECT = "Password reset.";

    private final String PL_CONFIRM_ACCOUNT_BODY = "Twoj adres e-mail zostal uzyty, podczas rejestracji w systemie, aby potwierdzic, ze to twoj mail, kliknij w link.";
    private final String EN_CONFIRM_ACCOUNT_BODY = "Your address e-mail has been used, during registration in system, to confirm that is your e-mail. Click link.";
    private final String PL_CONFIRM_ACCOUNT_SUBJECT = "Rejestracja konta w systemie.";
    private final String EN_CONFIRM_ACCOUNT_SUBJECT = "Account registration in system.";

    private final String PL_SENDING_DATE = "Czas wyslania wiadomosci:";
    private final String EN_SENDING_DATE = "Time of sent message:";

    public String getProperty(String key) {
        switch(key) {
            case "pl.reset.password.body": return PL_RESET_PASSWORD_BODY;
            case "en.reset.password.body": return EN_RESET_PASSWORD_BODY;
            case "pl.reset.password.subject": return PL_RESET_PASSWORD_SUBJECT;
            case "en.reset.password.subject": return EN_RESET_PASSWORD_SUBJECT;

            case "pl.confirm.account.body": return PL_CONFIRM_ACCOUNT_BODY;
            case "en.confirm.account.body": return EN_CONFIRM_ACCOUNT_BODY;
            case "pl.confirm.account.subject": return PL_CONFIRM_ACCOUNT_SUBJECT;
            case "en.confirm.account.subject": return EN_CONFIRM_ACCOUNT_SUBJECT;

            case "pl.sending.date": return PL_SENDING_DATE;
            case "en.sending.date": return EN_SENDING_DATE;

            default: return "Lack of translation";
        }
    }
}
