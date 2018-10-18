


import android.text.TextUtils;
import android.util.Base64;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by cks on 18-4-17.
 *
 * salt+psw 先做一次AES加密，再用salt为key做md5.
 *
 * debug 做一次md5； 非debug 做 n次 md5.
 */

public class PalmEncryptUtils {

    public static final String TAG = PalmEncryptUtils.class.getCanonicalName();
    private static final String AES_KEY = "Ov6fKFZDHY99iDpQSnG4TUrbi+wrrzwX3Tv2MZ7Uh7w=";//do not modify
    private static final int TIMES = 5;


    public static  synchronized String encrypt(String salt, String psw) {
        if (TextUtils.isEmpty(salt) || TextUtils.isEmpty(psw)){
            LogUtils.d(TAG,"salt value or password is null");
            return null;
        }
        String param = salt+psw;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);

            byte[] KeyBytes = Base64.decode(AES_KEY,Base64.NO_WRAP);
            SecretKey secretKey = new SecretKeySpec(KeyBytes,"AES");
            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            byte[] encodeResult = cipher.doFinal(param.getBytes());
            String encode = Base64.encodeToString(encodeResult,Base64.NO_WRAP);
            String result = EncryptUtils.encryptHmacMD5ToString(encode,salt);
            for (int i=0; i<TIMES; i++) {
                result = EncryptUtils.encryptHmacMD5ToString(result,salt);
            }
            return result;

        } catch (Exception e){
            LogUtils.e(TAG,"encrypt",e.getMessage());
            return null;
        }
    }


    public class Constant {
        public static final String ENC_SECRET = "palmcredit";//do not modify
    }
    /**
     *加密
     **/
    public static synchronized String encryptText(String clearText) {
        try {
            DESKeySpec keySpec = new DESKeySpec(
                    Constant.ENC_SECRET.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encrypedPwd = Base64.encodeToString(cipher.doFinal(clearText
                    .getBytes("UTF-8")), Base64.DEFAULT);
            return encrypedPwd;
        } catch (Exception e) {
            LogUtils.e(TAG,"encryptText",e.getMessage(),clearText);
        }
        return clearText;
    }

    /**
     *解密
     **/
    public static synchronized String decryptText(String encryptedPwd) {
        try {
            DESKeySpec keySpec = new DESKeySpec(Constant.ENC_SECRET.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encryptedWithoutB64 = Base64.decode(encryptedPwd, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = cipher.doFinal(encryptedWithoutB64);
            return new String(plainTextPwdBytes);
        } catch (Exception e) {
            LogUtils.e(TAG,"decryptText",e.getMessage(),encryptedPwd);
        }
        return encryptedPwd;
    }



    /**
     *加密
     **/
    public static synchronized String encryptText(String needEncryptWord,String alias) {
        try {
            DESKeySpec keySpec = new DESKeySpec(
                    alias.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encrypedPwd = Base64.encodeToString(cipher.doFinal(needEncryptWord
                    .getBytes("UTF-8")), Base64.DEFAULT);
            return encrypedPwd;
        } catch (Exception e) {
            LogUtils.e(TAG,"encryptText",e.getMessage(),needEncryptWord);
        }
        return needEncryptWord;
    }

    /**
     *解密
     **/
    public static synchronized String decryptText(String needDecryptWord,String alias) {
        try {
            DESKeySpec keySpec = new DESKeySpec(alias.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encryptedWithoutB64 = Base64.decode(needDecryptWord, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = cipher.doFinal(encryptedWithoutB64);
            return new String(plainTextPwdBytes);
        } catch (Exception e) {
            LogUtils.e(TAG,"decryptText",e.getMessage(),needDecryptWord);
        }
        return needDecryptWord;
    }


    public static synchronized String getAesKeyForRequestBody(String url, String salt){
        if (TextUtils.isEmpty(url)) return null;
        int index = url.lastIndexOf("/");
        url = url.substring(index+1,url.length());
        if ("login.do".equals(url)
                || "queryAppVersion.do".equals(url)
                || "getBanner.do".equals(url)
                || "getHelpList.do".equals(url)
                || "queryLanguage.do".equals(url)
                || "checkUpgrade.do".equals(url)
                || "loginNew.do".equals(url)){
            salt = "";
        }
        String aesKey =  salt + url ;
        LogUtils.i(TAG,"aesKey : "+aesKey);
        return aesKey;
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static synchronized String encryptRequestBody(String content, String password) {
        try {
            byte[] bytes1,bytes2,bytes3;
            bytes1 = password.getBytes("utf-8");
            int length = bytes1.length;
            if (bytes1.length >= 16){
                bytes3 = new byte[16];
                System.arraycopy(bytes1, 0, bytes3, 0,16);
            } else {
                bytes2 = new byte[16-length];
                bytes3 = new byte[16];
                System.arraycopy(bytes1, 0, bytes3, 0, bytes1.length);
                System.arraycopy(bytes2, 0, bytes3, bytes1.length, bytes2.length);
            }

            SecretKeySpec key = new SecretKeySpec(bytes3, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            String temp = parseByte2HexStr(result);
            LogUtils.d(TAG,"encryptRequestBody ： "+temp);
            return temp; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG,"encryptRequestBody",e.getMessage());
        }
        return null;
    }

    /**解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static synchronized String decryptRequestBody(String content, String password) {
        try {
            byte[] bytes1,bytes2,bytes3;
            bytes1 = password.getBytes("utf-8");
            int length = bytes1.length;
            if (bytes1.length >= 16){
                bytes3 = new byte[16];
                System.arraycopy(bytes1, 0, bytes3, 0,16);
            } else {
                bytes2 = new byte[16-length];
                bytes3 = new byte[16];
                System.arraycopy(bytes1, 0, bytes3, 0, bytes1.length);
                System.arraycopy(bytes2, 0, bytes3, bytes1.length, bytes2.length);
            }

            SecretKeySpec key = new SecretKeySpec(bytes3, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(parseHexStr2Byte(content));
            String temp = new String(result);
            LogUtils.d(TAG,"decryptRequestBody ： "+temp);
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG,"decryptRequestBody",e.getMessage());
        }
        return null;
    }

    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static synchronized String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static synchronized byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


}
