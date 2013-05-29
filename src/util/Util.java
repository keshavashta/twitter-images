package util;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: saxena.arunesh
 * Date: 5/25/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static boolean isStringNullOrEmpty(String value) {
        if (value != null && value.trim().length() != 0) {
            return false;
        }
        return true;
    }

    public static String getUrlStringFromQueryParams(String url, List<NameValuePair> params) throws Exception {
        return url + "?" + URLEncodedUtils.format(params, "UTF-8");
    }

}
