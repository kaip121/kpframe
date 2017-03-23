package com.kplike.library.common.storage;

import android.content.Context;

/**
 * 单例类,提供所有可能的存储选项。<br>
 * <br>
 * <b>Permissions:</b>
 * <ul>
 * <li>android.permission.WRITE_EXTERNAL_STORAGE</li>
 * <li>android.permission.READ_EXTERNAL_STORAGE</li>
 * </ul>
 * Created by admin on 2017/3/23.
 */

public class FinestStorage {

    private static InternalStorage mInternalStorage = null;
    private static ExternalStorage mExternalStorage = null;

    private static FinestStorage mInstance = null;
    private static FinestStorageConfiguration mSimpleStorageConfiguration;

    private FinestStorage() {
        // set default configuration
        mSimpleStorageConfiguration = new FinestStorageConfiguration.Builder().build();

        mInternalStorage = new InternalStorage();
        mExternalStorage = new ExternalStorage();
    }

    private static FinestStorage init() {
        if (mInstance == null) {
            mInstance = new FinestStorage();
        }
        return mInstance;
    }

    /**
     * 存储的类型 <br>
     * 可能的选项:
     * <ul>
     * <li>{@link StorageType#INTERNAL}</li>
     * <li>{@link StorageType#EXTERNAL}</li>
     * </ul>
     *
     * @author sromku
     *
     */
    public enum StorageType {
        INTERNAL,
        EXTERNAL
    }

    /**
     * 内部存储。设备的文件和文件夹将被持久化。
     * 内部存储有利于储存安全的数据
     * <br>
     * <b>Important:
     * <ul>
     * <li>When the device is low on internal storage space, Android may delete
     * these cache files to recover space.</li>
     * <li>You should always maintain the cache files yourself and stay within a
     * reasonable limit of space consumed, such as 1MB.</li>
     * <li>When the user uninstalls your application, these files are removed.</li>
     * </b>
     * </ul>
     * <i>http://developer.android.com/guide/topics/data/data-storage.html#
     * filesInternal</i>
     *
     * @return {@link InternalStorage}
     *
     */
    public static InternalStorage getInternalStorage(Context context) {
        init();
        mInternalStorage.initActivity(context);
        return mInternalStorage;
    }

    /**
     * 获取外部存储器 <br>
     *
     * @return {@link ExternalStorage}
     */
    public static ExternalStorage getExternalStorage() {
        init();
        return mExternalStorage;
    }

    /**
     * 检查而外部存储是否可写。
     *
     * @return
     */
    public static boolean isExternalStorageWritable() {
        init();
        return mExternalStorage.isWritable();
    }

    public static FinestStorageConfiguration getConfiguration() {
        return mSimpleStorageConfiguration;
    }

    /**
     * 设置和更新存储配置
     *
     * @param configuration
     */
    public static void updateConfiguration(FinestStorageConfiguration configuration) {
        if (mInstance == null) {
            throw new RuntimeException("First instantiate the Storage and then you can update the configuration");
        }
        mSimpleStorageConfiguration = configuration;
    }

    /**
     * 重置默认的配置
     */
    public static void resetConfiguration() {
        FinestStorageConfiguration configuration = new FinestStorageConfiguration.Builder().build();
        mSimpleStorageConfiguration = configuration;
    }
}
