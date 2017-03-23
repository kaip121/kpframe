package com.kplike.library.common.storage;

import android.graphics.Bitmap;

import com.kplike.library.common.storage.helpers.OrderType;
import com.kplike.library.common.storage.helpers.SizeUnit;

import java.io.File;
import java.util.List;

/**
 * File文件CRUD 方法接口
 * Created by admin on 2017/3/23.
 */

public interface Storage {

    /**
     * 获取存储器对象
     * @return
     */
    FinestStorage.StorageType getStorageType();

    /**
     * 根据路径创建目录。
     * @param name
     * @return
     */
    boolean createDirectory(String name);

    /**
     * 根据路径创建目录。<br/>
     * 根据<code>override</code>是否覆盖已存在的目录
     * @param name
     * @param override
     * @return
     */
    boolean createDirectory(String name, boolean override);


    /**
     * 删除目录和所有的子内容。
     * @param name
     * @return
     */
    boolean deleteDirectory(String name);


    /**
     * 检查目录是否已经存在。
     * @param name
     * @return
     */
    boolean isDirectoryExists(String name);


    /**
     * 创建文件
     * @param name  文件路径
     * @return
     */
    boolean createFile(String name);

    /**
     * 创建文件
     * @param directoryName 文件目录
     * @param fileName  文件名称
     * @return
     */
    boolean createFile(String directoryName, String fileName);

    /**
     * 创建文件
     * @param directoryName 文件目录
     * @param fileName  文件名称
     * @param content   文件内容
     * @return
     */
    boolean createFile(String directoryName, String fileName, String content);


    /**
     * 创建文件名字和storable存储器的内容
     * @param directoryName
     * @param fileName
     * @param storable
     * @return
     */
    boolean createFile(String directoryName, String fileName, Storable storable);

    /**
     * 创建文件名字和bitmap的内容
     * @param directoryName
     * @param fileName
     * @param bitmap
     * @return
     */
    boolean createFile(String directoryName, String fileName, Bitmap bitmap);

    /**
     * 创建文件名字和字节数组的内容
     * @param directoryName
     * @param fileName
     * @param content
     * @return
     */
    boolean createFile(String directoryName, String fileName, byte[] content);

    /**
     * 删除文件
     * @param directoryName
     * @param fileName
     * @return
     */
    boolean deleteFile(String directoryName, String fileName);

    /**
     * 删除文件
     * @param name 文件路径
     * @return
     */
    boolean deleteFile(String name);

    /***
     * 判断文件是否存在
     * @param directoryName
     * @param fileName
     * @return
     */
    boolean isFileExist(String directoryName, String fileName);

    /**
     * 检查文件是否已经存在。
     * @param path
     * @return
     */
    boolean isFileExists(String path);

    /**
     * 读取文件
     * @param directoryName
     * @param fileName
     * @return
     */
    byte[] readFile(String directoryName, String fileName);


    /**
     * 读取文件字节
     * @param name  文件路径
     * @return
     */
    byte[] readFile(String name);

    /**
     * 读取文件字符串
     * @param directoryName
     * @param fileName
     * @return
     */
    String readTextFile(String directoryName, String fileName);

    /**
     * 读取文件字符串
     * @param name 文件路径
     * @return
     */
    String readTextFile(String name);

    /**
     * 将内容附加到现有文件
     * @param directoryName
     * @param fileName
     * @param content
     */
    void appendFile(String directoryName, String fileName, String content);

    /**
     * 将内容附加到现有文件
     * @param directoryName
     * @param fileName
     * @param bytes
     */
    void appendFile(String directoryName, String fileName, byte[] bytes);

    /**
     * 获取目录下所有嵌套的列表文件
     * @param directoryName
     * @return
     */
    List<File> getNestedFiles(String directoryName);

    /**
     * 获取该目录下的所有文件和目录匹配正则表达式
     * @param directoryName
     * @param matchRegex
     * @return
     */
    List<File> getFiles(String directoryName, String matchRegex);

    /**
     * 从目录命令获取文件
     * @param directoryName
     * @param orderType
     * @return
     */
    List<File> getFiles(String directoryName, OrderType orderType);

    /**
     * 根据名称获取文件
     * @param name
     * @return
     */
    File getFile(String name);

    /**
     * 获取文件
     * @param directoryName
     * @param fileName
     * @return
     */
    File getFile(String directoryName, String fileName);

    /**
     * 重命名文件
     * @param file
     * @param newName
     */
    void rename(File file, String newName);

    /**
     * 获取文件单位的大小
     * @param file
     * @param unit
     * @return
     */
    double getSize(File file, SizeUnit unit);

    /**
     * 获取磁盘上的自由空间
     * @param sizeUnit
     * @return
     */
    long getFreeSpace(SizeUnit sizeUnit);

    /**
     * 获取已使用磁盘的空间
     * @param sizeUnit
     * @return
     */
    long getUsedSpace(SizeUnit sizeUnit);

    /**
     * 复制文件到另一个位置
     * @param file
     * @param directoryName
     * @param fileName
     */
    void copy(File file, String directoryName, String fileName);

    /**
     * 复制文件到另一个位置
     * @param file
     * @param directoryName
     */
    void copy(File file, String directoryName);

    /**
     * 复制文件
     * @param oldFile
     * @param newFile
     */
    void copy(File oldFile, File newFile);

    /**
     * 复制文件到另一个位置
     * @param oldPath
     * @param directoryName
     */
    void copy(String oldPath, String directoryName);

    /**
     * 复制文件到另一个位置，并修改文件名称
     * @param oldPath
     * @param directoryName
     * @param fileName
     */
    void copy(String oldPath, String directoryName, String fileName);

    /**
     * 将文件移动到另一个位置,并修改文件名称
     * @param file
     * @param directoryName
     * @param fileName
     */
    void move(File file, String directoryName, String fileName);

    /**
     * 将文件移动到另一个位置
     * @param file
     * @param directoryName
     */
    void move(File file, String directoryName);

    /**
     * 将文件移动到另一个位置,并修改文件名称
     * @param oldPath
     * @param directoryName
     * @param fileName
     */
    void move(String oldPath, String directoryName, String fileName);

    /**
     * 将文件移动到另一个位置
     * @param oldPath
     * @param directoryName
     */
    void move(String oldPath, String directoryName);


}
