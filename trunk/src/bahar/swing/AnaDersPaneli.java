package bahar.swing;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import net.miginfocom.swing.MigLayout;
import bahar.bilgi.DersBilgisi;
import bahar.bilgi.DersOturumu;
import bahar.bilgi.Klavye;
import bahar.bilgi.Klavyeler;

public class AnaDersPaneli extends JPanel implements KeyListener {

    DersBilgisi dersBilgisi;
    DurumPaneli durumPaneli;
    ElPaneli elPaneli;
    DersOturumu dersOturumu;
    DersIcerikPaneli dersIcerikPaneli;
    Klavye klavye;

    boolean basladi = false;
    boolean durakladi = false;

    public AnaDersPaneli(DersBilgisi dersBilgisi, Klavye klavye) {
        this.dersBilgisi = dersBilgisi;
        this.klavye = klavye;

        MigLayout ml = new MigLayout();
        this.setLayout(ml);

        DurumPaneli durumPaneli = new DurumPaneli(dersBilgisi);
        this.dersOturumu = new DersOturumu(dersBilgisi, durumPaneli);

        dersIcerikPaneli = new DersIcerikPaneli(dersBilgisi, this.dersOturumu);
        this.add(dersIcerikPaneli);

        this.add(durumPaneli, "wrap");

        elPaneli = new ElPaneli();
        setParmakIsareti(dersIcerikPaneli.beklenenHarf());
        this.add(elPaneli);

        this.setFocusable(true);
        this.addKeyListener(this);
    }


    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setLayout(new MigLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new AnaDersPaneli(DersBilgisi.ornek(), Klavyeler.amerikanQ()));
        f.pack();
        f.setVisible(true);
    }

    public void setParmakIsareti(char c) {
        elPaneli.setParmakBilgisi(klavye.getPArmakBilgisi(c));
    }

    public void keyTyped(KeyEvent e) {
        if (klavye.harfYazilabilir(e.getKeyChar())) {
            if (!basladi) {
               basladi = true;
               dersOturumu.devamEt();
            }
            if(durakladi) {
                dersOturumu.devamEt();
                durakladi = false;
            }
            dersIcerikPaneli.karakterYaz(e.getKeyChar());
            setParmakIsareti(dersIcerikPaneli.beklenenHarf());

        } else {
            if(e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                if(!durakladi) {
                    System.out.println("Durakla..");
                    durakladi = true;
                    dersOturumu.durakla();
                } else {
                    System.out.println("Basla....");
                    dersOturumu.devamEt();
                    durakladi = false;
                }
            }
            dersIcerikPaneli.ozelKarakter(e);
        }
        validate();
    }

    public void keyPressed(KeyEvent e) {
        // gerek yok.
    }

    public void keyReleased(KeyEvent e) {
        // gerek yok
    }
}
