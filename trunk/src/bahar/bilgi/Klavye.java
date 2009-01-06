package bahar.bilgi;

import org.jmate.Collects;

import java.util.Map;

/**
 * Klavye tus yerlesimi, parmak-tus bilgilerini icerir.
 */
public class Klavye {

    String ad;
    String shiftKarakterleri;
    String normalKarakterler;
    String altGrKarakterleri;
    private Map<Character, ParmakBilgisi> parmakTablosu = Collects.newHashMap();
    private String tumKArakterler;

    public Klavye(
            String ad,
            String shiftKarakterleri,
            String normalKarakterler,
            String altGrKarakterleri,
            Map<Character, ParmakBilgisi> parmakTablosu) {
        this.ad = ad;
        this.shiftKarakterleri = shiftKarakterleri;
        this.normalKarakterler = normalKarakterler;
        this.altGrKarakterleri = altGrKarakterleri;
        this.parmakTablosu = parmakTablosu;
        this.tumKArakterler = shiftKarakterleri + normalKarakterler + altGrKarakterleri;
    }

    public ParmakBilgisi getPArmakBilgisi(char c) {
        return parmakTablosu.get(c);
    }

    public boolean harfYazilabilir(char c) {
        return tumKArakterler.indexOf(c) > 0;
    }

    public boolean yaziYazilabilir(String s) {
        for (char c : s.toCharArray()) {
            if (!harfYazilabilir(c)) {
                System.out.println("c = " + c);
                return false;
            }
        }
        return true;
    }

}

