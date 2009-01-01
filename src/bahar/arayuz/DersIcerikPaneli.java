package bahar.arayuz;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import org.jmate.Strings;
import bahar.bilgi.DersBilgisi;
import bahar.bilgi.DersOturumu;
import bahar.bilgi.SatirDinleyici;


public class DersIcerikPaneli extends JPanel implements SatirDinleyici {

    final String icerik;
    List<SatirPaneli> satirlar = new ArrayList<SatirPaneli>();
    int currentLine = 0;

    final DersBilgisi dersBilgisi;
    final DersOturumu oturum;

    boolean yaziSonunaErisildi = false;

    public DersIcerikPaneli(DersBilgisi db, DersOturumu oturum) {

        this.oturum = oturum;
        this.dersBilgisi = db;
        this.icerik = db.icerik;

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

    public char beklenenHarf() {
        return current().beklenenHarf();
    }

    private SatirPaneli current() {
        return satirlar.get(currentLine);
    }

    public void ozelKarakter(KeyEvent e) {
        if (yaziSonunaErisildi)
            return;

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == '\b') {
            if (dersBilgisi.silmeyeIzinVer)
                current().backSpace();
        }
        e.consume();
    }

    public boolean yaziSonunaErisildi() {
        return yaziSonunaErisildi;
    }

    public void karakterYaz(char c) {
        if (yaziSonunaErisildi)
            return;
        current().type(c);
    }

    public void satirSonu() {
        current().goInactive();
        current().eraseCursor();
        currentLine++;
        if (currentLine > satirlar.size()) {
            yaziSonunaErisildi = true;
            return;
        }
        current().goActive();
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
        boolean lastWasError = true;
        final SatirDinleyici satirDinleyici;

        public SatirPaneli(String beklenenString, SatirDinleyici satirDinleyici) {

            this.satirDinleyici = satirDinleyici;

            this.setLayout(new MigLayout("wrap 1"));

            this.beklenenString = beklenenString;

            yazilacakSatir.setText(beklenenString);
            yazilacakSatir.setFont(new Font("Lucida Console", Font.PLAIN, 15));
            yazilacakSatir.setForeground(Color.DARK_GRAY);
            yazilanSatir.setFont(new Font("Lucida Console", Font.PLAIN, 15));
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
            if (ke != expectedChr && lastWasError) {
                satirDinleyici.hataYapildi(ke, false);
                return;
            }

            yazilan += ke;
            cursor++;

            if (ke != expectedChr) {
                satirDinleyici.hataYapildi(ke, true);
                bicimliYazilan += '#';
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
            String[] splitz = cnt.split("#");
            if (splitz.length == 1) {
                list.add(new TextLayoutInfo(cnt, false));
                return list;
            }
            //System.out.println(Arrays.toString(splitz));
            int i = 0;
            for (String s : splitz) {

                if (s.length() > 0 && i == 0) {
                    i++;
                    list.add(new TextLayoutInfo(s, false));
                    continue;
                }

                if (s.length() > 0) {
                    String wrong = s.substring(0, 1);
                    if (wrong.equals(" "))
                        wrong = "\u25a1"; // bosluk yerine yanlis ise bos kare koy.
                    list.add(new TextLayoutInfo(wrong, true));
                }
                if (s.length() > 1) {
                    String correct = s.substring(1, s.length());
                    list.add(new TextLayoutInfo(correct, false));
                }
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

        public void eraseCursor() {
            if (yazilan.length() > 0)
                this.yazilanSatir.setText(this.yazilan.substring(0, yazilan.length() - 1));
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



