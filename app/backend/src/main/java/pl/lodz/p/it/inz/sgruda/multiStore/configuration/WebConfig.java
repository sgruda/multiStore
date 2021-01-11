package pl.lodz.p.it.inz.sgruda.multiStore.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.AccountInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;
    private final String FRONTEND_URL = "https://frontend-7lhoj5kiqa-ey.a.run.app";
    private @Autowired AccountInterceptor accountInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(FRONTEND_URL)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountInterceptor);
    }
}