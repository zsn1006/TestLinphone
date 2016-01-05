package org.linphone.newphone.db;

import java.io.File;

import org.linphone.LinphoneManager;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.mediastream.Log;

public class saveRecordDao {

	private static saveRecordDao instance = null;
	private LinphoneCall mCall;

	private saveRecordDao() {
	}

	public static saveRecordDao getInstance() {
		if (instance == null) {
			load();
		}
		return instance;
	}

	private static synchronized void load() {
		if (instance == null) {
			instance = new saveRecordDao();
		}
	}

	public void onStartRecording(String path, Boolean isVideo) {

		LinphoneCall call = LinphoneManager.getLc().getCurrentCall();
		if (call != null) {

			mCall = call;
			if (isVideo) {
				path = "/mnt/sdcard/testRecordVideo.mkv";
				Log.i("===111=================testRecordVideo===");
			} else {
				path = "/mnt/sdcard/testRecordAudio.wav";
				Log.i("===222=================testRecordAudio===");
			}
			
			try {
				File file = new File(path);
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();

				LinphoneCallParams params = LinphoneManager.getLc().createCallParams(call);
				params.setRecordFile(path);
				LinphoneManager.getLc().updateCall(call, params);
				call.startRecording();
				Log.i("===333=================startRecord===");
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("===444=================startRecord===");
			}
		}
	}

	public void onStopRecording() {

		Log.i("===111=================stopRecord===");
		if (mCall != null) {
			Log.i("===222=================stopRecord===");
			mCall.stopRecording();
		}
	}
}
