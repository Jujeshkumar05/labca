package com.labca.listeners;

import com.labca.base.BaseTest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * JUnit 5 extension that saves a screenshot to target/screenshots when a test fails.
 * BaseTest defers driver.quit() to this extension so the browser is still open here.
 */
public class ScreenshotOnFailure implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        withDriver(context, driver -> {
            if (!(driver instanceof TakesScreenshot ts)) return;
            try {
                Path dir = Path.of("target", "screenshots");
                Files.createDirectories(dir);
                String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                String name = context.getRequiredTestMethod().getName() + "-" + stamp + ".png";
                Files.copy(ts.getScreenshotAs(OutputType.FILE).toPath(), dir.resolve(name),
                        StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Screenshot saved: " + dir.resolve(name).toAbsolutePath());
            } catch (IOException | RuntimeException e) {
                System.err.println("Could not save screenshot: " + e.getMessage());
            }
        });
        quit(context);
    }

    @Override
    public void testSuccessful(ExtensionContext context) { quit(context); }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) { quit(context); }

    private static void quit(ExtensionContext context) {
        withDriver(context, WebDriver::quit);
    }

    private static void withDriver(ExtensionContext context, java.util.function.Consumer<WebDriver> action) {
        context.getTestInstance()
                .filter(BaseTest.class::isInstance)
                .map(t -> ((BaseTest) t).getDriver())
                .ifPresent(action);
    }
}
