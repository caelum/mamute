package org.mamute.infra;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    /**
     * Encodes a string
     * 
     * @param str
     *            String to encode
     * @return Encoded String
     */
    public static String crypt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        StringBuffer hexString = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();

            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return hexString.toString();
    }
}