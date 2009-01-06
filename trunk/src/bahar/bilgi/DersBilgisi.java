package bahar.bilgi;

import org.jmate.Collects;
import org.jmate.Strings;
import org.jmate.Words;

import java.util.Date;
import java.util.List;


public class DersBilgisi {

    public String kullaniciAdi;
    public String kullaniciNumarasi;
    public String kullaniciSinifi;
    public Date tarih;
    public String icerik;
    public int harfSayisi;
    public Klavye klavye;
    public boolean silmeyeIzinVer = false;
    public boolean durumGoster = true;
    public boolean elGoster = true;

    public DersBilgisi(String content) {
        this.icerik = Strings.whiteSpacesToSingleSpace(content).trim();
        this.harfSayisi = icerik.length();
    }

    public int kelimeSayisi() {
        if (!Strings.hasText(icerik)) {
            return 0;
        }
        return icerik.split("[ ]").length;
    }


    public List<String> generateLines() {
        String wrapped = Words.wrap(icerik, 80);
        String[] lines = wrapped.split("[\n]");
        List<String> strs = Collects.newArrayList();
        for (String line : lines) {
            line = Strings.whiteSpacesToSingleSpace(line);
            strs.add(line);
        }
        return strs;
    }

    public static DersBilgisi ornek() {
        DersBilgisi dersBilgisi = new DersBilgisi("Hello world");
//                "Mini mini bir kus donmustu pencereme konmustu Aldim onu iceriye cik cik cik cik otsun diye" +
//                        " Pir pir ederken canlandi ellerim bak bos kaldi ");
        dersBilgisi.kullaniciAdi = "Ahmet A. Akin";
        dersBilgisi.kullaniciNumarasi = "12345";
        dersBilgisi.kullaniciSinifi = "4-B";
        dersBilgisi.klavye = Klavyeler.turkceF();
        return dersBilgisi;
    }


}
