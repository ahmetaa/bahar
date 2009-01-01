package bahar.arayuz;

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
    DersOturumu dersOturumu;
    DersIcerikPaneli dersIcerikPaneli;
    Klavye klavye;

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

        this.add(new ElPaneli());

        this.setFocusable(true);
        this.addKeyListener(this);

        dersOturumu.go();
    }


    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setLayout(new MigLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new AnaDersPaneli(DersBilgisi.ornek(), Klavyeler.amerikanQ()));
        f.pack();
        //f.setSize(850, 450);
        f.setVisible(true);
    }

    public void keyTyped(KeyEvent e) {
        if (klavye.karakterLegal(e.getKeyChar()))
            dersIcerikPaneli.karakterYaz(e.getKeyChar());
        else
            dersIcerikPaneli.ozelKarakter(e);
        validate();
    }

    public void keyPressed(KeyEvent e) {
        // gerek yok.
    }

    public void keyReleased(KeyEvent e) {
        // gerek yok
    }
}
