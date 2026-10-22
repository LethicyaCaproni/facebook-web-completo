
package teste;

import model.utils.PasswordEncryptor;

public class TestePasswordEncryptor {

    public static void main(String[] args) {
        String senha = "Lele@1234";

        String hash = PasswordEncryptor.hashPassword(senha);

        System.out.println("Hash gerado: " + hash);
        System.out.println("Tamanho: " + hash.length());
    }
}
