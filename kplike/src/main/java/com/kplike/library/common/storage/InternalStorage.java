package com.kplike.library.common.storage;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;

/**
 * 内部存储器
 * Created by admin on 2017/3/23.
 */

public class InternalStorage extends AbstractDiskStorage{
    private Context mContext;

    InternalStorage(){
        super();
    }

    /**
     * 初始化Activity使用外部存储文件的方法
     * @param context
     */
    void initActivity(Context context){
        this.mContext = context;
    }

    @Override
    public FinestStorage.StorageType getStorageType() {
        return null;
    }

    @Override
    public boolean createDirectory(String name) {
        File dir = mContext.getDir(name, Context.MODE_PRIVATE);
        if (dir.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 创建内部存储的文件
     * @param name
     * @param content
     * @return
     */
    public boolean createFile(String name, String content) {
        try {
            byte[] bytes = content.getBytes();
			/*
			 * Check if needs to be encrypted. If yes, then encrypt it.
			 */
            if (getConfiguration().isEncrypted()) {
                bytes = encrypt(bytes, Cipher.ENCRYPT_MODE);
            }

            FileOutputStream fos = mContext.openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(bytes);
            fos.close();
            return true;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create private file on internal storage", e);
        }
    }

    /**
     * 从内部存储器读取文件。
     * @param name
     * @return
     */
    public byte[] readFile(String name) {
        try {
            FileInputStream stream = mContext.openFileInput(name);
            byte[] out = readFile(stream);
            return out;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create private file on internal storage", e);
        }
    }


    @Override
    protected String buildAbsolutePath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 构建路径目录的内部存储位置。
     * @param directoryName
     * @return
     */
    @Override
    protected String buildPath(String directoryName) {
        return mContext.getDir(directoryName, Context.MODE_PRIVATE).getAbsolutePath();
    }


    /**
     * 构建文件夹+文件内部的存储位置。
     * @param directoryName
     * @param fileName
     * @return
     */
    @Override
    protected String buildPath(String directoryName, String fileName) {
        String path = mContext.getDir(directoryName, Context.MODE_PRIVATE).getAbsolutePath();
        path = path + File.separator + fileName;
        return path;
    }


}
