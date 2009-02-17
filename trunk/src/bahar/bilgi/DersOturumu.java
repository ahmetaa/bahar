package bahar.bilgi;

import org.jmate.Chars;
import org.jmate.SimpleFileWriter;
import org.jmate.Strings;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DersOturumu {

    private final AtomicInteger zamanYuzMilisaniye = new AtomicInteger(0);
    public int yazilanHarfSayisi;
    public int gorunenHataSayisi;
    public int toplamHataSayisi;

    private String yazilan;

    public DersBilgisi dersBilgisi;

    private AtomicBoolean running = new AtomicBoolean(false);

    private final OturumDinleyici dinleyici;


    public void yazilanArttir() {
        yazilanHarfSayisi++;
        dinleyici.harfYazildi(yazilanHarfSayisi, gorunenHataSayisi, harfHizHasapla());
    }

    public String getYazilan() {
        return yazilan;
    }

    public void setYazilan(String yazilan) {
        this.yazilan = yazilan;
    }

    public String harfHizHasapla() {
        if (zamanYuzMilisaniye.get() > 3) {
            float f = (60f * yazilanHarfSayisi / ((float) zamanYuzMilisaniye.get() / 10));
            return String.format("%.1f", f);
        } else return "--";
    }

    public void gorunenHataArttir() {
        gorunenHataSayisi++;
        dinleyici.harfYazildi(yazilanHarfSayisi, gorunenHataSayisi, harfHizHasapla());
    }

    public void tumHataArttir() {
        toplamHataSayisi++;
    }

    public DersOturumu(DersBilgisi dersBilgisi, OturumDinleyici dinleyici) {
        this.dersBilgisi = dersBilgisi;
        this.dinleyici = dinleyici;
        initialize();
    }

    Timer timer = new Timer("MyTimer");

    public void initialize() {

        TimerTask timerTask = new TimerTask() {
            public void run() {
                if (running.get()) {
                    zamanYuzMilisaniye.incrementAndGet();
                    dinleyici.saniyeArtti(zamanYuzMilisaniye.get(), harfHizHasapla());
                }
            }
        };
        timer.schedule(timerTask, 0, 100);
    }

    public void durakla() {
        running.set(false);
    }

    public void devamEt() {
        running.set(true);
    }

    private int zamanSaniye() {
        return zamanYuzMilisaniye.get() / 10;
    }

    public int sure() {
        return zamanSaniye();
    }

    public int yazilanKelimeSayisi() {
        if (!Strings.hasText(yazilan))
            return 0;
        else return Strings.whiteSpacesToSingleSpace(yazilan).split("[ ]").length;
    }


    public static final char CHAR_cc = '\u00e7'; // Kuyruklu kucuk c (ch)
    public static final char CHAR_gg = '\u011f'; // Kucuk yumusak g
    public static final char CHAR_ii = '\u0131'; // Noktassiz kucuk i
    public static final char CHAR_oo = '\u00f6'; // Noktali kucuk o
    public static final char CHAR_ss = '\u015f'; // Kuyruklu kucuk s (sh)
    public static final char CHAR_uu = '\u00fc'; // Noktali kucuk u


    public void stopTimer() {
        timer.cancel();
    }

    public void kaydet() throws IOException {

        String kullaniciAdi = dersBilgisi.kullaniciAdi.toLowerCase(new Locale("tr"));
        StringBuilder sb = new StringBuilder();
        for (char c : kullaniciAdi.toCharArray()) {
            if (c == CHAR_cc)
                sb.append('c');
            else if (c == CHAR_gg)
                sb.append('g');
            else if (c == CHAR_ii)
                sb.append('i');
            else if (c == CHAR_oo)
                sb.append('o');
            else if (c == CHAR_ss)
                sb.append('s');
            else if (c == CHAR_uu)
                sb.append('u');
            else if (Chars.isAsciiAlphanumeric(c))
                sb.append(c);
        }
        String fileName = Strings.eliminateWhiteSpaces(sb.toString().replaceAll("[ ]+", "-")) + "_" + (System.currentTimeMillis() + ".txt");
        SimpleFileWriter swf = new SimpleFileWriter.Builder(fileName).encoding("utf-8").keepOpen().build();

        swf.writeLine("Ad:" + dersBilgisi.kullaniciAdi);
        swf.writeLine("Numara:" + dersBilgisi.kullaniciNumarasi);
        swf.writeLine("Klavye:" + dersBilgisi.klavye.ad);
        swf.writeLine("Beklenen Yazi:" + dersBilgisi.icerik);
        swf.writeLine("Yazilan Yazi :" + yazilan);
        swf.writeLine("Toplam harf sayisi:" + dersBilgisi.icerik.length());
        swf.writeLine("Kelime sayisi:" + dersBilgisi.kelimeSayisi());
        swf.writeLine("Yazilan harf sayisi:" + yazilan.length());
        swf.writeLine("Yazilan kelime sayisi:" + yazilanKelimeSayisi());
        swf.writeLine("Gorunen Hata Sayisi:" + gorunenHataSayisi);
        swf.writeLine("Toplam Hata Sayisi:" + toplamHataSayisi);
        swf.writeLine("Sure (sn):" + String.format("%.1f", ((float)zamanYuzMilisaniye.get() / 10f) ));
        swf.writeLine("Hiz (dakika/kelime):" + kelimeHizHesapla());
        swf.writeLine("Hiz (dakika/harf):" + harfHizHasapla());
//        swf.writeLine("Dogru yazim orani (gorunen hatalara):" +  )
//        swf.writeLine("Dogru yazim orani (tum hatalara):" +  )
        swf.close();
    }

    private String kelimeHizHesapla() {
        float f = (60f * yazilanKelimeSayisi() / ((float) zamanYuzMilisaniye.get() / 10f));
        return String.format("%.1f", f);
    }



}
