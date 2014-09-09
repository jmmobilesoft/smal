package sk.jmmobilesoft.smartalarm.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.os.Environment;
import sk.jmmobilesoft.smartalarm.service.Helper;

public class Logger {

	private static String file = "smartLog.txt";

	public static void appInfo(String s) {
		String log = "INFO app: " + getTime() + s;
		System.out.println(log);
		writeToFile(log);
	}

	public static void setInfo(String s) {
		String log = "INFO set: " + getTime() + s;
		System.out.println(log);
		writeToFile(log);
	}

	private static String getTime() {
		Calendar current = Helper.getCurrentTime();
		String time = current.get(Calendar.DAY_OF_MONTH) + "/"
				+ (current.get(Calendar.MONTH) + 1) + "/"
				+ current.get(Calendar.YEAR) + " - "
				+ current.get(Calendar.HOUR_OF_DAY) + ":"
				+ current.get(Calendar.MINUTE) + ":"
				+ current.get(Calendar.SECOND) + " || ";
		return time;
	}

	private static void writeToFile(String s) {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (mExternalStorageAvailable && mExternalStorageWriteable) {
			BufferedWriter out = null;
			File log = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), file);
			if (!log.exists()) {
				try {
					log.createNewFile();
				} catch (IOException e) {
					System.out.println("CANT CREATE LOG FILE");
				}
			}
			try {
				out = new BufferedWriter(new FileWriter(log, true));
				out.append(s);
				out.newLine();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
