package bahar.bilgi;

import org.jcaki.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class DersBilgisi {

    public String ogretmen;
    public int verilenSureSaniye = -1;
    public String kullaniciAdi;
    public String kullaniciNumarasi;
    public String kullaniciSinifi;
    public String icerik;
    public int harfSayisi;
    public int kelimeSayisi;
    public Klavye klavye;
    public boolean silmeyeIzinVer = false;
    public boolean ardisilHataGoster = false;
    public boolean durumGoster = true;
    public boolean elGoster = true;

    public DersBilgisi(String content) {
        this.icerik = Strings.whiteSpacesToSingleSpace(content).trim();
        this.harfSayisi = icerik.length();
    }

    public DersBilgisi(File dersIcerikDosyasi) throws DersBilgiHatasi, IOException {
        KeyValueReader kvr = new KeyValueReader("=", "#");
        Map<String, String> bilgiler = kvr.loadFromFile(new SimpleTextReader(dersIcerikDosyasi, "utf-8"));
        this.ogretmen = bilgiler.get("ogretmen");
        this.silmeyeIzinVer = Boolean.parseBoolean(bilgiler.get("silmeye.izin.ver"));
        this.ardisilHataGoster = Boolean.parseBoolean(bilgiler.get("ardisil.hata.goster"));
        this.elGoster = Boolean.parseBoolean(bilgiler.get("el.goster"));
        this.durumGoster = Boolean.parseBoolean(bilgiler.get("durum.goster"));
        this.verilenSureSaniye = Integer.parseInt(bilgiler.get("sure.saniye"));
        this.klavye = Klavyeler.getFromLayout(KlavyeYerlesimi.fromString(bilgiler.get("klavye")));
        if (bilgiler.containsKey("icerik.yazi"))
            icerik = bilgiler.get("icerik.yazi");
        if (bilgiler.containsKey("icerik.dosya")) {
            File dosya = new File(bilgiler.get("icerik.dosya"));
            if (!dosya.exists()) {
                throw new DersBilgiHatasi("Icerik dosyasina erisilemedi!" + dosya.getName());
            }
            icerik = SimpleTextReader.trimmingUTF8Reader(dosya).asString();
        }
        this.icerik = Strings.whiteSpacesToSingleSpace(icerik);
        this.kelimeSayisi = icerik.split(" ").length;
        this.harfSayisi = icerik.length();
        if (harfSayisi > 500)
            throw new DersBilgiHatasi("Yazi boyutu cok buyuk. 500 karakterden az olmali.");
    }


    public boolean surelimi() {
        return verilenSureSaniye > 0;
    }

    public boolean sureVar(int saniyeSure) {
        return verilenSureSaniye > 0 && saniyeSure < verilenSureSaniye;
    }

    private static final byte[] bomBytes = new byte[]{(byte) 0xef, (byte) 0xbb, (byte) 0xbf};

    private static boolean possibleUtf8(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        try {
            byte[] bomRead = new byte[bomBytes.length];
            return is.read(bomRead, 0, bomBytes.length) != -1 && Arrays.equals(bomRead, bomBytes);
        } finally {
            IOs.closeSilently(is);
        }
    }


    public int kelimeSayisi() {
        if (!Strings.hasText(icerik)) {
            return 0;
        }
        return kelimeSayisi;
    }


    public List<String> generateLines() {
        String wrapped = Words.wrap(icerik, 80);
        String[] lines = wrapped.split("[\n]");
        List<String> strs = new ArrayList<String>();
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
