package com.pir.wicket.web.util;

import org.apache.wicket.util.cookies.CookieDefaults;
import org.apache.wicket.util.cookies.CookieUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */

public class CookieUtil {

    public static String TRUE = "true";
    public static String UID = "uid";
    public static String REMEMBER_ME = "rememberMe";

    public static void save(String key, String value){
        CookieDefaults defaults = new CookieDefaults();
        //365 days
        defaults.setMaxAge(31536000);
        org.apache.wicket.util.cookies.CookieUtils utils = new org.apache.wicket.util.cookies.CookieUtils(defaults);
        utils.save(key,value);
    }

    public static void remove(String key) {
        org.apache.wicket.util.cookies.CookieUtils utils = new org.apache.wicket.util.cookies.CookieUtils();
        utils.remove(key);
    }

    public static String load(String key) {
        CookieUtils utils = new CookieUtils();
        return utils.load(key);
    }
}