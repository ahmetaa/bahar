package bahar.swing;

import bahar.bilgi.DersBilgisi;
import bahar.bilgi.Klavye;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class DersFrame extends JDialog  {

    public DersFrame(DersBilgisi dersBilgisi, Klavye klavye) {
        setLayout(new MigLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        add(new AnaDersPaneli(dersBilgisi, klavye));
        pack();
        setVisible(true);
    }

}
