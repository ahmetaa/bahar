package bahar.bilgi;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class DersOturumu {

    public final AtomicInteger zamanSaniye = new AtomicInteger(0);
    public int yazilanHarfSayisi;
    public int gorunenHataSayisi;
    public int toplamHataSayisi;
    public int yazilanKelimeSayisi;

    public DersBilgisi dersBilgisi;

    Timer timer = new Timer("MyTimer");
    private AtomicBoolean running = new AtomicBoolean(false);

    private final OturumDinleyici dinleyici;


    public void yazilanArttir() {
        yazilanHarfSayisi++;
        dinleyici.harfYazildi(yazilanHarfSayisi, gorunenHataSayisi, hizHesapla());
    }

    public String hizHesapla() {
        if (zamanSaniye.get() > 5) {
            float f = (60 * yazilanHarfSayisi / (float) zamanSaniye.get());
            return String.format("%.1f", f);
        } else return "--";
    }

    public void gorunenHataArttir() {
        gorunenHataSayisi++;
        dinleyici.harfYazildi(yazilanHarfSayisi, gorunenHataSayisi, hizHesapla());
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
        TimerTask timerTask = new TimerTask() {
            public void run() {
                if (running.get()) {
                    zamanSaniye.incrementAndGet();
                    dinleyici.saniyeArtti(zamanSaniye.get(), hizHesapla());
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public void stop() {
        running.set(false);
    }

    public void go() {
        running.set(true);
    }

}
