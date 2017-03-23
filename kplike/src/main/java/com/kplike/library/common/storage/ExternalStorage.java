package com.kplike.library.common.storage;

import android.os.Environment;

import java.io.File;

/**
 * 外部存储器,存储在外部的存储(SD)卡
 */
public class ExternalStorage extends AbstractDiskStorage {

	ExternalStorage() {
		super();
	}

	@Override
	public FinestStorage.StorageType getStorageType() {
		return FinestStorage.StorageType.EXTERNAL;
	}

	/**
	 * 检查外部存储器可读和写。 <br>
	 */
	public boolean isWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	@Override
	protected String buildAbsolutePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 构建路径的文件夹或文件在外部存储位置。
	 * 
	 * @param name
	 * @return
	 */
	protected String buildPath(String name) {
		String path = buildAbsolutePath();
		path = path + File.separator + name;
		return path;
	}

	/**
	 * 构建文件夹+文件在外部存储位置。
	 *
	 * @param directoryName
	 * @param fileName
	 * @return
	 */
	protected String buildPath(String directoryName, String fileName) {
		String path = buildAbsolutePath();
		path = path + File.separator + directoryName + File.separator + fileName;
		return path;
	}

}