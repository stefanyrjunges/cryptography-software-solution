package org.example.passwordmanager;

import org.mindrot.jbcrypt.BCrypt;

public class AuthManager {

    private final UserRepository userRepo;

    // Using Fake Hash so that we can prevent timing attack
    private static final  String Fake_BCRYPT_HASH =
            "$2a$12$e9uZrP1z3Y8y3q9aV1p9EuWz6b6q1sM8fF7Yv8q9WqV1tZ2cE9a6K";

    public AuthManager(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    public boolean authenticate(String email, String plainPassword) {
        if(email == null || plainPassword == null) return false;
        if(plainPassword.length() > 1024) return false;

        // Normalize email
        String normalized = email.trim().toLowerCase();

        // Retrieve stored hash from Database
        String storedHash = userRepo.findHashByEmail(normalized);

        if (storedHash == null) {
            // I will run this fake, to prevent timing attack
            BCrypt.checkpw(plainPassword, Fake_BCRYPT_HASH);
            return false;
        }

        return BCrypt.checkpw(plainPassword, storedHash);
    }


}
// validates input
// gets hash from DB
// calls PasswordHash
// returns true/false

