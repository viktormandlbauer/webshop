package at.fhtw.webshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirectoryInitializer {

    @Value("${output.path.product.images}")
    private String pathProducteImages;

    @Value("${output.path.receipts}")
    private String pathReceipts;

    @EventListener(ContextRefreshedEvent.class)
    public void ensureDirectoriesExist() {
        createDirectoryIfNotExists(pathProducteImages);
        createDirectoryIfNotExists(pathReceipts);
    }

    private void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Unable to create directory: " + path);
            }
        }
    }
}