package pl.lodz.p.it.inz.sgruda.multiStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.lodz.p.it.inz.sgruda.multiStore.configuration.AppProperties;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class MultiStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiStoreApplication.class, args);
	}

}
