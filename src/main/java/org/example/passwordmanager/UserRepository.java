package org.example.passwordmanager;

public interface UserRepository {
    boolean usernameExists(String email);
    void saveUser(String email, String passwordHash);
    String findHashByEmail(String email);
}