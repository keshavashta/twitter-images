package util;

import java.util.HashMap;

/**
 * Created by KESHAV on 27/5/13.
 */
public class DataMessenger {
    private static HashMap<String, Object> map = new HashMap<String, Object>();

    public static void Set(String key, Object value) {
        map.put(key, value);
    }

    public static Object Get(String key) {
        Object data = map.get(key);
        map.remove(key);
        return data;
    }
}