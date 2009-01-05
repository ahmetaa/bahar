package bahar.bilgi;

import org.jmate.SimpleFileWriter;
import org.jmate.Strings;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.IOException;

public class DersOturumu {

    private final AtomicInteger zamanSaniye = new AtomicInteger(0);
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
        if (zamanSaniye.get() > 3) {
            float f = (60 * yazilanHarfSayisi / (float) zamanSaniye.get());
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

    public void initialize() {
        Timer timer = new Timer("MyTimer");
        TimerTask timerTask = new TimerTask() {
            public void run() {
                if (running.get()) {
                    zamanSaniye.incrementAndGet();
                    dinleyici.saniyeArtti(zamanSaniye.get(), harfHizHasapla());
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public void durakla() {
        running.set(false);
    }

    public void devamEt() {
        running.set(true);
    }

    public int sure() {
        return zamanSaniye.get();
    }

    public int yazilanKelimeSayisi() {
        if (!Strings.hasText(yazilan))
            return 0;
        else return Strings.whiteSpacesToSingleSpace(yazilan).split("[ ]").length;
    }

    public void kaydet() throws IOException {
        String fileName = Strings.eliminateWhiteSpaces(dersBilgisi.kullaniciAdi) + "_" + (System.currentTimeMillis() + ".txt");
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
        swf.writeLine("Sure (sn):" + zamanSaniye);
        swf.writeLine("Hiz (dakika/kelime):" + kelimeHizHesapla());
        swf.writeLine("Hiz (dakika/harf):" + harfHizHasapla());
//        swf.writeLine("Dogru yazim orani (gorunen hatalara):" +  )
//        swf.writeLine("Dogru yazim orani (tum hatalara):" +  )
        swf.close();
    }

    private String kelimeHizHesapla() {
        float f = (60 * yazilanKelimeSayisi() / (float) zamanSaniye.get());
        return String.format("%.1f", f);
    }


}
