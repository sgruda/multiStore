package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.ResetPasswordService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.services.MailSenderService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER,
        transactionManager = "mokTransactionManager"
)
public class ResetPasswordEndpoint {
    private ResetPasswordService resetPasswordService;
    private MailSenderService mailSenderService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ResetPasswordEndpoint(ResetPasswordService resetPasswordService, MailSenderService mailSenderService, PasswordEncoder passwordEncoder) {
        this.resetPasswordService = resetPasswordService;
        this.mailSenderService = mailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/api/auth/reset-password/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email) {
        try {
            String tokenToReset = resetPasswordService.resetPassword(email);
            mailSenderService.sendPasswordResetMail(email, tokenToReset);
            return ResponseEntity.ok(new ApiResponse(true, "account.reset.password.token.correctly.send"));
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
    @PutMapping("/api/auth/reset-password/change")
    public ResponseEntity<?> changePasswordAfterReset(@Valid @RequestBody ChangePasswordAfterResetRequest changePasswordAfterResetRequest) {
        try {
            resetPasswordService.changeResetPassword(changePasswordAfterResetRequest.getResetPasswordToken(),
                    passwordEncoder.encode(changePasswordAfterResetRequest.getPassword()));
            return ResponseEntity.ok(new ApiResponse(true, "account.password.correctly.resetted"));
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Getter
    private static class ChangePasswordAfterResetRequest {

        @NotNull(message = "{validation.notnull}")
        @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{validation.pattern}")
        private String password;

        @Size(min = 64, max = 64)
        @NotNull(message = "{validation.notnull}")
        @Pattern(regexp = "[0-9a-zA-Z]+", message = "{validation.pattern}")
        private String resetPasswordToken;
    }
}
