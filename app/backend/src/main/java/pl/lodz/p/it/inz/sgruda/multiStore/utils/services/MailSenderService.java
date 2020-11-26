package pl.lodz.p.it.inz.sgruda.multiStore.utils.services;

import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;

import javax.mail.MessagingException;

public interface MailSenderService {
    void sendRegistrationMail(String email, String veryficationToken, Language language) throws MessagingException;
    void sendPasswordResetMail(String email, String resetToken, Language language) throws MessagingException;
}
