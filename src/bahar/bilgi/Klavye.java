package bahar.bilgi;

import org.jmate.Collects;

import java.util.Map;
import java.util.Set;

/**
 * Klavye tus yerlesimi, parmak-tus bilgilerini icerir.
 */
public class Klavye {

    String shiftKarakterleri;
    String normalKarakterler;
    String altGrKarakterleri;
    private Map<Character, ParmakBilgisi> parmakTablosu = Collects.newHashMap();
    private String tumKArakterler;

    public Klavye(
            String shiftKarakterleri,
            String normalKarakterler,
            String altGrKarakterleri,
            Map<Character, ParmakBilgisi> parmakTablosu) {
        this.shiftKarakterleri = shiftKarakterleri;
        this.normalKarakterler = normalKarakterler;
        this.altGrKarakterleri = altGrKarakterleri;
        this.parmakTablosu = parmakTablosu;
        this.tumKArakterler = shiftKarakterleri + normalKarakterler + altGrKarakterleri;
    }

    public ParmakBilgisi getPArmakBilgisi(char c) {
        return parmakTablosu.get(c);
    }

    public boolean karakterLegal(char c) {
        return tumKArakterler.indexOf(c) > 0;
    }
}

