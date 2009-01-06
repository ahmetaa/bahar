package bahar.swing;

import bahar.bilgi.DersBilgisi;
import bahar.bilgi.DersOturumu;
import bahar.bilgi.Klavye;
import net.miginfocom.swing.MigLayout;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class AnaDersPaneli extends JPanel implements KeyListener {

    DersBilgisi dersBilgisi;
    DurumPaneli durumPaneli;
    ElPaneli elPaneli;
    DersOturumu dersOturumu;
    DersIcerikPaneli dersIcerikPaneli;
    Klavye klavye;
    StatusLabel statusLabel = new StatusLabel();

    boolean basladi = false;
    boolean durakladi = false;

    public AnaDersPaneli(DersBilgisi dersBilgisi) {

        // Eventbus mekanizmasina bu sinifi ekle.
        AnnotationProcessor.process(this);

        this.dersBilgisi = dersBilgisi;
        this.klavye = dersBilgisi.klavye;
        this.klavye = klavye;

        MigLayout ml = new MigLayout("", "[center][center]", "[center][center]");
        this.setLayout(ml);

        durumPaneli = new DurumPaneli(dersBilgisi);

        dersOturumu = new DersOturumu(dersBilgisi, durumPaneli);

        dersIcerikPaneli = new DersIcerikPaneli(dersBilgisi, this.dersOturumu);

        if (dersBilgisi.durumGoster) {
            this.add(dersIcerikPaneli);
            this.add(durumPaneli, "wrap");
        } else
            this.add(dersIcerikPaneli, "wrap");

        elPaneli = new ElPaneli();
        setParmakIsareti(dersIcerikPaneli.beklenenHarf());

        if (dersBilgisi.elGoster)
            this.add(elPaneli, "wrap");

        this.add(statusLabel, "align left");

        this.setFocusable(true);
        this.addKeyListener(this);

        EventBus.publish(new StatusEvent("Bir tusa basinca oturum baslayacak."));
    }

    @EventSubscriber(eventClass = DersEvent.class)
    public void onEvent(DersEvent event) {
        if (event.dersSonlandi) {
            EventBus.publish(new StatusEvent("Yazim oturumu sonlandi."));
            new SonucDialog(dersOturumu);
        }

    }

    public void setParmakIsareti(char c) {
        if (klavye.getPArmakBilgisi(c) != null)
            elPaneli.setParmakBilgisi(klavye.getPArmakBilgisi(c));
    }

    public void saveSession() {
        dersOturumu.setYazilan(dersIcerikPaneli.yazilan);
        try {
            dersOturumu.kaydet();
            dersOturumu.stopTimer();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Oturum kaydedilirken hata olustu! + " + e.getMessage(),
                    "Beklenmeyen Hata..",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void keyTyped(KeyEvent e) {
        if (klavye.harfYazilabilir(e.getKeyChar())) {
            // ders oturumu ilk legal tusa basma ile baslar.
            if (!basladi) {
                basladi = true;
                dersOturumu.devamEt();
                EventBus.publish(new StatusEvent("Oturum basladi. Duraklama icin ESC tusunu kullanin."));
            }
            // eger ders duramlamis ise
            if (durakladi) {
                dersOturumu.devamEt();
                durakladi = false;
            }

            // eger ders sonuna gelinmemis el resmini guncelle.
            if (!dersIcerikPaneli.yaziSonunaGelindi()) {
                dersIcerikPaneli.karakterYaz(e.getKeyChar());
                setParmakIsareti(dersIcerikPaneli.beklenenHarf());
            }

        } else {
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                if (!durakladi) {
                    EventBus.publish(new StatusEvent("Oturum Durakladi.. Tusa basarak devam edebilirsiniz."));
                    durakladi = true;
                    dersOturumu.durakla();
                } else {
                    dersOturumu.devamEt();
                    durakladi = false;
                    EventBus.publish(new StatusEvent("Oturum devam ediyor. Duraklama icin ESC tusunu kullanin."));
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

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setLayout(new MigLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new AnaDersPaneli(DersBilgisi.ornek()));
        f.pack();
        f.setVisible(true);
    }

}
