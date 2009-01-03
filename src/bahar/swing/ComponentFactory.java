package bahar.swing;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

public class ComponentFactory {

    public static final Icon GIRIS_RESIM = getIcon("giris_resmi.jpg");

    public static Icon getIcon(String fileName) {
        return new ImageIcon(ComponentFactory.class.getResource("/resimler/" + fileName));
    }

    public static BufferedImage getImageResource(String imageFile) {
        try {
            return ImageIO.read(ComponentFactory.class.getResource(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Font VERDANA = new Font("Verdana", Font.PLAIN, 14);
    public static Font VERDANA_BOLD = new Font("Verdana", Font.BOLD, 14);

    public static JLabel label(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, size));
        return label;
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(VERDANA);
        return label;
    }

    public static JLabel boldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(VERDANA_BOLD);
        return label;
    }

    public static JLabel iconLabel(String icon) {
        return new JLabel(getIcon(icon));
    }

    public static JTextField textField() {
        JTextField tf = new JTextField(20);
        tf.setFont(VERDANA);
        return tf;
    }

    public static JTextField textField(int size) {
        JTextField tf = new JTextField(size);
        tf.setFont(VERDANA);
        return tf;
    }

    public static JLabel fixedLengthLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Luicida Console", Font.PLAIN, size));
        return label;
    }

}
