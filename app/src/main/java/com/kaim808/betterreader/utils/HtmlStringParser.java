package com.kaim808.betterreader.utils;

import android.os.Build;
import android.text.Html;

/**
 * Created by KaiM on 8/21/17.
 */

public class HtmlStringParser {

    public static String htmlStringToString(String s) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            // deprecated as of api 24
            return Html.fromHtml(s).toString();
        }
    }
}
