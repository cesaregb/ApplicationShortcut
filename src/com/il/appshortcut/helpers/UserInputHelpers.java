package com.il.appshortcut.helpers;

import android.media.AudioManager;
import android.os.Vibrator;

/**
 * @author cesar
 *
 */
public class UserInputHelpers {
	
	/**
	 * @param vb
	 * @param am
	 */
	public static void pressEffect(Vibrator vb, AudioManager am){
        vb.vibrate(50);
        float vol = 1.0f; 
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);
	}
}
