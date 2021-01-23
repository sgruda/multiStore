package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Log
@Component
public class TransactionRetryingInterceptor {
    @Bean
    public List<RetryListener> retryListeners() {
        return Collections.singletonList(new RetryListenerSupport() {
            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.info("Retryable method " +  context.getAttribute("context.name") +
                                " threw " +  context.getRetryCount() + "th exception " + throwable.toString());
            }
        });
    }
}
