package pl.lodz.p.it.inz.sgruda.multiStore.utils.services;

import javax.mail.MessagingException;

public interface MailSenderService {
    void sendRegistrationMail(String email, String veryficationToken) throws MessagingException;
    void sendPasswordResetMail(String email, String resetToken) throws MessagingException;
}
