package com.kaim808.betterreader.utils;

import android.content.Context;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

/**
 * Created by KaiM on 7/27/17.
 */

public class ViewMeasurementUtils {

    /*
    credit: Katsunori KAWAGUCHI
            hamakn
            https://gist.github.com/hamakn/8939eb68a920a6d7a498
     */

    public static int getStatusBarHeight(Context context) {
        // status bar height
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            return statusBarHeight;
        }
        return 0;
    }

    public static int getNavigationBarHeight(Context context) {
        // navigation bar height
        int navigationBarHeight = 0;
        if (!hasSoftKeys(context)) {
            return navigationBarHeight;
        }
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            return navigationBarHeight;
        }
        return 0;
    }

    // to figure out whether the navigation bar is physical or not
    private static boolean hasSoftKeys(Context context){
        boolean hasSoftwareKeys;

        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        hasSoftwareKeys = !hasMenuKey && !hasBackKey;

        return hasSoftwareKeys;
    }
}
