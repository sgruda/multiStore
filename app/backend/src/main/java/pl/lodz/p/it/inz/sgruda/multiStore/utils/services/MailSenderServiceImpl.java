package pl.lodz.p.it.inz.sgruda.multiStore.utils.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.ResourceBundle;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    private JavaMailSender javaMailSender;
    private ResourceBundle resourceBundle;

    @Value("${spring.mail.redirect.server.uri}")
    private String REDIRECT_SERVER_URI;

    @Autowired
    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.resourceBundle = new ResourceBundle();
    }

    @Async
    @Override
    public void sendPasswordResetMail(String email, String resetToken, Language language) throws MessagingException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(resourceBundle.getProperty(language.name() + ".mail.account.reset.password.body"));
        stringBuilder.append(resetToken);
        sendMail(email, resourceBundle.getProperty(language.name() + ".mail.account.reset.password.subject"), stringBuilder.toString(), false);
    }

    @Async
    @Override
    public void sendRegistrationMail(String email , String veryficationToken, Language language) throws MessagingException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<a href=\"");
        stringBuilder.append(REDIRECT_SERVER_URI);
        stringBuilder.append("/verify-email?token=");
        stringBuilder.append(veryficationToken);
        stringBuilder.append("\">");
        stringBuilder.append(resourceBundle.getProperty(language.name() + ".mail.account.confirm.body"));
        stringBuilder.append("</a>");
        sendMail(email, resourceBundle.getProperty(language.name() + ".mail.account.confirm.subject"), stringBuilder.toString(), true);

    }
    private void sendMail(String to, String subject, String text, boolean isHtmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }
}