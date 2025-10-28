package org.example.passwordmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDataBase {
    private final Map<String, String> users = new ConcurrentHashMap<>(); //email -> bcrypt hash

    public boolean usernameExists(String email){
        return users.containsKey(email);
    }

    public void saveUser(String email, String passwordHash){
        users.put(email, passwordHash);
    }

    public String findHashByEmail(String email){
        return users.get(email);
    }
}
