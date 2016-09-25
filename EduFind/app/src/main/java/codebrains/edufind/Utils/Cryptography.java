package codebrains.edufind.Utils;

import java.security.MessageDigest;

/**
 * Class that contains necessary methods for encrypting or hashing data.
 */
public class Cryptography {

    //Constructor
    public Cryptography() {

    }

    /**
     * Method that creates a SHA-256 hash of a string message.
     * @param message The message to be hashed.
     * @return Returns a string representation of the hashed message.
     */
    public String HashSHA256(String message) {

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
