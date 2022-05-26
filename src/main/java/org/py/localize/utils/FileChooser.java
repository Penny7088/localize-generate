package org.py.localize.utils;

import com.sun.javafx.stage.StageHelper;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileFilter;

/**
 * Created by Administrator on 2016/3/28.
 */
public class FileChooser {
    public static final String[] fileExtNames = {".xls", ".xlsx"};

    public static File getFileFromFileChooser() {
        // 创建文件选择器
        final javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        // 设置当前目录
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("所有Excel", "*.xls", "*.xlsx"),
                new javafx.stage.FileChooser.ExtensionFilter("MS-Word 2003 文件(*.xls)", "*.xls"),
                new javafx.stage.FileChooser.ExtensionFilter("MS-Excel 2007 文件(*.xlsx)", "*.xlsx")
        );
        return fileChooser.showOpenDialog(StageHelper.getStages().get(0));
    }

    public static String getDirChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("View dir");
        File showDialog = directoryChooser.showDialog(new Stage());
        if (showDialog != null) {
            return showDialog.getAbsolutePath();
        }
        return "";
    }
}
