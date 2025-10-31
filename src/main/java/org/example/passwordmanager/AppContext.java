package org.example.passwordmanager;

public class AppContext {
    public static final UserRepository UserRepo;

    static {
        String jdbcUrl = System.getenv("APP_JDBC_URL");
        String dbUser = System.getenv("APP_DB_USER");
        String dbPass = System.getenv("APP_DB_PASSWORD");

        if (jdbcUrl != null && dbUser != null && dbPass != null) {
            UserRepo = new PostgresDatabase(jdbcUrl, dbUser, dbPass);
            System.out.println("Using Supabase PostgresDatabase");
        } else {
            UserRepo = new InMemoryDataBase();
            System.out.println("Using InMemoryDataBase (no DB credentials found)");
        }
    }

    private AppContext() {}
}
