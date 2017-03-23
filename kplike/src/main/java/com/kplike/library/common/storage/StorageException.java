package com.kplike.library.common.storage;

/**
 * Created by admin on 2017/3/23.
 */

public class StorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StorageException(String msg) {
        super(msg);
    }

    public StorageException(Throwable t) {
        super(t);
    }
}
