package bahar.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

import bahar.bilgi.Klavye;
import bahar.bilgi.Klavyeler;

public class GirisPaneli extends JPanel {

    private JTextField isim;
    private JTextField no;
    private JComboBox klavyeCombo;
    private JCheckBox klavyeGoster;
    private JCheckBox elGoster;
    private JCheckBox durumGoster;
    private JCheckBox silmeyeIzinVer;
    private JTextField dersField;
    private JLabel hataLbl;

    public GirisPaneli() {

        this.setLayout(new MigLayout("wrap 2"));

        JLabel resim = new JLabel(ComponentFactory.GIRIS_RESIM);

        this.add(resim, "span 1 2");
        this.add(bilgiler(), "grow");
        this.add(hataPanel());

    }


    private JPanel bilgiler() {
        JPanel jp = new JPanel(new MigLayout("wrap 3"));

        jp.add(ComponentFactory.boldLabel("Lutfen Asagidaki Bilgileri giriniz.."), "span 3");
        jp.add(new JPanel(), "wrap");

        JLabel isimLbl = ComponentFactory.label("Ad Soyad:");
        isim = ComponentFactory.textField();
        jp.add(isimLbl);
        jp.add(isim, "wrap");

        JLabel numaraLbl = ComponentFactory.label("Numara:");
        no = ComponentFactory.textField();
        jp.add(numaraLbl);
        jp.add(no, "wrap");

        jp.add(new JPanel(), "wrap");        
        JLabel klavye = ComponentFactory.label("Klavye:");
        klavyeCombo = new JComboBox(new String[]{"Turkce F", "Turkce Q", "Amerikan Q"});
        klavyeCombo.setFont(ComponentFactory.VERDANA);
        jp.add(klavye);
        jp.add(klavyeCombo, "wrap");


        jp.add(ComponentFactory.label("Ders:"));
        dersField = ComponentFactory.textField();
        jp.add(dersField);
        JButton btn = new JButton("..");
        btn.setFont(ComponentFactory.VERDANA);

        jp.add(btn);

        jp.add(new JPanel(), "wrap");
        jp.add(ComponentFactory.label("El resimlerini goster"));
        elGoster = new JCheckBox("", true);
        jp.add(elGoster, "wrap");

        jp.add(ComponentFactory.label("Durum penceresi goster"));
        durumGoster = new JCheckBox("", true);
        jp.add(durumGoster, "wrap");


        //jp.add(ComponentFactory.label("Klavye goster"));

        return jp;
    }

    private JPanel hataPanel() {
        hataLbl = ComponentFactory.boldLabel("");
        JPanel jp =  new JPanel(new MigLayout());
        jp.add(hataLbl, "grow");
        return jp;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // fall back to default.
        }

        JFrame f = new JFrame();
        f.setLayout(new MigLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GirisPaneli());
        f.pack();
        f.setVisible(true);
    }

    public Klavye getFromCombo() {
        switch (klavyeCombo.getSelectedIndex()) {
            case 0:
                return Klavyeler.turkceF();
            case 1:
                return Klavyeler.turkceQ();
            case 2:
                return Klavyeler.amerikanQ();
        }
        throw new IllegalArgumentException("Klavye bulunamadi!");
    }
}
