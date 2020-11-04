package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Log
@Component
public class AccountInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalDateTime interceptionTime = LocalDateTime.now();
        StringBuilder messageBuilder = new StringBuilder("Intercepted method invocation: ");
        messageBuilder.append(" Method type: " + request.getMethod());
        messageBuilder.append(" Request endpoint: " + request.getRequestURI());
        messageBuilder.append(" Interception time: ").append(interceptionTime);
        messageBuilder.append(" User: ").append(request.getRemoteUser() != null ? request.getRemoteUser() : "null ");
        messageBuilder.append(" With parameters: ");

        if (request.getQueryString() != null) {
            messageBuilder.append(
                    Arrays.stream(
                            request.getQueryString()
                            .split("&"))
                            .collect(Collectors.joining(",")));
        }
        else {
            messageBuilder.append("null ");
        }
        messageBuilder.append(" Ended with status: " + response.getStatus());
        log.info(messageBuilder.toString());

    }
}
