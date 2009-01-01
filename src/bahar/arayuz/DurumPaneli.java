package bahar.arayuz;

import bahar.bilgi.DersBilgisi;
import bahar.bilgi.OturumDinleyici;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class DurumPaneli extends JPanel implements OturumDinleyici {

    JLabel sureLbl;
    JLabel yazilanSayisiLbl;
    JLabel hataSayisiLbl;
    JLabel hizLbl;

    public DurumPaneli(DersBilgisi dersBilgisi) {

        this.setBackground(Color.white);
        this.setLayout(new MigLayout("wrap 2"));

        // ad
        this.add(lbl(dersBilgisi.kullaniciAdi, 20), "span 2");

        // sureLbl
        this.add(lbl("Sure ", 16));
        sureLbl = lbl("00:00", 16);

        this.add(sureLbl);

        // toplam
        this.add(lbl("Toplam:", 16));
        this.add(lbl(String.valueOf(dersBilgisi.harfSayisi), 16));

        // yazilan
        this.add(lbl("Yazilan:", 16));
        yazilanSayisiLbl = lbl("0", 16);
        this.add(yazilanSayisiLbl);


        // hata sayisi
        this.add(lbl("Hata:", 16));
        hataSayisiLbl = lbl("0", 16);
        this.add(hataSayisiLbl);

        // hizLbl
        this.add(lbl("Hiz (harf/dk):", 16));
        hizLbl = lbl("--", 16);
        this.add(hizLbl);

    }


    private JLabel lbl(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, size));
        return label;
    }


    public void saniyeArtti(int sn, String hiz) {
        sureLbl.setText(sureFormatla(sn));
        this.hizLbl.setText(hiz);
        validate();
    }

    public void harfYazildi(int harfSayisi, int hataSayisi, String hiz) {
        this.hataSayisiLbl.setText(String.valueOf(hataSayisi));
        this.yazilanSayisiLbl.setText(String.valueOf(harfSayisi));
        this.hizLbl.setText(hiz);
        validate();
    }


    private String sureFormatla(int sn) {
        StringBuilder sb = new StringBuilder();
        int saniyeler = sn % 60;
        int dakikalar = sn / 60;

        if (dakikalar < 10) {
            sb.append("0").append(dakikalar);
        } else sb.append(dakikalar);
        sb.append(":");
        if (saniyeler < 10) {
            sb.append("0").append(saniyeler);
        } else sb.append(saniyeler);
        return sb.toString();
    }
}
