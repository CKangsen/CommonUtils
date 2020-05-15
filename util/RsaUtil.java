package com.casstime.base.util;



import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import cass.org.apache.commons.codec.binary.Base64;



public class RsaUtil {
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final int RAS_KEY_SIZE = 1024;
    private static final String ALGORITHM = "SHA1WithRSA";

    public RsaUtil() {
    }

    public static String encrypt(RsaUtil.RsaConfig config, String value) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            RSAPublicKey publicKey = getPublicKey(config.getPublicKey());
            cipher.init(1, publicKey);
            return Base64.encodeBase64String(rsaSplitCodec(cipher, 1, value.getBytes("UTF-8"), publicKey.getModulus().bitLength()));
        } catch (Exception var4) {
            LogUtil.e("加密字符串[{}]时遇到异常" + value);
//            throw new IllegalException(ErrorCode.AUTH_RSA_ENCRYPT_FAILED, new Object[]{value, var4});
            return null;
        }
    }

    public static String decrypt(RsaUtil.RsaConfig config, String base64) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            RSAPrivateKey privateKey = getPrivateKey(config.getPrivateKey());
            cipher.init(2, privateKey);
            return new String(rsaSplitCodec(cipher, 2, Base64.decodeBase64(base64), privateKey.getModulus().bitLength()), "UTF-8");
        } catch (Exception var4) {
            LogUtil.e("解密字符串[{}]时遇到异常:" + base64);
//            throw new IllegalException(ErrorCode.AUTH_RSA_DECRYPT_FAILED, new Object[]{base64, var4});
            return null;
        }
    }

    public static RsaUtil.RsaConfig generateRsaConfig() {
        return generateRsaConfig(1024);
    }

    public static RsaUtil.RsaConfig generateRsaConfig(int keySize) {
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException var8) {
            throw new IllegalArgumentException("No such algorithm-->[RSA]");
        }

        kpg.initialize(keySize);
        KeyPair keyPair = kpg.generateKeyPair();
        RsaUtil.RsaConfig config = new RsaUtil.RsaConfig();
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        config.setPublicKey(publicKeyStr);
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        config.setPrivateKey(privateKeyStr);
        return config;
    }

    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        return (RSAPrivateKey)keyFactory.generatePrivate(pkcs8KeySpec);
    }

    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        return (RSAPublicKey)keyFactory.generatePublic(x509KeySpec);
    }

    public static String sign(String data, String privateKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException {
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(getPrivateKey(privateKey));
        signature.update(data.getBytes());
        return Base64.encodeBase64URLSafeString(signature.sign());
    }

    public static boolean verify(String data, String sign, String publicKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException {
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(getPublicKey(publicKey));
        signature.update(data.getBytes());
        return signature.verify(Base64.decodeBase64(sign));
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == 2) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;

        try {
            while(datas.length > offSet) {
                byte[] buff;
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }

                out.write(buff, 0, buff.length);
                ++i;
                offSet = i * maxBlock;
            }
        } catch (Exception var10) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", var10);
        }

        byte[] resultDatas = out.toByteArray();
        return resultDatas;
    }

    public static class RsaConfig {
        private String publicKey;
        private String privateKey;

        public RsaConfig() {
        }

        public String getPublicKey() {
            return this.publicKey;
        }

        public String getPrivateKey() {
            return this.privateKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof RsaUtil.RsaConfig)) {
                return false;
            } else {
                RsaUtil.RsaConfig other = (RsaUtil.RsaConfig)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$publicKey = this.getPublicKey();
                    Object other$publicKey = other.getPublicKey();
                    if (this$publicKey == null) {
                        if (other$publicKey != null) {
                            return false;
                        }
                    } else if (!this$publicKey.equals(other$publicKey)) {
                        return false;
                    }

                    Object this$privateKey = this.getPrivateKey();
                    Object other$privateKey = other.getPrivateKey();
                    if (this$privateKey == null) {
                        if (other$privateKey != null) {
                            return false;
                        }
                    } else if (!this$privateKey.equals(other$privateKey)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof RsaUtil.RsaConfig;
        }

        @Override
        public int hashCode() {
            int PRIME = 1;
            int result = 1;
            Object $publicKey = this.getPublicKey();
            result = result * 59 + ($publicKey == null ? 43 : $publicKey.hashCode());
            Object $privateKey = this.getPrivateKey();
            result = result * 59 + ($privateKey == null ? 43 : $privateKey.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "RsaUtil.RsaConfig(publicKey=" + this.getPublicKey() + ", privateKey=" + this.getPrivateKey() + ")";
        }
    }
}

