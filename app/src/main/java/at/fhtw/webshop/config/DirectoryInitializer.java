package at.fhtw.webshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirectoryInitializer {

    @Value("${image.upload.path}")
    private String imageUploadPath;

    @Value("${receipts.output.path}")
    private String receiptsOutputPath;

    @EventListener(ContextRefreshedEvent.class)
    public void ensureDirectoriesExist() {
        createDirectoryIfNotExists(imageUploadPath);
        createDirectoryIfNotExists(receiptsOutputPath);
    }

    private void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Konnte Verzeichnis nicht erstellen: " + path);
            }
        }
    }
}