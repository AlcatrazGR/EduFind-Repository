package codebrains.edufind.Utils;

import java.security.MessageDigest;

/**
 * Created by Vasilhs on 9/25/2016.
 */
public class Cryptography {

    //Constructor
    public Cryptography() {

    }

    public static String HashSHA256(String message) {

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }


}
