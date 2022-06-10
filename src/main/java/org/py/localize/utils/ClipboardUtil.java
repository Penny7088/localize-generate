package org.py.localize.utils;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

public class ClipboardUtil {


    public static void copy(String contents) {
        try {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.put(DataFormat.PLAIN_TEXT, contents);
            clipboard.setContent(content);
        } catch (Exception exception) {
            System.out.println("copy fail = " + exception.getMessage());
        }
    }
}
