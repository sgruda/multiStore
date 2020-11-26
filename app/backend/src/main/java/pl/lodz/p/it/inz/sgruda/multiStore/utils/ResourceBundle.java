package pl.lodz.p.it.inz.sgruda.multiStore.utils;
import lombok.extern.java.Log;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log
public class ResourceBundle {
    private final String TRANSLATION_LOCATION = "classpath:translation.properties";
    private Properties properties;

    public ResourceBundle() {
        this.properties = new Properties();
        try {
            File file = ResourceUtils.getFile(TRANSLATION_LOCATION);
            InputStream in = new FileInputStream(file);
            this.properties.load(in);
        } catch (IOException e) {
            log.severe("Error: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
}
