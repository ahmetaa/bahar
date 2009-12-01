package bahar.bilgi;


import java.util.HashMap;
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
        return uret("Amerikan Q", shift, normal, altGr, sagElDagilimi, solElDagilimi);
    }


    //TODO: fix a
    public static Klavye turkceQ() {
        String shift = "�!'^+%&/()=?_QWERTYUIOP\u011e\u00dcASDFGHJKL\u015e\u0130>ZXCVBNM\u00d6\u00c7:;";
        String normal = "*1234567890/-qwertyu\u0131op\u011f\u00dcasdfghjkl\u015fi<zxcvbnm\u00f6\u00e7., ";
        // TODO: FIX BELOW.
        String altGr = "";
        String[] solElDagilimi = {" ", "5rfv6tgb$RFV%TGB", "4edc$EDC", "3^gGi", "*+1!2\"fFuU<>jJ"};
        String[] sagElDagilimi = {"", "7yhn8ujm&YHN*UJM", "9ik,(IK<", "0ol.)OL>", "-=p[]'/_+P{}:\"?"};
        return uret("Turkce Q", shift, normal, altGr, sagElDagilimi, solElDagilimi);
    }

    // Turkce ozel


    public static final char CHAR_cc = '\u00e7'; // Kuyruklu kucuk c (ch)
    public static final char CHAR_gg = '\u011f'; // Kucuk yumusak g
    public static final char CHAR_ii = '\u0131'; // Noktassiz kucuk i
    public static final char CHAR_oo = '\u00f6'; // Noktali kucuk o
    public static final char CHAR_ss = '\u015f'; // Kuyruklu kucuk s (sh)
    public static final char CHAR_uu = '\u00fc'; // Noktali kucuk u

    public static final char CHAR_CC = '\u00c7'; // Kuyruklu buyuk c (ch)
    public static final char CHAR_GG = '\u011e'; // Buyuk yumusak g
    public static final char CHAR_II = '\u0130'; // Noktali buyuk i
    public static final char CHAR_OO = '\u00d6'; // Noktali buyuk o
    public static final char CHAR_SS = '\u015e'; // Kuyruklu buyuk s (sh)
    public static final char CHAR_UU = '\u00dc'; // Noktali buyuk u

    public static Klavye turkceF() {
        String shift = "+!\"^$%&'()=?_QWERTYUIOP\u011e\u00dcASDFGHJKL\u015e\u0130>ZXCVBNM\u00d6\u00c7:;";
        String normal = " *1234567890/-qwertyu\u0131op\u011f\u00fcasdfghjkl\u015fi<zxcvbnm\u00f6\u00e7.,";
        String altGr = "";
        String[] solElDagilimi = {" ", "5%\u0131IaAcC6&oO\u00dc\u00fc\u00e7\u00c7", "4$\u011f\u011eeEvV", "3^gGi\u0130\u00f6\u00d6", "*+1!2\"fFuU<>jJ"};
        String[] sagElDagilimi = {"", "7'dDtTzZ8(rRkKsS", "9)nNmMbB", "0=hHlL.:", "/?-_pPqQwWyY\u015e\u015fxX,;"};
        return uret("Turkce F", shift, normal, altGr, sagElDagilimi, solElDagilimi);
    }

    private static Klavye uret(String ad, String shift, String normal, String altGr, String[] sagEl, String[] solEl) {
        Map<Character, ParmakBilgisi> parmakBilgiTablosu = new HashMap<Character, ParmakBilgisi>();

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

        return new Klavye(ad, shift, normal, altGr, parmakBilgiTablosu);
    }

    public static Klavye getFromLayout(KlavyeYerlesimi yerlesim) {
        switch (yerlesim) {
            case AMERIKAN_Q:
                return amerikanQ();
            case TURKCE_F:
                return turkceF();
            case TURKCE_Q:
                return turkceQ();
            default:
                throw new IllegalArgumentException("Cannot find keyboard!" + yerlesim.name());

        }
    }

}
