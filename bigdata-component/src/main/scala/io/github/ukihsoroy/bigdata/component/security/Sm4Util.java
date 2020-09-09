package io.github.ukihsoroy.bigdata.component.security;

import io.github.ukihsoroy.bigdata.component.security.sm.SM4;
import io.github.ukihsoroy.bigdata.component.security.sm.SM4Context;
import io.github.ukihsoroy.bigdata.component.security.sm.SMUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author K.O
 */
public class Sm4Util {

    private static final boolean HEX_STRING = false;

    private Sm4Util() {
    }

    public static String encryptDataEcb(String secretKey, String plainText) {
        try {
            SM4Context e = new SM4Context();
            e.isPadding = true;
            e.mode = 1;
            byte[] keyBytes;
            if(HEX_STRING) {
                keyBytes = SMUtil.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4SetKeyEnc(e, keyBytes);
            byte[] encrypted = sm4.sm4CryptEcb(e, plainText.getBytes(StandardCharsets.UTF_8));
            String cipherText = Base64Helper.encodeToString(encrypted);
            if(cipherText != null && cipherText.trim().length() > 0) {
            }

            return cipherText;
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static String decryptDataEcb(String secretKey, String cipherText) {
        try {
            SM4Context e = new SM4Context();
            e.isPadding = true;
            e.mode = 0;
            byte[] keyBytes;
            if(HEX_STRING) {
                keyBytes = SMUtil.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4SetKeyDec(e, keyBytes);
            byte[] decrypted = sm4.sm4CryptEcb(e, Base64Helper.decodeFromString(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static String encryptDataCbc(String iv, String secretKey, String plainText) {
        try {
            SM4Context e = new SM4Context();
            e.isPadding = true;
            e.mode = 1;
            byte[] keyBytes;
            byte[] ivBytes;
            if(HEX_STRING) {
                keyBytes = SMUtil.hexStringToBytes(secretKey);
                ivBytes = SMUtil.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4SetKeyEnc(e, keyBytes);
            byte[] encrypted = sm4.sm4CryptCbc(e, ivBytes, plainText.getBytes(StandardCharsets.UTF_8));
            String cipherText = Base64Helper.encodeToString(encrypted);
            if(cipherText != null && cipherText.trim().length() > 0) {
            }

            return cipherText;
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public static String decryptDataCbc(String iv, String secretKey, String cipherText) {
        try {
            SM4Context e = new SM4Context();
            e.isPadding = true;
            e.mode = 0;
            byte[] keyBytes;
            byte[] ivBytes;
            if(HEX_STRING) {
                keyBytes = SMUtil.hexStringToBytes(secretKey);
                ivBytes = SMUtil.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4SetKeyDec(e, keyBytes);
            byte[] decrypted = sm4.sm4CryptCbc(e, ivBytes, Base64Helper.decodeFromString(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        String plainText = "ererfeiisgod";
        String secretKey = "JeF8U9wHFOMfs2Y8";
        System.out.println("ECB模式");
        String cipherText = encryptDataEcb(secretKey, plainText);
        System.out.println("密文: " + cipherText);
        System.out.println();
        plainText = decryptDataEcb(secretKey, cipherText);
        System.out.println("明文: " + plainText);
        System.out.println();
        System.out.println("CBC模式");
        String iv = "UISwD9fW6cFh9SNS";
        cipherText = encryptDataCbc(iv, secretKey, plainText);
        System.out.println("密文: " + cipherText);
        System.out.println();
        plainText = decryptDataCbc(iv, secretKey, cipherText);
        System.out.println("明文: " + plainText);
    }
}
