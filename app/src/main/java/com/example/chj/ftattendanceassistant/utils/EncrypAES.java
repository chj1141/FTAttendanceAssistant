package com.example.chj.ftattendanceassistant.utils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.os.Build.VERSION_CODES.M;

/**
 * AES算法加密类
 * @author Administrator
 *
 */
public class EncrypAES {
    private final static String HEX = "0123456789ABCDEF";
    private static final String TAG="EncryAES";

    /**
     * Cipher负责完成加密或解密工作
     */
    private Cipher c;

    /**
     * 该字节数组负责保存加密的结果
     */
    private byte[] cipherByte;

    /**
     * 用于生产密钥
     */
    private static final String SECRETKET="AESDemo";
    private SecretKeySpec deskey;

    public EncrypAES(){
        Security.addProvider(new com.sun.crypto.provider.SunJCE());

        try {
            //deskey = new SecretKeySpec(getRawKey(SECRETKET.getBytes()),"AES");
            deskey = new SecretKeySpec(getAESKey(SECRETKET.getBytes()),"AES");
            //生成Cipher对象,指定其支持的DES算法
            c = Cipher.getInstance("AES");
        } catch (Exception e) {
            Log.e(TAG, "EnrypAES construct failed.",e);
        }
    }

    /**
     * 对字符串加密,返回字节数组
     *
     * @param str
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private byte[] Encrytor(String str) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        c.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] src = str.getBytes();

        cipherByte = c.doFinal(src);        // 加密，结果保存进cipherByte
        return cipherByte;
    }

    /**
     * 对字符串解密，返回字节数组
     *
     * @param buff
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private byte[] Decryptor(byte[] buff) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
        c.init(Cipher.DECRYPT_MODE, deskey);
        cipherByte = c.doFinal(buff);

        return cipherByte;
    }

    /**
     * 对字符串进行加密，返回字符串
     * @param string 要加密的字符串
     * @return 加密后的字符串
     */
    public String EncryptorString(String string){
        String result =null;
        byte[] encontent;
        try {
            encontent = Encrytor(string);
            result=toHex(encontent);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "EncryptorString",e);
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, "EncryptorString",e);
        } catch (BadPaddingException e) {
            Log.e(TAG, "EncryptorString",e);
        }

        return result;
    }

    /**
     * 对字符串进行解密，返回字符串
     * @param string 要解密的字符串
     * @return 解密后的字符串
     */
    public String DecryptorString(String string){
        byte[] cryptcontent=toByte(string);
        byte[] decontent;
        String result=null;

        try {
            decontent=Decryptor(cryptcontent);
            result=new String(decontent);

        } catch (InvalidKeyException e) {
            Log.e(TAG,"DecryptorString failed.",e);
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG,"DecryptorString failed.",e);
        } catch (BadPaddingException e) {
            Log.e(TAG,"DecryptorString failed.",e);
        }

        return result;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        //KeyGenerator 此类提供(对称加密算法:AES,DES 等等)密钥生成器的功能
        KeyGenerator kgen = KeyGenerator.getInstance("AES");

        /**
         * 这一句很关键。
         * 网上有的代码是这一句SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
         * 在getInstance函数中少了一个String参数。
         * 这个参数是不能少的，因为如果少了这个参数的话，生成的密钥是随机的，而加密和解密必须是用同样的密钥的
         * 所以会出现不能解密的问题（总是抛出BadPaddingException异常）。而且这种方法中，对比少了一个参数的加密结果，
         * 会发现每一次加密的结果都是不一样的。
         * 而用下面的语句得到的密钥加密，同样的字符串任何时候得到的加密结果都是一样的。
         *
         * 后得知，对于4.2以上版本的（包含），该函数默认的provider不再是Crypto，而是新的OpenSSL，因此需要将其强制置为Crypto
         */
        SecureRandom sr;
        if (Build.VERSION.SDK_INT >= 28){
            String stringKey = "string got from machines below Pie";
            byte[] encodedKey     = Base64.decode(stringKey, Base64.DEFAULT);
            SecretKey secretKey= new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            byte[] raw = secretKey.getEncoded();
            return raw;
        }
        else if(Build.VERSION.SDK_INT >= 23){
            sr = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
        }else if(Build.VERSION.SDK_INT >= 17){
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        }else{
            sr = SecureRandom.getInstance("SHA1PRNG");
        }

        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static byte[] getAESKey(byte[] seed){
        try{

            return InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(seed, 32);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //将文本字符串转化为16进制字符串形式
    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    //将16进制字符串转化为文本字符串
    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    //将16进制字符串转化为字节型
    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    //将字节型转化为16进制字符串
    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }
    public static class CryptoProvider extends Provider {
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG","org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}
