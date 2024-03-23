package com.example.capitaview_page_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CacheClearing extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Clears app cache daily to update listings
        cacheManager.clearCache(context);
    }
}
