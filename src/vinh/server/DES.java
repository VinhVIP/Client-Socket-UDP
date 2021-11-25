package vinh.server;

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Vinh
 */
public class DES {

    /**
     * Mã hóa DES
     * @param data      chuỗi cần mã hóa
     * @param key       khóa
     * @return          chuỗi được mã hóa
     */
    public static String encrypt(String data, String key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            
            byte[] byteEncrypted = cipher.doFinal(data.getBytes());
            String encrypted = Base64.getEncoder().encodeToString(byteEncrypted);
            return encrypted;
        } catch (Exception ex) {
            Logger.getLogger(DES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Giải mã DES
     * 
     * @param encrypted     chuỗi bị mã hóa cần được giải mã
     * @param key           khóa
     * @return              chuỗi sau khi giải mã
     */
    public static String decrypt(String encrypted, String key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
            byte[] byteEncrypted = Base64.getDecoder().decode(encrypted);

            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] byteDecrypted = cipher.doFinal(byteEncrypted);
            String decrypted = new String(byteDecrypted);

            return decrypted;
        } catch (Exception ex) {
            Logger.getLogger(DES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        // Testing
        
        String s = "Tran quang vinh tran quang vinh";
        String key = "abcd1234";
        String en = encrypt(s, key);
        String de = decrypt(en, key);

        System.out.println(en);
        System.out.println(en.length());
        System.out.println(de);
    }
}
