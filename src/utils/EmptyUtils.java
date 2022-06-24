package utils;

import javax.swing.*;

public class EmptyUtils {
    public static boolean isEmpty(JTextField textField) {
        // TODO: 0103
        /*实现isEmpty方法的功能
        将textField的值取出来 * 如果值为null或者是空串，则返回true，否则返回false
        */
        String str = textField.getText();
        return "".equals(str) || str == null;
    }
}

