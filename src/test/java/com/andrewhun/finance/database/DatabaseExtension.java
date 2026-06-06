package com.andrewhun.finance.database;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class DatabaseExtension implements BeforeAllCallback, AfterAllCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
        ExtensionContext.Namespace.create(DatabaseExtension.class);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        Path tempDir = Files.createTempDirectory("desktop-finance-test");
        context.getStore(NAMESPACE).put("tempDir", tempDir);
        DatabaseConnection.setDatabaseDirectory(tempDir);
        new DatabaseInitializer().initialize();
    }

    @Override
    public void afterAll(ExtensionContext context) throws IOException {
        DatabaseConnection.setDatabaseDirectory(null);
        Path tempDir = context.getStore(NAMESPACE).remove("tempDir", Path.class);
        if (tempDir != null) {
            try (var stream = Files.walk(tempDir)) {
                stream.sorted(Comparator.reverseOrder())
                      .map(Path::toFile)
                      .forEach(File::delete);
            }
        }
    }
}