package org.example.passwordmanager;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.List;

public final class SQLInjectionPrevention {

    //SQL Injection Prevention - Teephopalex Machugh

    private SQLInjectionPrevention() {}

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Map<String, String> ALLOWED_SORT_COLUMNS = Map.of(
            "email", "email",
            "created_at", "created_at"
    );

    public static String validateEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email cannot be null");
        String trimmed = email.trim().toLowerCase();
        if (trimmed.length() > 254)
            throw new IllegalArgumentException("Email too long");
        if (!EMAIL_PATTERN.matcher(trimmed).matches())
            throw new IllegalArgumentException("Invalid email format");
        return trimmed;
    }

    public static String sanitizeLike(String input) {
        if (input == null) return "";
        return input
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    public static String likeContains(String term) {
        return "%" + sanitizeLike(term) + "%";
    }

    public static String getSafeOrderBy(String userInput) {
        return ALLOWED_SORT_COLUMNS.getOrDefault(userInput, "email");
    }

    public static String cleanText(String input, int maxLength) {
        if (input == null) return "";
        String cleaned = input.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        return cleaned.length() > maxLength ? cleaned.substring(0, maxLength) : cleaned;
    }

    public static String buildPlaceholders(List<?> list) {
        return list.stream().map(x -> "?").collect(Collectors.joining(", "));
    }
}
