package bahar.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class GirisPaneli extends JPanel {

    private JTextField isim;

    public GirisPaneli() {

        this.setLayout(new MigLayout());

        JLabel resim = new JLabel(ComponentFactory.PAPATYA_RESIM);

        this.add(resim);
        this.add(bilgiler(), "grow");

    }


    private JPanel bilgiler() {
        JPanel jp = new JPanel(new MigLayout("wrap 2"));
        JLabel isimLbl = ComponentFactory.label("Ad Soyad:");
        isim = ComponentFactory.textField();
        jp.add(isimLbl);
        jp.add(isim);
        return jp;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setLayout(new MigLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GirisPaneli());
        f.pack();
        f.setVisible(true);
    }
}
