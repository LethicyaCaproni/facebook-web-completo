
package model.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class PasswordEncryptor {

    private PasswordEncryptor() {
        // Impede a criação de instâncias desta classe utilitária.
    }

    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null) {
            throw new IllegalArgumentException("A senha não pode ser nula.");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    plainTextPassword.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Erro ao gerar o hash da senha.", e
            );
        }
    }

    public static boolean checkPassword(
            String plainTextPassword,
            String hashedPassword
    ) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }

        String calculatedHash = hashPassword(plainTextPassword);

        return MessageDigest.isEqual(
                calculatedHash.getBytes(StandardCharsets.UTF_8),
                hashedPassword.getBytes(StandardCharsets.UTF_8)
        );
    }
}
