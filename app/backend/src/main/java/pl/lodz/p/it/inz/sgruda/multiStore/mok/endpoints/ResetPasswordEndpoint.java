package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.ResetPasswordService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.services.MailSenderService;

import javax.mail.MessagingException;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
public class ResetPasswordEndpoint {
    private ResetPasswordService resetPasswordService;
    private MailSenderService mailSenderService;

    @Autowired
    public ResetPasswordEndpoint(ResetPasswordService resetPasswordService, MailSenderService mailSenderService) {
        this.resetPasswordService = resetPasswordService;
        this.mailSenderService = mailSenderService;
    }

    @PutMapping("/api/auth/reset-password/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email) {
        try {
            String tokenToReset = resetPasswordService.resetPassword(email);
            mailSenderService.sendPasswordResetMail(email, tokenToReset);
            return ResponseEntity.ok(new ApiResponse(true, "account.password.correctly.resetted"));
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (MessagingException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, "error.email.not.send"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
