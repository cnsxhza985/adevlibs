/*
 * System: MediaGuideEU

 * Copyright (C) 2012, TOSHIBA Corporation.
 * 
 * This fine includes the class which helps to encrypt and decrypt using AES.
 * 
 * @version 1.00 
 * @author An Zewei
 * @date 2012/07/12
 */
package adevlibs.algorithm;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;

/**
 *	本类用来进行AES加密和解密
 * 
 * @author wangfan
 */
public class AESHelper {
    /**
     * 加密或解密使用的 密钥
     */
    private static String m_password = "";

    /**
     * 初始化加密或解密使用的 密钥
     * @param password 密钥
     */
    public static void init(String password) {
        m_password = password;
    }

    /**
     * 对字符串进行AES加密
     * 
     * @param sourceStr 用于进行AES加密的原始字符串
     * 
     * @return 加密之后的字符串
     */
    public static String aesEncrypt(String sourceStr) {
        if (TextUtils.isEmpty(m_password) || TextUtils.isEmpty(sourceStr)) {
            return "";
        }

        byte[] result = null;
        try {

            byte[] rawkey = getRawKey(m_password.getBytes());
            result = encrypt(rawkey, sourceStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String content = toHex(result);
        return content;

    }

    /**
     * 对AES加密的字符串进行AES解密
     * 
     * @param encryptedStr 已经被AES加密的字符串
     * 
     * @return 解密之后的字符串
     */
    public static String aesDecrypt(String encryptedStr) {
        if (TextUtils.isEmpty(m_password) || TextUtils.isEmpty(encryptedStr)) {
            return "";
        }

        try {
            byte[] rawKey = getRawKey(m_password.getBytes());
            byte[] enc = toByte(encryptedStr);
            byte[] result = decrypt(rawKey, enc);
            String coentn = new String(result, "UTF-8");
            return coentn;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取AES加密的rawkey
     * 
     * @param seed AES加密使用的种子
     * 
     * @return rawKey
     * 
     * @throws Exception
     */
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        kgen.init(128, sr);
        SecretKey sKey = kgen.generateKey();
        byte[] raw = sKey.getEncoded();

        return raw;
    }

    /**
     * 使用raw数组对byte数组进行加密
     * 
     * @param rawArr raw数组
     * @param sourceByteArr 要加密的原始byte数组
     * 
     * @return 加密之后的数组
     * 
     * @throws Exception
     */
    private static byte[] encrypt(byte[] rawArr, byte[] sourceByteArr) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(rawArr, "AES");
        // Cipher cipher = Cipher.getInstance("AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(sourceByteArr);
        return encrypted;
    }

    /**
     * 使用rawArr低byte数组进行解密
     * 
     * @param rawArr raw数组
     * @param encryptedByteArr 已加密的byte数组
     * 
     * @return 解密之后的 byte数组
     * 
     * @throws Exception
     */
    private static byte[] decrypt(byte[] rawArr, byte[] encryptedByteArr) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(rawArr, "AES");
        // Cipher cipher = Cipher.getInstance("AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encryptedByteArr);
        return decrypted;
    }

    /**
     * 将Hex字符串转换为byte数组
     * 
     * @param hexString Hex串
     * 
     * @return byte数组
     */
    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }

        return result;
    }

    /**
     * 将一个byte数组转换成 hex 个提示的字符串
     * 
     * @param sourceByteArr byte数组
     * 
     * @return the hex 格式的字符串
     */
    private static String toHex(byte[] sourceByteArr) {
        if (sourceByteArr == null) {
            return "";
        }

        StringBuffer result = new StringBuffer(2 * sourceByteArr.length);

        for (int i = 0; i < sourceByteArr.length; i++) {
            appendHex(result, sourceByteArr[i]);
        }

        return result.toString();
    }

    /**
     * 将指定的hex附加到原始的字符串上去
     * 
     * @param sourceStr 原始的字符串
     * @param appendByte 将要附加上去的byte
     */
    private static void appendHex(StringBuffer sourceStr, byte appendByte) {
        final String HEX = "0123456789ABCDEF";
        sourceStr.append(HEX.charAt((appendByte >> 4) & 0x0f)).append(HEX.charAt(appendByte & 0x0f));
    }
}
