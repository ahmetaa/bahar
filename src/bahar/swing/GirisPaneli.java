package bahar.swing;

import bahar.bilgi.DersBilgiHatasi;
import bahar.bilgi.DersBilgisi;
import bahar.i18n.I18n;
import net.miginfocom.swing.MigLayout;
import org.jcaki.Files;
import org.jcaki.IOs;
import org.jcaki.Strings;
import org.jcaki.Systems;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GirisPaneli extends JPanel {

    private JTextField isim;
    private JTextField no;
    private JTextField dersField;
    private JLabel hataLbl;
    private File currentDir;

    private DersBilgisi dersBilgisi = null;

    public GirisPaneli() {

        this.setLayout(new MigLayout("wrap 2"));

        JLabel resim = new JLabel(ComponentFactory.GIRIS_RESIM);

        this.add(resim, "span 1 2");
        this.add(bilgiler(), "grow");
        this.add(hataPanel());

    }

    private JPanel bilgiler() {
        JPanel jp = new JPanel(new MigLayout("wrap 3"));

        jp.add(ComponentFactory.boldLabel(I18n.getText("bilgi.aciklama")), "span 3");
        jp.add(new JPanel(), "wrap");

        JLabel isimLbl = ComponentFactory.label("Ad Soyad:");
        isim = ComponentFactory.textField();
        jp.add(isimLbl);
        jp.add(isim, "wrap");

        JLabel numaraLbl = ComponentFactory.label("Numara:");
        no = ComponentFactory.textField();
        jp.add(numaraLbl);
        jp.add(no, "wrap");

        currentDir = new File(".");

        jp.add(ComponentFactory.label(I18n.getText("bilgi.dosya")));
        dersField = ComponentFactory.textField();
        File firstBhr = getFirstBhrFile();
        if (firstBhr != null)
            dersField.setText(firstBhr.getName());
        dersField.setEditable(false);
        jp.add(dersField);
        JButton btn = new JButton("..");
        btn.setFont(ComponentFactory.VERDANA);

        btn.addActionListener(new FileButtonListener(this));

        jp.add(btn, "shrink");

        JButton btnStart = new JButton(I18n.getText("bilgi.basla"));
        btnStart.setFont(ComponentFactory.VERDANA);


        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateform()) {
                    try {
                        dersBilgisi = new DersBilgisi(new File(currentDir, dersField.getText()));
                        setStudentData();
                        if (!dersBilgisi.klavye.yaziYazilabilir(dersBilgisi.icerik)) {
                            hataLbl.setText("Calisma yazilamaz karakter iceriyor.");
                            return;
                        }
                        new DersFrame(dersBilgisi);
                        hataLbl.setText("");
                        validate();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        hata("Dosya erisiminde hata olustu.");
                    }
                }
            }
        });

        JButton btnTest = new JButton("Deneme Yap");
        btnTest.setFont(ComponentFactory.VERDANA);

        btnTest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testAction();
            }
        });

        jp.add(btnStart);
        jp.add(btnTest);

        return jp;
    }


    private void testAction() {
        File out = new File(Systems.getJavaIoTmpDir(), "bahar_test.bhr");
        try {
            IOs.copy(
                    IOs.getClassPathResourceAsStream("/ornekler/test.bhr"),
                    new FileOutputStream(out));

            this.dersBilgisi = new DersBilgisi(out);
            dersBilgisi.kullaniciAdi = "Test";
            dersBilgisi.kullaniciNumarasi = "--";
            new DersFrame(dersBilgisi);
            hataLbl.setText("");
            validate();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (DersBilgiHatasi hata) {
            hata(hata.getMessage());
        }
    }


    private void hata(String mesaj) {
        hataLbl.setText(mesaj);
    }


    private File getFirstBhrFile() {
        List<File> files = Files.crawlDirectory(currentDir, false, new Files.ExtensionFilter("bhr"));
        return files.size() > 0 ? files.get(0) : null;
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
                    dersBilgisi = new DersBilgisi(f);
                    setStudentData();
                    dersField.setText(f.getName());
                    currentDir = f;
                    component.validate();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (DersBilgiHatasi hata) {
                    hata(hata.getMessage());
                }
            }
        }


    }

    private void setStudentData() {
        dersBilgisi.kullaniciAdi = isim.getText().trim();
        dersBilgisi.kullaniciNumarasi = no.getText().trim();
    }


    private JPanel hataPanel() {
        hataLbl = ComponentFactory.boldLabel("");
        hataLbl.setForeground(Color.RED);
        JPanel jp = new JPanel(new MigLayout());
        jp.add(hataLbl, "grow");
        return jp;
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
        if (!Strings.hasText(dersField.getText())) {
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
        f.add(new GirisPaneli());
        f.pack();
        f.setLocation(ComponentFactory.getCenterPos(f.getWidth(), f.getHeight()));
        f.setVisible(true);
    }
}
