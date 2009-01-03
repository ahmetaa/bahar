package bahar.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import bahar.bilgi.Klavye;
import bahar.bilgi.Klavyeler;
import bahar.bilgi.DersBilgisi;
import org.jmate.Systems;
import org.jmate.SimpleFileReader;
import org.jmate.Strings;

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
    private JTextArea yaziAlani = new JTextArea();
    private String currentDir;

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

        currentDir = Systems.getUserHome().getAbsolutePath();
        btn.addActionListener(new FileButtonListener(this));

        jp.add(btn, "shrink");

        //       JScrollPane jsp = new JScrollPane(yaziAlani);
        //       jp.add(jsp, "span 3");

        jp.add(checkBoxPanel(), "span 3");

        JButton btnStart = new JButton("Basla");
        btnStart.setFont(ComponentFactory.VERDANA);


        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateform()) {
                    new DersFrame(dersBilgisiUret(), getKlavye());
                    hataLbl.setText("");
                    validate();
                }
            }
        });

        jp.add(btnStart,"span 3");

        return jp;
    }

    private class FileButtonListener implements ActionListener {

        private Component component;

        private FileButtonListener(Component component) {
            this.component = component;
        }

        public void actionPerformed(ActionEvent e) {

            JFileChooser c;
            if (currentDir == null)
                c = new JFileChooser();
            else
                c = new JFileChooser(currentDir);

            int rVal = c.showOpenDialog(component);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File f = c.getSelectedFile();
                    String yazi = new SimpleFileReader(f).asString();
                    dersField.setText(f.getName());
                    yaziAlani.setColumns(30);
                    yaziAlani.setRows(5);
                    yaziAlani.setWrapStyleWord(true);
                    yaziAlani.setText(yazi);
                    currentDir = f.getAbsolutePath();
                    component.validate();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }

        public String getCurrentDir() {
            return currentDir;
        }
    }

    private JPanel hataPanel() {
        hataLbl = ComponentFactory.boldLabel("");
        hataLbl.setForeground(Color.RED);
        JPanel jp = new JPanel(new MigLayout());
        jp.add(hataLbl, "grow");
        return jp;
    }

    private JPanel checkBoxPanel() {
        JPanel jp = new JPanel(new MigLayout("wrap 2"));
        jp.add(new JPanel(), "wrap");
        jp.add(ComponentFactory.label("El resimlerini goster"));
        elGoster = new JCheckBox("", true);
        jp.add(elGoster, "wrap");

        jp.add(ComponentFactory.label("Durum penceresi goster"));
        durumGoster = new JCheckBox("", true);
        jp.add(durumGoster, "wrap");
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

    public Klavye getKlavye() {
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

    public DersBilgisi dersBilgisiUret() {
        DersBilgisi dersBilgisi = new DersBilgisi(yaziAlani.getText());
        dersBilgisi.kullaniciAdi = isim.getText().trim();
        dersBilgisi.kullaniciNumarasi = no.getText().trim();
        dersBilgisi.hataGoster = durumGoster.isSelected();
        dersBilgisi.elGoster = elGoster.isSelected();
        return dersBilgisi;
    }

    public boolean validateform() {
        if (!Strings.hasText(isim.getText())) {
            hataLbl.setText("Ad-Soyad bilgisi eksik.");
            return false;
        }
        if (!Strings.hasText(no.getText())) {
            hataLbl.setText("Numara bilgisi eksik.");
            return false;
        }
        if (!Strings.hasText(yaziAlani.getText())) {
            hataLbl.setText("Ders bilgisi eksik.");
            return false;
        }
        return true;
    }
}
