package bahar.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class DersSonucPenceresi extends JDialog {
    public DersSonucPenceresi(DurumPaneli durumPaneli) {
        setLayout(new MigLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        add(durumPaneli);
        pack();
        setVisible(true);
    }
}
