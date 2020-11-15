package pl.lodz.p.it.inz.sgruda.multiStore.utils.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.redirect.server.uri}")
    private String REDIRECT_SERVER_URI;

    @Autowired
    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendMail(String to, String subject, String text, boolean isHtmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }

    @Async
    @Override
    public void sendRegistrationMail(String email , String veryficationToken) throws MessagingException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<a href=\"");
        stringBuilder.append(REDIRECT_SERVER_URI);
        stringBuilder.append("/verify-email?token=");
        stringBuilder.append(veryficationToken);
        stringBuilder.append("\">");
        stringBuilder.append("mail.account.confirm.body");
        stringBuilder.append("</a>");
        sendMail(email, "mail.account.confirm.subject", stringBuilder.toString(), true);

    }
}