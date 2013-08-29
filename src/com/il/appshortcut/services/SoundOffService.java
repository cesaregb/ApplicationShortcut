package com.il.appshortcut.services;

import android.content.Context;
import android.media.AudioManager;

public class SoundOffService extends ServiceVo{
	public SoundOffService(){
		ID = 5;
	}
	AudioManager mAudioManager;
	@Override
	public boolean run(Context context) {
		mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		if (checkIfPhoneIsSilent()) {
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		} else {
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
		return true;
	}
	
	private boolean checkIfPhoneIsSilent() {
		int ringermode = mAudioManager.getRingerMode();
		if (ringermode == AudioManager.RINGER_MODE_SILENT) {
			return true;
		} else {
			return false;
		}
	}
}
