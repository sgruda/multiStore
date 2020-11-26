package pl.lodz.p.it.inz.sgruda.multiStore.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private @Autowired ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        ApiResponse response = new ApiResponse(false, getMessageCode(e.getMessage()));
        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getOutputStream()
                .println(mapper.writeValueAsString(response));
    }
    private String getMessageCode(String message) {
        if(message.equals("Bad credentials"))
            return "error.auth.credentials";
        if(message.equals("User account is locked"))
            return "error.auth.locked";
        if(message.equals("User is disabled"))
            return "error.auth.disabled";
        return "error.default";
    }
}
