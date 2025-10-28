package org.example.passwordmanager;

public class AppContext {
    // as database isn't ready, we use this in memory repo to test GUI flows today.
    // later to be replaced wih database
    public static final InMemoryDataBase UserRepo = new  InMemoryDataBase();
    private AppContext() {}
}
