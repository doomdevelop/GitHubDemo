package org.kozlowski.githubdemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.kozlowski.githubdemo.MyApplication;

/**
 * Created by and on 16.05.17.
 */

public class NetworkUtil {
    public static boolean isConnected(final MyApplication myApplication) {
        ConnectivityManager cm = (ConnectivityManager) myApplication
            .getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
            && activeNetwork.isConnectedOrConnecting();
    }

}
