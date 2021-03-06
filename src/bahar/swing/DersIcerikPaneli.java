package bahar.swing;

import bahar.bilgi.DersBilgisi;
import bahar.bilgi.DersOturumu;
import bahar.bilgi.SatirDinleyici;
import net.miginfocom.swing.MigLayout;
import org.bushe.swing.event.EventBus;
import org.jcaki.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class DersIcerikPaneli extends JPanel implements SatirDinleyici {

    private List<SatirPaneli> satirlar = new ArrayList<SatirPaneli>();
    private int currentLine = 0;

    private final DersBilgisi dersBilgisi;
    private final DersOturumu oturum;

    public DersIcerikPaneli(DersBilgisi db, DersOturumu oturum) {

        this.oturum = oturum;
        this.dersBilgisi = db;

        this.setLayout(new MigLayout("wrap 1"));

        List<String> lines = db.generateLines();
        for (String line : lines) {
            SatirPaneli tl = new SatirPaneli(line, this);
            tl.goInactive();
            satirlar.add(tl);
            this.add(tl, "grow");
        }
        current().goActive();
    }

    public String getYazilan() {
        String y = "";
        for (SatirPaneli satirPaneli : satirlar) {
            y += satirPaneli.yazilan;
        }
        return y.trim();
    }

    public char beklenenHarf() {
        if (!yaziSonunaGelindi())
            return current().beklenenHarf();
        return '\u0000';
    }

    private SatirPaneli current() {
        return satirlar.get(currentLine);
    }

    public void ozelKarakter(KeyEvent e) {
        if (yaziSonunaGelindi())
            return;

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == '\b') {
            if (dersBilgisi.silmeyeIzinVer)
                current().backSpace();
        }
        e.consume();
    }

    public void karakterYaz(char c) {
        if (yaziSonunaGelindi())
            return;
        current().type(c);
    }

    public boolean yaziSonunaGelindi() {
        return currentLine == satirlar.size() - 1 && current().satirBitti();
    }

    public void satirSonu() {

        current().goInactive();
        current().eraseCursor();

        // ders sonuna gelinmisse.
        if (yaziSonunaGelindi()) {
            oturum.durakla();
            EventBus.publish(new DersEvent(true));
        } else {
            currentLine++;
            current().goActive();
        }
    }

    public void hataYapildi(char hata, boolean hataYazildi) {
        if (hataYazildi) {
            oturum.yazilanArttir();
            oturum.gorunenHataArttir();
        }
        oturum.tumHataArttir();
    }

    public void karakterYazildi(char c) {
        oturum.yazilanArttir();
    }

    class SatirPaneli extends JPanel {

        private JLabel yazilacakSatir = new JLabel();
        private JLabel yazilanSatir = new JLabel();
        //private JPanel yazilanSatirPanel = new JPanel(new MigLayout());
        //private JPanel yazilacakSatirPanel = new JPanel(new MigLayout());
        String yazilan = "";
        final String beklenenString;
        String bicimliYazilan = "";
        int cursor = 0;
        boolean lastWasError = false;
        final SatirDinleyici satirDinleyici;

        public SatirPaneli(String beklenenString, SatirDinleyici satirDinleyici) {

            this.satirDinleyici = satirDinleyici;

            this.setLayout(new MigLayout("wrap 1"));

            this.beklenenString = beklenenString;

            yazilacakSatir.setText(beklenenString);
            yazilacakSatir.setFont(new Font("Lucida Console", Font.PLAIN, 16));
            yazilacakSatir.setForeground(Color.DARK_GRAY);
            yazilanSatir.setFont(new Font("Lucida Console", Font.PLAIN, 16));
            yazilanSatir.setText(Strings.repeat(" ", beklenenString.length()));
            yazilanSatir.setForeground(Color.BLACK);

            //yazilacakSatirPanel.add(yazilacakSatir, "grow");
            //yazilacakSatirPanel.setBackground(Color.GRAY);
            //yazilanSatirPanel.add(yazilanSatir, "grow");
            //yazilanSatirPanel.setBackground(Color.GRAY);

            this.add(yazilacakSatir, "grow");
            this.add(yazilanSatir, "grow");
        }

        public void goActive() {
            this.setBackground(Color.WHITE);
            repaint();
        }

        public void goInactive() {
            this.setBackground(Color.LIGHT_GRAY);
            repaint();
        }

        public void backSpace() {
            if (yazilan.length() > 0) {
                yazilan = yazilan.substring(0, yazilan.length() - 1);
                bicimliYazilan = bicimliYazilan.substring(0, bicimliYazilan.length() - 1);
            }
            render();
        }

        public void type(char ke) {

            char expectedChr = beklenenString.charAt(cursor);

            // eger son yazilan karakter hatali ise tekrar yazmaya musade etme.
            if (ke != expectedChr && lastWasError && !dersBilgisi.ardisilHataGoster) {
                satirDinleyici.hataYapildi(ke, false);
                return;
            }

            yazilan += ke;
            cursor++;

            if (ke != expectedChr) {
                satirDinleyici.hataYapildi(ke, true);
                bicimliYazilan += '\u0123';
                lastWasError = true;
            } else {
                lastWasError = false;
                satirDinleyici.karakterYazildi(ke);
            }
            bicimliYazilan += ke;
            render();

            if (yazilan.length() == beklenenString.length())
                satirDinleyici.satirSonu();
        }


        private void render() {
            yazilanSatir.setText(format(splitText(bicimliYazilan)));
        }

        /**
         * format is: "hello #qworld #whow#a are you"
         *
         * @param cnt
         * @return
         */
        java.util.List<TextLayoutInfo> splitText(String cnt) {
            java.util.List<TextLayoutInfo> list = new ArrayList<TextLayoutInfo>();
            String[] splitz = cnt.split("\u0123");
            if (splitz.length < 2) {
                list.add(new TextLayoutInfo(cnt, false));
                return list;
            }
            if (splitz[0].length() > 0)
                list.add(new TextLayoutInfo(splitz[0], false));
            for (int i = 1; i < splitz.length; i++) {
                String s = splitz[i];
                String wrong = s.substring(0, 1);
                if (wrong.equals(" "))
                    wrong = "\u25a1"; // bosluk yerine yanlis ise bos kare koy.
                list.add(new TextLayoutInfo(wrong, true));
                String correct = s.substring(1, s.length());
                list.add(new TextLayoutInfo(correct, false));
            }
            return list;
        }

        String format(java.util.List<TextLayoutInfo> list) {
            StringBuilder sb = new StringBuilder("<HTML>");
            for (TextLayoutInfo textLayoutInfo : list) {
                if (textLayoutInfo.wrong) {
                    sb.append("<FONT COLOR=RED>").append(textLayoutInfo.s).append("</FONT>");
                } else {
                    sb.append(textLayoutInfo.s);
                }
            }
            return sb.append("_").toString();
        }

        public boolean satirBitti() {
            return yazilan.length() == beklenenString.length();
        }

        public void eraseCursor() {
            String icerik = format(splitText(bicimliYazilan));
            if (yazilan.length() > 0)
                this.yazilanSatir.setText(icerik.substring(0, icerik.length() - 1));
        }

        public char beklenenHarf() {
            return beklenenString.charAt(cursor);
        }

        class TextLayoutInfo {
            String s;
            boolean wrong = false;

            TextLayoutInfo(String s, boolean wrong) {
                this.s = s;
                this.wrong = wrong;
            }
        }
    }
}



