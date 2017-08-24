package com.kaim808.betterreader.utils;

import android.os.Build;
import android.text.Html;

import java.net.URLDecoder;

/**
 * Created by KaiM on 8/21/17.
 */

public class StringParser {

    public static String htmlStringToString(String s) {
        if (Build.VERSION.SDK_INT >= 24) {
            s = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            // deprecated as of api 24
            s = Html.fromHtml(s).toString();
        }
        return s;
    }

    public static String URLStringToString(String s) {
        try {
            s = URLDecoder.decode(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
