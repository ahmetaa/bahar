package bahar.bilgi;

import org.jmate.Collects;

import java.util.Map;

/**
 * cesitli klavyelerin bilgilerini tasir.
 * Tanimlanan klavyeler:
 * turkce - q
 * turkce - f
 * ingilizce - q
 */
public class Klavyeler {

    public static Klavye amerikanQ() {

        String shift = "~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?";
        String normal = "`1234567890-=qwertyuiop[]asdfghjkl;'zxcvbnm,./ ";
        String altGr = "";
        String[] solElDagilimi = {" ", "5rfv6tgb$RFV%TGB", "4edc$EDC", "3wsx#WSX", "`12qaz!@QAZ~"};
        String[] sagElDagilimi = {"", "7yhn8ujm&YHN*UJM", "9ik,(IK<", "0ol.)OL>", "-=p[]'/_+P{}:\"?"};
        return uret(shift, normal, altGr, sagElDagilimi, solElDagilimi);
    }

    public static Klavye turkceQ() {
        String shift = "é!'^+%&/()=?_QWERTYUIOP\u011e\u00dcASDFGHJKL\u015e\u0130;>ZXCVBNM\u00d6\u00c7:";
        String normal = "\"1234567890*-qwertyu\u0131op\u011f\u00dcasdfghjkl\u015fi,<zxcvbnm\u00f6\u00e7. ";
        // TODO: FIX BELOW.
        String altGr = "";
        String[] sagElDagilimi = {" ", "5rfv6tgb$RFV%TGB", "4edc$EDC", "3wsx#WSX", "`12qaz!@QAZ~"};
        String[] solElDagilimi = {"", "7yhn8ujm&YHN*UJM", "9ik,(IK<", "0ol.)OL>", "-=p[]'/_+P{}:\"?"};
        return uret(shift, normal, altGr, sagElDagilimi, solElDagilimi);
    }

    public static Klavye turkceF() {
        // TODO: HENUZ TANIMLANMADI.
        String shift = "é!'^+%&/()=?_QWERTYUIOP\u011e\u00dcASDFGHJKL\u015e\u0130;>ZXCVBNM\u00d6\u00c7:";
        String normal = "\"1234567890*-qwertyu\u0131op\u011f\u00dcasdfghjkl\u015fi,<zxcvbnm\u00f6\u00e7. ";
        String altGr = "";
        String[] sagElDagilimi = {" ", "5rfv6tgb$RFV%TGB", "4edc$EDC", "3wsx#WSX", "`12qaz!@QAZ~"};
        String[] solElDagilimi = {"", "7yhn8ujm&YHN*UJM", "9ik,(IK<", "0ol.)OL>", "-=p[]'/_+P{}:\"?"};
        return uret(shift, normal, altGr, sagElDagilimi, solElDagilimi);
    }

    private static Klavye uret(String shift, String normal, String altGr, String[] sagEl, String[] solEl) {
        Map<Character, ParmakBilgisi> parmakBilgiTablosu = Collects.newHashMap();

        //TODO: kod tekrari yapma.
        for (int i = 0; i < sagEl.length; i++) {
            String s = sagEl[i];
            Parmak parmak = Parmak.values()[i];
            for (char c : s.toCharArray()) {
                boolean shiftVar = shift.indexOf(c) > 0;
                boolean altGrVar = altGr.indexOf(c) > 0;
                parmakBilgiTablosu.put(c, new ParmakBilgisi(El.SAG, parmak, shiftVar, altGrVar));
            }
        }
        for (int i = 0; i < solEl.length; i++) {
            String s = solEl[i];
            Parmak parmak = Parmak.values()[i];
            for (char c : s.toCharArray()) {
                boolean shiftVar = shift.indexOf(c) > 0;
                boolean altGrVar = altGr.indexOf(c) > 0;
                parmakBilgiTablosu.put(c, new ParmakBilgisi(El.SOL, parmak, shiftVar, altGrVar));
            }
        }

        return new Klavye(shift, normal, altGr, parmakBilgiTablosu);
    }


}
