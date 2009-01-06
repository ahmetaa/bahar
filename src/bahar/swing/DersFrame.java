package bahar.swing;

import bahar.bilgi.DersBilgisi;
import bahar.bilgi.Klavye;
import bahar.bilgi.Klavyeler;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.EventBus;
import sun.awt.WindowClosingListener;

public class DersFrame extends JDialog {

    AnaDersPaneli anaDersPaneli;

    public DersFrame(DersBilgisi dersBilgisi) {

        // Eventbus mekanizmasina bu sinifi ekle.
        AnnotationProcessor.process(this);

        setLayout(new MigLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                EventBus.clearAllSubscribers();
                EventBus.clearCache();
                dispose();
            }
        });
        this.setModal(true);
        anaDersPaneli = new AnaDersPaneli(dersBilgisi);
        add(anaDersPaneli, "shrink");
        pack();
        this.setLocation(ComponentFactory.getCenterPos(getWidth(), getHeight()));
        setVisible(true);
    }

    public static void main(String[] args) {
        new DersFrame(DersBilgisi.ornek());
    }

    @EventSubscriber(eventClass = DersEvent.class)
    public void onEvent(DersEvent event) {
        if (event.dersSonucuKapandi) {
            anaDersPaneli.saveSession();
            EventBus.clearAllSubscribers();
            EventBus.clearCache();
            this.dispose();
        }
    }

}