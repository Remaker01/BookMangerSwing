package utils;

import javax.swing.*;

public class EmptyUtils {
    public static boolean isEmpty(JTextField textField) {
        // TODO: 0103
        /*ʵ��isEmpty�����Ĺ���
        ��textField��ֵȡ���� * ���ֵΪnull�����ǿմ����򷵻�true�����򷵻�false
        */
        String str = textField.getText();
        return "".equals(str) || str == null;
    }
}

