package com.kplike.library.common.storage;

import android.os.Build;
import android.util.Log;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 每一个特定的存储类型(如外部存储工具)需要它自己的
 * 配置。这个配置类构建和用于存储类。
 * Created by admin on 2017/3/23.
 */

public class FinestStorageConfiguration {
    /**
     * 最好的块大小
     */
    private int mChunkSize;
    private boolean mIsEncrypted;
    private byte[] mIvParameter;
    private byte[] mSecretKey;

    private FinestStorageConfiguration(Builder builder) {
        mChunkSize = builder._chunkSize;
        mIsEncrypted = builder._isEncrypted;
        mIvParameter = builder._ivParameter;
        mSecretKey = builder._secretKey;
    }


    /**
     * 获取块大小。块的大小由读取文件时使用
     * {@link FileInputStream#read(byte[], int, int)}.
     *
     * @return The chunk size
     */
    public int getChuckSize() {
        return mChunkSize;
    }

    /**
     * 加密文件内容
     *
     */
    public boolean isEncrypted() {
        return mIsEncrypted;
    }

    /**
     * 获取密钥
     *
     * @return
     */
    public byte[] getSecretKey() {
        return mSecretKey;
    }

    /**
     * 获取iv参数
     *
     * @return
     */
    public byte[] getIvParameter() {
        return mIvParameter;
    }


    /**
     * Configuration Builder类
     */
    public static class Builder {
        private int _chunkSize = 8 * 1024; // 8kbits = 1kbyte;
        private boolean _isEncrypted = false;
        private byte[] _ivParameter = null;
        private byte[] _secretKey = null;

        private static final String UTF_8 = "UTF-8";

        public Builder() {
        }

        /**
         * Build the configuration for storage.
         *
         * @return
         */
        public FinestStorageConfiguration build() {
            return new FinestStorageConfiguration(this);
        }

        /**
         * 组块的大小。查克大小是在阅读文件时使用块{ @link FileInputStream #读(byte[],int,int)}。更可取的
         * 值为1024 xn碎片。当N等于2的幂(如1、2、4、8、16日…)< br >
         * <br>
         *
         * The default: <b>8 * 1024</b> = 8192 bits
         *
         * @param chunkSize
         *            The chunk size in bits
         * @return The {@link Builder}
         */
        public Builder setChuckSize(int chunkSize) {
            _chunkSize = chunkSize;
            return this;
        }

        /**
         * 读写磁盘内容加密和解密<br>
         *
         *
         * @param ivx
         *            This is not have to be secret. It used just for better
         *            randomizing the cipher. You have to use the same IV
         *            parameter within the same encrypted and written files.
         *            Means, if you want to have the same content after
         *            descryption then the same IV must be used.<br>
         * <br>
         *
         *            <b>Important: The length must be 16 long</b><br>
         *
         *            <i>About this parameter from wiki:
         *            https://en.wikipedia.org
         *            /wiki/Block_cipher_modes_of_operation
         *            #Initialization_vector_.28IV.29</i><br>
         * <br>
         * @param secretKey
         *            Set the secret key for encryption of file content. <br>
         * <br>
         *
         *            <b>Important: The length must be 16 long</b> <br>
         *
         *            <i>Uses SHA-256 to generate a hash from your key and trim
         *            the result to 128 bit (16 bytes)</i><br>
         * <br>
         * @see <a
         *      href="https://en.wikipedia.org/wiki/Block_cipher_modes_of_operation">Block
         *      cipher mode of operation</a>
         *
         */
        public Builder setEncryptContent(String ivx, String secretKey) {
            _isEncrypted = true;

            // Set IV parameter
            try {
                _ivParameter = ivx.getBytes(UTF_8);
            }
            catch (UnsupportedEncodingException e) {
                Log.e("FinestStorageConfiguration", "UnsupportedEncodingException", e);
            }

            // Set secret key
            try {
				/*
				 * We generate random salt and then use 1000 iterations to
				 * initialize secret key factory which in-turn generates key.
				 */
                int iterationCount = 1000; // recommended by PKCS#5
                int keyLength = 128;

                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[16]; // keyLength / 8 = salt length
                random.nextBytes(salt);
                KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount, keyLength);
                SecretKeyFactory keyFactory = null;
                if (Build.VERSION.SDK_INT >= 19) {
                    // see:
                    // http://android-developers.blogspot.co.il/2013/12/changes-to-secretkeyfactory-api-in.html
                    // Use compatibility key factory -- only uses lower 8-bits
                    // of passphrase chars
                    keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1And8bit");
                }
                else {
                    // Traditional key factory. Will use lower 8-bits of
                    // passphrase chars on
                    // older Android versions (API level 18 and lower) and all
                    // available bits
                    // on KitKat and newer (API level 19 and higher).
                    keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                }
                byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();

                _secretKey = keyBytes;

            }
            catch (InvalidKeySpecException e) {
                Log.e("FinestStorageConfiguration", "InvalidKeySpecException", e);
            }
            catch (NoSuchAlgorithmException e) {
                Log.e("FinestStorageConfiguration", "NoSuchAlgorithmException", e);
            }

            return this;
        }

    }
}
