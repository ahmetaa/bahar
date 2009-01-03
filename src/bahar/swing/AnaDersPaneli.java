package bahar.swing;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import net.miginfocom.swing.MigLayout;
import bahar.bilgi.*;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.EventBus;

public class AnaDersPaneli extends JPanel implements KeyListener, IcerikDinleyici {

    DersBilgisi dersBilgisi;
    DurumPaneli durumPaneli;
    ElPaneli elPaneli;
    DersOturumu dersOturumu;
    DersIcerikPaneli dersIcerikPaneli;
    Klavye klavye;
    StatusLabel statusLabel = new StatusLabel();

    boolean basladi = false;
    boolean durakladi = false;

    public AnaDersPaneli(DersBilgisi dersBilgisi, Klavye klavye) {
        this.dersBilgisi = dersBilgisi;
        this.klavye = klavye;

        MigLayout ml = new MigLayout("", "[center][center]", "[center][center]");
        this.setLayout(ml);

        durumPaneli = new DurumPaneli(dersBilgisi);

        dersOturumu = new DersOturumu(dersBilgisi, durumPaneli);

        dersIcerikPaneli = new DersIcerikPaneli(dersBilgisi, this.dersOturumu);

        if (dersBilgisi.elGoster)
            this.add(dersIcerikPaneli);
        else
            this.add(dersIcerikPaneli, "wrap");

        if (dersBilgisi.elGoster)
            this.add(durumPaneli, "wrap");

        elPaneli = new ElPaneli();
        setParmakIsareti(dersIcerikPaneli.beklenenHarf());

        if (dersBilgisi.hataGoster)
            this.add(elPaneli, "wrap");

        this.add(statusLabel," align left");

        this.setFocusable(true);
        this.addKeyListener(this);

        EventBus.publish(new StatusEvent("Bir tus basinca oturum baslayacak. Duraklama icin ESC tusunu kullanin."));
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
            if (durakladi) {
                dersOturumu.devamEt();
                durakladi = false;
            }
            dersIcerikPaneli.karakterYaz(e.getKeyChar());
            setParmakIsareti(dersIcerikPaneli.beklenenHarf());

        } else {
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                if (!durakladi) {
                    EventBus.publish(new StatusEvent("Oturum Durakladi.. ESC tusu ile devam edebilirsiniz."));
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

    public void dersBitti() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dersDurakladi() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dersYenidenBasladi() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dersBasladi() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
