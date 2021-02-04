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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        stringBuilder.append(resourceBundle.getProperty(language.name() + ".reset.password.body"));
        stringBuilder.append(": ");
        stringBuilder.append(resetToken);
        stringBuilder.append("\n");
        stringBuilder.append(this.getSendingDateMessage(language));
        sendMail(email, resourceBundle.getProperty(language.name() + ".reset.password.subject"), stringBuilder.toString(), false);
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
        stringBuilder.append(resourceBundle.getProperty(language.name() + ".confirm.account.body"));
        stringBuilder.append("</a>");
        stringBuilder.append("\n");
        stringBuilder.append(this.getSendingDateMessage(language));
        sendMail(email, resourceBundle.getProperty(language.name() + ".confirm.account.subject"), stringBuilder.toString(), true);

    }
    private void sendMail(String to, String subject, String text, boolean isHtmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }
    private String getSendingDateMessage(Language language) {
        String format = language.equals(Language.pl) ? "HH:mm:ss dd-MM-yyyy " : "MM-dd-yyyy h:m:s a";
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
        return resourceBundle.getProperty(language.name() + ".sending.date") + " " + date;
    }
}