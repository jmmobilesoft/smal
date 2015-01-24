package sk.jmmobilesoft.smartalarm.helpers;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

public class GlobalHelper {

	public static float determineVolume(int seekbarStatus) {
		final float MIN = 0.2f;
		float volume = (float) (seekbarStatus * 0.01);
		if (volume < MIN) {
			return 0.1f;
		}
		return volume;
	}

	public static Formatter getNumberPickFormater() {
		return new Formatter() {

			@Override
			public String format(int value) {
				if (value < 10) {
					return "0" + value;
				}
				return String.valueOf(value);
			}
		};
	}	
	
	public static String getSongName(Uri uri, Activity activity) {
		String[] projection = { MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.TITLE };

		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);

		cursor.moveToNext();
		String artist = cursor.getString(0);
		String title = cursor.getString(1);

		if (!artist.equals("<unknown>")) {
			title = artist + " - " + title;
		}

		return title;
	}

	public static void hideActionBar(Activity activity) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			ActionBar actionBar = activity.getActionBar();
			actionBar.hide();
		}
	}

	public static boolean setNumberPickerTextColor(NumberPicker numberPicker,
			int color) {
		final int count = numberPicker.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = numberPicker.getChildAt(i);
			if (child instanceof EditText) {
				try {
					Field selectorWheelPaintField = numberPicker.getClass()
							.getDeclaredField("mSelectorWheelPaint");
					selectorWheelPaintField.setAccessible(true);
					((Paint) selectorWheelPaintField.get(numberPicker))
							.setColor(color);
					((EditText) child).setTextColor(color);
					numberPicker.invalidate();
					return true;
				} catch (NoSuchFieldException e) {
					Log.w("setNumberPickerTextColor", e);
				} catch (IllegalAccessException e) {
					Log.w("setNumberPickerTextColor", e);
				} catch (IllegalArgumentException e) {
					Log.w("setNumberPickerTextColor", e);
				}
			}
		}
		return false;
	}

	public static void stopMediaPlayer(AudioManager mAudioManager,
			int originalVolume, MediaPlayer mp) {
		try {
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					originalVolume, 0);
			mp.stop();
			mp.release();
			mp.reset();
		} catch (NullPointerException | IllegalStateException e) {
			Log.i("INFO", "media player already stopped");
		}
	}
	
	public static String getPreference(Context context, String key, String defaultV){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String value = sharedPref.getString(key, defaultV);
		return value;
	}
}
