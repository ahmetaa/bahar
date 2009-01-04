package bahar.bilgi;

import org.jmate.Words;
import org.jmate.Strings;
import org.jmate.Collects;

import java.util.List;
import java.util.Date;


public class DersBilgisi {

    public String kullaniciAdi;
    public String kullaniciNumarasi;
    public String kullaniciSinifi;
    public Date tarih;
    public String icerik;
    public int harfSayisi;
    public Klavye klavye;
    public boolean silmeyeIzinVer=false;
    public boolean durumGoster =true;
    public boolean elGoster=true;


    public DersBilgisi(String content) {
        this.icerik = content;
        this.harfSayisi = content.length();
    }


    public List<String> generateLines() {
        String wrapped = Words.wrap(icerik, 60);
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
        return dersBilgisi;
    }


}
