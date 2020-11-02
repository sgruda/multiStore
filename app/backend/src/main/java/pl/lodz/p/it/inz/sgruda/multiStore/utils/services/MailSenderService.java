package pl.lodz.p.it.inz.sgruda.multiStore.utils.services;

import javax.mail.MessagingException;

public interface MailSenderService {
    void sendMail(String to, String subject, String text, boolean isHtmlContent) throws MessagingException;
}
