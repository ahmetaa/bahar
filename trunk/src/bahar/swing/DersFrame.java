package bahar.swing;

import bahar.bilgi.DersBilgisi;
import net.miginfocom.swing.MigLayout;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DersFrame extends JDialog {

    AnaDersPaneli anaDersPaneli;

    public DersFrame(DersBilgisi dersBilgisi) {

        // Eventbus mekanizmasina bu sinifi ekle.
        AnnotationProcessor.process(this);

        setLayout(new MigLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
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
            //anaDersPaneli.saveSession();
            closeWindow();
        }
    }

    private void closeWindow() {
        EventBus.clearAllSubscribers();
        EventBus.clearCache();
        this.dispose();
    }

}
