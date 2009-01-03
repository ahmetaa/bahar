package bahar.swing;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.Map;
import java.io.IOException;
import java.io.File;

import org.jmate.Collects;
import bahar.bilgi.El;
import bahar.bilgi.Parmak;
import bahar.bilgi.ParmakBilgisi;
import net.miginfocom.swing.MigLayout;


public class ElPaneli extends JPanel {

    TekEl sagEl, solEl;


    public ElPaneli() {
        this.setLayout(new MigLayout());
        solEl = new TekEl(El.SOL, Parmak.TANIMSIZ);
        sagEl = new TekEl(El.SAG, Parmak.TANIMSIZ);
        this.add(solEl);
        this.add(sagEl);
    }

    public void setParmakBilgisi(ParmakBilgisi pb) {
        if (pb.el == El.SAG) {
            sagEl.setFinger(pb.parmak);
            if (pb.shift)
                solEl.setFinger(Parmak.SERCE);
            else if (pb.altGr)
                solEl.setFinger(Parmak.YUZUK);
            else
                solEl.setFinger(Parmak.TANIMSIZ);
        } else {
            solEl.setFinger(pb.parmak);
            if (pb.shift)
                sagEl.setFinger(Parmak.SERCE);
            else if (pb.altGr)
                sagEl.setFinger(Parmak.YUZUK);
            else
                sagEl.setFinger(Parmak.TANIMSIZ);
        }
    }

    private class TekEl extends JPanel {

        private BufferedImage ElResmi;
        private El hand;
        public static final int KARE_BOYUTU = 30;
        private Map<Parmak, Rectangle> parmakKareTablosu = Collects.newHashMap();
        private Parmak parmak = Parmak.TANIMSIZ;
        // if true, panel do not fire finger change events and finger recktangle cannot be altered.
        boolean readOnly = false;


        public Parmak getSelectedFinger() {
            return parmak;
        }

        public void setFinger(Parmak parmak) {
            this.parmak = parmak;
            repaint();
        }

        public TekEl(El el, Parmak initialFinger) {
            // right hand finger locations.
            // this center finger locations should be measured from the right-hand image.
            Point[] locations = {
                    new Point(16, 134),
                    new Point(68, 35),
                    new Point(116, 18),
                    new Point(156, 35),
                    new Point(188, 76)};
            this.hand = el;

            if (el == El.SAG) {
                ElResmi = ComponentFactory.getImageResource("/resimler/right_hand.png");
                for (Point point : locations) {
                    point.setLocation(point.x - KARE_BOYUTU / 2, point.y - KARE_BOYUTU / 2);
                }
            } else {
                ElResmi = ComponentFactory.getImageResource("/resimler/left_hand.png");
                //if this is the left hand, recalculate the x positions.
                for (Point point : locations) {
                    point.setLocation(ElResmi.getWidth() - point.x - KARE_BOYUTU / 2, point.y - KARE_BOYUTU / 2);
                }
            }
            ElResmi.flush();

            // create parmakKareTablosu for each finger. select the index finger rectangle initially.
            for (int i = 0; i < locations.length; i++) {
                Rectangle rec = new Rectangle(locations[i], new Dimension(KARE_BOYUTU, KARE_BOYUTU));
                Parmak finger = Parmak.values()[i];
                parmakKareTablosu.put(finger, rec);
            }

            parmak = initialFinger;

            this.setPreferredSize(new Dimension(ElResmi.getWidth(), ElResmi.getHeight()));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(ElResmi, null, 0, 0);

            // Paint the red frame around parmak finger.
            if (parmak != Parmak.TANIMSIZ) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.setColor(Color.GREEN);
                g2.fill(parmakKareTablosu.get(parmak));
            }
        }
    }
}
