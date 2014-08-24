package sk.jmmobilesoft.smartalarm.service;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Paint;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

public abstract class Helper {

	public static String format(int value) {
		String ret = "";
		if (value < 10) {
			ret = "0";
		}
		ret += Integer.toString(value);
		return ret;
	}
	
	private static PowerManager.WakeLock wl = null;
	
	public static void wakeLockOn(Context context){
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		if (wl == null)
			wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "CLOCK_ALARM");
		wl.acquire();
	}
	
	public static void wakeLockOff(Context context){
		try {
			if (wl != null)
				wl.release();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public static Formatter getNumberPickFormater(){
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
	
	public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
	{
	    final int count = numberPicker.getChildCount();
	    for(int i = 0; i < count; i++){
	        View child = numberPicker.getChildAt(i);
	        if(child instanceof EditText){
	            try{
	                Field selectorWheelPaintField = numberPicker.getClass()
	                    .getDeclaredField("mSelectorWheelPaint");
	                selectorWheelPaintField.setAccessible(true);
	                ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
	                ((EditText)child).setTextColor(color);
	                numberPicker.invalidate();
	                return true;
	            }
	            catch(NoSuchFieldException e){
	                Log.w("setNumberPickerTextColor", e);
	            }
	            catch(IllegalAccessException e){
	                Log.w("setNumberPickerTextColor", e);
	            }
	            catch(IllegalArgumentException e){
	                Log.w("setNumberPickerTextColor", e);
	            }
	        }
	    }
	    return false;
	}
	
}
