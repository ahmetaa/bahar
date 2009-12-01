package bahar.i18n;

import org.jcaki.IOs;
import org.jcaki.KeyValueReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: ahmetaa
 * Date: Nov 30, 2009
 * Time: 5:00:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class I18n {
    static Map<String,String> map = new HashMap<String, String>();

    static {
        try {
            map = new KeyValueReader("=","#").loadFromStream(IOs.getClassPathResourceAsStream("i18n_tr.txt"),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getText(String key) {
        if (map.containsKey(key))
            return map.get(key);
        else return key;
    }
}
