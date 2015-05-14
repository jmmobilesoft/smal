package sk.jmmobilesoft.smartalarm.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;
import sk.jmmobilesoft.smartalarm.helpers.Helper;

public class Logger {

	private static String file = "smartLog.txt";

	public static void appInfo(String s) {
		String log = getTime() + " | INFO APP: " + s;
		System.out.println(log);
		//writeToFile(log);
	}

	public static void setInfo(String s) {
		String log = getTime() + " | INFO SET: " + s;
		System.out.println(log);
		//writeToFile(log);
	}
	
	public static void serviceInfo(String s) {
		String log = getTime() + " | SERVICE : " + s;
		System.out.println(log);
		//writeToFile(log);
	}

	public static void logStackTrace(StackTraceElement[] e){
		for (int i = 0; i < e.length; i++) {
			Logger.serviceInfo(e[i].toString());
		}
	}
	
	private static String getTime() {
		Calendar current = Helper.getCurrentTime();
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		return formater.format(current.getTime());
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
