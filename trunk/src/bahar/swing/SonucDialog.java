package bahar.swing;

import bahar.bilgi.DersOturumu;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.bushe.swing.event.EventBus;

public class SonucDialog extends JDialog {

        public SonucDialog(DersOturumu oturum) {

            DurumPaneli durumPaneli = new DurumPaneli(oturum);

            this.setLayout(new MigLayout());

            this.add(durumPaneli, "wrap");
            JButton btnOk = new JButton("Tamam");
            btnOk.setFont(ComponentFactory.VERDANA);

            btnOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    EventBus.publish(new DersEvent(false, true));
                }
            });
            this.add(btnOk);
            this.setModal(true);
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.pack();
            this.setLocation(ComponentFactory.getCenterPos(this.getWidth(), this.getHeight()));
            this.setVisible(true);
        }
    }
