package org.example.passwordmanager;

import javafx.scene.Scene;
import java.io.*;
import java.util.Properties;

public class ThemeManager {
    private static boolean darkMode = false;
    private static final String SETTINGS_FILE = "user-settings.properties";

    private ThemeManager() {}

    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        String theme = darkMode ? "dark-theme.css" : "light-theme.css";
        scene.getStylesheets().add(ThemeManager.class.getResource(theme).toExternalForm());
    }

    public static void toggleTheme(Scene scene) {
        darkMode = !darkMode;
        applyTheme(scene);
        saveThemePreference();
    }

    public static void setDarkMode(boolean enabled) {
        darkMode = enabled;
        saveThemePreference();
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    // Optional: load and save from a tiny local file
    public static void loadThemePreference() {
        try (InputStream input = new FileInputStream(SETTINGS_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            darkMode = Boolean.parseBoolean(prop.getProperty("darkMode", "false"));
        } catch (IOException ignored) {}
    }

    private static void saveThemePreference() {
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            Properties prop = new Properties();
            prop.setProperty("darkMode", String.valueOf(darkMode));
            prop.store(output, null);
        } catch (IOException ignored) {}
    }
}
