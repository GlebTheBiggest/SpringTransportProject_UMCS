package org.example.security;

import org.mindrot.jbcrypt.BCrypt;

public class Hasher {
    public static String hashString(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkString(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
