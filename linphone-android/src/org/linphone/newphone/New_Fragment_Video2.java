package org.linphone.newphone;

import org.linphone.CallManager;
import org.linphone.LinphoneManager;
import org.linphone.LinphoneUtils;
import org.linphone.R;
import org.linphone.compatibility.CompatibilityScaleGestureDetector;
import org.linphone.compatibility.CompatibilityScaleGestureListener;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.mediastream.Log;
import org.linphone.mediastream.video.AndroidVideoWindowImpl;
import org.linphone.mediastream.video.capture.hwconf.AndroidCameraConfiguration;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class New_Fragment_Video2 extends Fragment implements OnGestureListener,
		OnDoubleTapListener, CompatibilityScaleGestureListener {

	private static New_Fragment_Video2 instance;

	private SurfaceView oppositeView;
	private SurfaceView localView;
	private LinearLayout mMenuLeft;
	private TextView mTvAnswerVideo, mTvAnswerAudio, mTvAnswerHangup, mTvAnswerWindow,
			mTvAnswerCamera;

	private AndroidVideoWindowImpl androidVideoWindowImpl;
	private float mZoomFactor = 1.f;
	private float mZoomCenterX, mZoomCenterY;
	private CompatibilityScaleGestureDetector mScaleDetector;
	private View myView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {		
		myView = inflater.inflate(R.layout.new_fragment_video2, container, false);

		onInit();
		setSurfaceView();

		return myView;
	}

	public static final boolean isInstanciated() {
		return instance != null;
	}

	public static final New_Fragment_Video2 instance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new New_Fragment_Video2();
			return instance;
		}
	}

	private void onInit() {

		mMenuLeft = (LinearLayout) myView.findViewById(R.id.mMenuLeft);
		mTvAnswerVideo = (TextView) myView.findViewById(R.id.mTvAnswerVideo);
		mTvAnswerAudio = (TextView) myView.findViewById(R.id.mTvAnswerAudio);
		mTvAnswerHangup = (TextView) myView.findViewById(R.id.mTvAnswerHangup);
		mTvAnswerWindow = (TextView) myView.findViewById(R.id.mTvAnswerWindow);
		mTvAnswerCamera = (TextView) myView.findViewById(R.id.mTvAnswerCamera);

		mTvAnswerVideo.setOnClickListener(mOnClick);
		mTvAnswerAudio.setOnClickListener(mOnClick);
		mTvAnswerHangup.setOnClickListener(mOnClick);
		mTvAnswerWindow.setOnClickListener(mOnClick);
		mTvAnswerCamera.setOnClickListener(mOnClick);
	}

	private OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int id = arg0.getId();
			switch (id) {
			case R.id.mTvAnswerVideo:
				Toast.makeText(myView.getContext(), "敬请期待", Toast.LENGTH_SHORT).show();
				break;
			case R.id.mTvAnswerAudio:
				Toast.makeText(myView.getContext(), "敬请期待", Toast.LENGTH_SHORT).show();
				break;
			case R.id.mTvAnswerHangup:
				hangUp();
				break;
			case R.id.mTvAnswerWindow:
				mSwitchWindow.setResult("SwitchWindow");
				break;
			case R.id.mTvAnswerCamera:
				switchCamera();
				break;
			case R.id.localView:
				break;
			}
		}
	};

	private void hangUp() {
		LinphoneCore lc = LinphoneManager.getLc();
		LinphoneCall currentCall = lc.getCurrentCall();

		if (currentCall != null) {
			lc.terminateCall(currentCall);
		} else if (lc.isInConference()) {
			lc.terminateConference();
		} else {
			lc.terminateAllCalls();
		}
	}

	@SuppressWarnings("deprecation")
	private void setSurfaceView() {

		oppositeView = (SurfaceView) myView.findViewById(R.id.oppositeView);
		localView = (SurfaceView) myView.findViewById(R.id.localView);
		localView.setOnClickListener(mOnClick);
		localView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		fixZOrder(oppositeView, localView);

		androidVideoWindowImpl = new AndroidVideoWindowImpl(oppositeView, localView,
				new AndroidVideoWindowImpl.VideoWindowListener() {
					public void onVideoRenderingSurfaceReady(AndroidVideoWindowImpl vw,
							SurfaceView surface) {
						LinphoneManager.getLc().setVideoWindow(vw);
						oppositeView = surface;
					}

					public void onVideoRenderingSurfaceDestroyed(AndroidVideoWindowImpl vw) {
						LinphoneCore lc = LinphoneManager.getLc();
						if (lc != null) {
							lc.setVideoWindow(null);
						}
					}

					public void onVideoPreviewSurfaceReady(AndroidVideoWindowImpl vw,
							SurfaceView surface) {
						localView = surface;
						LinphoneManager.getLc().setPreviewWindow(localView);
					}

					public void onVideoPreviewSurfaceDestroyed(AndroidVideoWindowImpl vw) {
						// Remove references kept in jni code and restart camera
						LinphoneManager.getLc().setPreviewWindow(null);
					}
				});

		localView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (mScaleDetector != null) {
					mScaleDetector.onTouchEvent(event);
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (mMenuLeft.getVisibility() == View.GONE) {
						mMenuLeft.setVisibility(View.VISIBLE);
					} else {
						mMenuLeft.setVisibility(View.GONE);
					}
				}
				return true;
			}
		});

		LinphoneManager.getInstance().changeStatusToOnThePhone();
	}

	private void fixZOrder(SurfaceView video, SurfaceView preview) {
		video.setZOrderOnTop(true);
		preview.setZOrderOnTop(false);
		preview.setZOrderMediaOverlay(true);
	}

	private void switchCamera() {
		try {
			int videoDeviceId = LinphoneManager.getLc().getVideoDevice();
			videoDeviceId = (videoDeviceId + 1)
					% AndroidCameraConfiguration.retrieveCameras().length;
			LinphoneManager.getLc().setVideoDevice(videoDeviceId);
			CallManager.getInstance().updateCall();

			if (localView != null) {
				LinphoneManager.getLc().setPreviewWindow(localView);
			}
		} catch (ArithmeticException ae) {
			Log.e("Cannot swtich camera : no camera");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (oppositeView != null) {
			((GLSurfaceView) oppositeView).onResume();
		}

		if (androidVideoWindowImpl != null) {
			synchronized (androidVideoWindowImpl) {
				LinphoneManager.getLc().setVideoWindow(androidVideoWindowImpl);
			}
		}

		instance = this;
	}

	@Override
	public void onPause() {
		if (androidVideoWindowImpl != null) {
			synchronized (androidVideoWindowImpl) {
				LinphoneManager.getLc().setVideoWindow(null);
			}
		}

		if (oppositeView != null) {
			((GLSurfaceView) oppositeView).onPause();
		}
		super.onPause();
	}

	public boolean onScale(CompatibilityScaleGestureDetector detector) {
		mZoomFactor *= detector.getScaleFactor();
		// Don't let the object get too small or too large.
		// Zoom to make the video fill the screen vertically
		float portraitZoomFactor = ((float) oppositeView.getHeight())
				/ (float) ((3 * oppositeView.getWidth()) / 4);
		// Zoom to make the video fill the screen horizontally
		float landscapeZoomFactor = ((float) oppositeView.getWidth())
				/ (float) ((3 * oppositeView.getHeight()) / 4);
		mZoomFactor = Math.max(0.1f,
				Math.min(mZoomFactor, Math.max(portraitZoomFactor, landscapeZoomFactor)));

		LinphoneCall currentCall = LinphoneManager.getLc().getCurrentCall();
		if (currentCall != null) {
			currentCall.zoomVideo(mZoomFactor, mZoomCenterX, mZoomCenterY);
			return true;
		}
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if (LinphoneUtils.isCallEstablished(LinphoneManager.getLc().getCurrentCall())) {
			if (mZoomFactor > 1) {
				// Video is zoomed, slide is used to change center of zoom
				if (distanceX > 0 && mZoomCenterX < 1) {
					mZoomCenterX += 0.01;
				} else if (distanceX < 0 && mZoomCenterX > 0) {
					mZoomCenterX -= 0.01;
				}
				if (distanceY < 0 && mZoomCenterY < 1) {
					mZoomCenterY += 0.01;
				} else if (distanceY > 0 && mZoomCenterY > 0) {
					mZoomCenterY -= 0.01;
				}

				if (mZoomCenterX > 1)
					mZoomCenterX = 1;
				if (mZoomCenterX < 0)
					mZoomCenterX = 0;
				if (mZoomCenterY > 1)
					mZoomCenterY = 1;
				if (mZoomCenterY < 0)
					mZoomCenterY = 0;

				LinphoneManager.getLc().getCurrentCall()
						.zoomVideo(mZoomFactor, mZoomCenterX, mZoomCenterY);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		if (LinphoneUtils.isCallEstablished(LinphoneManager.getLc().getCurrentCall())) {
			if (mZoomFactor == 1.f) {
				// Zoom to make the video fill the screen vertically
				float portraitZoomFactor = ((float) oppositeView.getHeight())
						/ (float) ((3 * oppositeView.getWidth()) / 4);
				// Zoom to make the video fill the screen horizontally
				float landscapeZoomFactor = ((float) oppositeView.getWidth())
						/ (float) ((3 * oppositeView.getHeight()) / 4);

				mZoomFactor = Math.max(portraitZoomFactor, landscapeZoomFactor);
			} else {
				resetZoom();
			}

			LinphoneManager.getLc().getCurrentCall()
					.zoomVideo(mZoomFactor, mZoomCenterX, mZoomCenterY);
			return true;
		}

		return false;
	}

	private void resetZoom() {
		mZoomFactor = 1.f;
		mZoomCenterX = mZoomCenterY = 0.5f;
	}

	@Override
	public void onDestroy() {
		// inCallActivity = null;
		instance = null;

		localView = null;
		if (oppositeView != null) {
			oppositeView.setOnTouchListener(null);
			oppositeView = null;
		}
		if (androidVideoWindowImpl != null) {
			androidVideoWindowImpl.release();
			androidVideoWindowImpl = null;
		}
		if (mScaleDetector != null) {
			mScaleDetector.destroy();
			mScaleDetector = null;
		}
		LinphoneManager.getInstance().changeStatusToOnline();
		instance = null;
		super.onDestroy();
		System.gc();
	}

	// private void registerCallDurationTimer() {
	//
	// int callDuration = mCall.getDuration();
	// if (callDuration == 0 && mCall.getState() != State.StreamsRunning) {
	// return;
	// }
	// mCallTimer.setBase(SystemClock.elapsedRealtime() - 1000 * callDuration);
	// mCallTimer.start();
	// }

	@Override
	public boolean onDown(MotionEvent e) {
		return true; // Needed to make the GestureDetector working
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	public interface SwitchWindow2 {
		public void setResult(String result);
	}
	
	private SwitchWindow2 mSwitchWindow;
	public void setSwitchWindow(SwitchWindow2 switchWindow) {
		mSwitchWindow = switchWindow;
	}
}
