package bahar.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import bahar.bilgi.Klavye;
import bahar.bilgi.Klavyeler;
import bahar.bilgi.DersBilgisi;
import org.jmate.Systems;
import org.jmate.SimpleFileReader;
import org.jmate.Strings;
import org.jmate.IOs;

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

        JButton btnStart = new JButton("Teste Basla");
        btnStart.setFont(ComponentFactory.VERDANA);


        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateform()) {
                    new DersFrame(dersBilgisiUret());
                    hataLbl.setText("");
                    validate();
                }
            }
        });

        JButton btnTest = new JButton("Deneme Yap");
        btnTest.setFont(ComponentFactory.VERDANA);


        btnTest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //InputStream is = GirisPaneli.class.getResourceAsStream("/ornekler/ornek.txt");
                InputStream is = IOs.getClassPathResourceAsStream("/ornekler/ornek.txt");
                try {
                    String icerik = IOs.readAsString(IOs.getReader(is, "utf-8"));
                    DersBilgisi db = new DersBilgisi(icerik.replaceAll("[\n]", " "));
                  //  DersBilgisi db = new DersBilgisi("aaaaaa aaaaa aaaaa aaaaa aaaaa aaaa aaaaaaa aaaaaa aaaaaa aaaaa aaaaaa");
                    db.kullaniciAdi = "Test";
                    db.kullaniciNumarasi = "--";
                    flagDegerleriniBelirle(db);
                    db.klavye = getKlavye();
                    new DersFrame(db);

                    hataLbl.setText("");
                    validate();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        jp.add(btnStart);
        jp.add(btnTest);

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
                    yazi = Strings.whiteSpacesToSingleSpace(yazi.trim());

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
        durumGoster = new JCheckBox("", false);
        jp.add(durumGoster, "wrap");
        return jp;
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
        dersBilgisi.klavye = getKlavye();
        flagDegerleriniBelirle(dersBilgisi);
        return dersBilgisi;
    }

    private void flagDegerleriniBelirle(DersBilgisi dersBilgisi) {
        dersBilgisi.durumGoster = durumGoster.isSelected();
        dersBilgisi.elGoster = elGoster.isSelected();
    }

    public boolean validateform() {
        if (!Strings.hasText(isim.getText())) {
            hataLbl.setText("Ad-Soyad bilgisi eksik.");
            return false;
        }

        if(!isim.getText().matches("[a-zA-Z ]+")){
            hataLbl.setText("Ad soyad sadece harflerden olusabilir.");
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
        f.setLocation(200,150);
        f.add(new GirisPaneli());
        f.pack();
        f.setVisible(true);
    }
}
