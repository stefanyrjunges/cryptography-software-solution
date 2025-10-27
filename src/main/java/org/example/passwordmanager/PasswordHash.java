package org.example.passwordmanager;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
    private static final int COST = 12;

    private PasswordHash(){}

    //hash for registration
    public static String hash(String plainPassword){
        if(plainPassword == null || plainPassword.isEmpty()){
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        String salt = BCrypt.gensalt(COST); //random salt included;
        return BCrypt.hashpw(plainPassword, salt); // returns hash + salt
    }

    //verify for login
    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null || storedHash.isBlank()) return false;
        try {
            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (IllegalArgumentException badHash) {
            return false; // if storedHash isn’t a valid BCrypt string
        }
    }
}
