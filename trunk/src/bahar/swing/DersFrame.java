package bahar.swing;

import bahar.bilgi.DersBilgisi;
import bahar.bilgi.Klavye;
import bahar.bilgi.Klavyeler;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.EventBus;

public class DersFrame extends JDialog {

    AnaDersPaneli anaDersPaneli;

    public DersFrame(DersBilgisi dersBilgisi, Klavye klavye) {

        // Eventbus mekanizmasina bu sinifi ekle.
        AnnotationProcessor.process(this);

        setLayout(new MigLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setLocation(200, 150);
        anaDersPaneli = new AnaDersPaneli(dersBilgisi, klavye);
        add(anaDersPaneli, "shrink");
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new DersFrame(DersBilgisi.ornek(), Klavyeler.amerikanQ());
    }

    @EventSubscriber(eventClass = DersEvent.class)
    public void onEvent(DersEvent event) {
        if (event.dersSonucuKapandi) {
            anaDersPaneli.saveSession();
            this.dispose();
        }
    }

}
