package utils;

import javax.swing.*;
import java.net.URL;

public class CreateIcon {
    public static ImageIcon getIconFromFile(String fileName) {
        URL iconUrl = CreateIcon.class.getResource("/imgs/" +fileName);
        assert iconUrl != null;
        ImageIcon imageIcon = new ImageIcon(iconUrl);
        return imageIcon;
    }
}
