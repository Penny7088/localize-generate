package org.py.localize.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO关闭工具类
 *
 * @author : zhangfeng
 * @date : 2020/11/03
 */
class FileCloseUtils {
    public static final String TAG = "CloseUtils";

    public static void closeIO(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeIO(Closeable closeableA, Closeable closeableB) {
        try {
            if (closeableA != null) {
                closeableA.close();
            }

            if (closeableB != null) {
                closeableB.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
