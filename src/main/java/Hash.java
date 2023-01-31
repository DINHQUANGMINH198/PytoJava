
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.lang.*;

public class Hash {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = new byte[32];
    private static final String secretKey = "ViettelAI@2023";


    public static String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(data.getBytes());
        return new BASE64Encoder().encode(encValue);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    private static Key generateKey() {
        System.arraycopy(secretKey.getBytes(), 0, keyValue, 0, Math.min(keyValue.length, secretKey.getBytes().length));
        return new SecretKeySpec(keyValue, ALGORITHM);
    }

    public static void main(String[] args) throws Exception {
        String password ="123456aA@";
        String hashPassword = encrypt(password);
        System.out.println(hashPassword);
        String passwordDecrypt = decrypt(hashPassword);
        System.out.println(passwordDecrypt);
    }
}

