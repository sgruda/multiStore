package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import pl.lodz.p.it.inz.sgruda.multiStore.configuration.AppProperties;

@Log
@EnableConfigurationProperties(AppProperties.class)
@EntityScan(basePackageClasses = {
		MultiStoreApplication.class,
		Jsr310JpaConverters.class
})
@SpringBootApplication
public class MultiStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiStoreApplication.class, args);
	}

}
