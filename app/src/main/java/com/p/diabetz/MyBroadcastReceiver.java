package com.p.diabetz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private MediaPlayer mp;
    public void onReceive(Context context, Intent intent){

    	//lire la sonnerie
        mp = MediaPlayer.create(context,R.raw.iphone);
        mp.start();
        

    }


}
