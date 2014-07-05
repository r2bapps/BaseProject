/*
 * Logger
 * 
 * 0.2
 * 
 * 2014/05/16
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package r2b.apps.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Android log wrapper. Useful to add several implementations about log
 * management like send to a service, print in a file,...
 * 
 * The order in terms of verbosity, from least to most is ERROR, WARN, INFO, 
 * DEBUG, VERBOSE. Verbose should never be compiled into an application 
 * except during development, this class hides them. Debug logs are compiled 
 * in but stripped at runtime, this class hides them. Error, warning and 
 * info logs are always kept.
 * 
 * If you ofuscate your code a good convention is to declare a TAG constant 
 * in your class with the arg tag needed here.
 * 
 * WARNING: ERROR & INFO logs are allways showing on logcat and saving on log file.
 */
public final class Logger {
	
	/**
	 * Log printer.
	 */
	private static PrintWriter printer;
	/**
	 * Application context
	 */
	private static Context context;
	/**
	 * Logcat date format.
	 */
	private static SimpleDateFormat dateFormat;

	/**
	 * Send a VERBOSE log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void v(String tag, String msg) {
		if (Cons.DEBUG) {
			Log.v(tag, msg);
			if(printer != null) {
				printer.write(parseLog("V", tag, msg));   
				printer.flush();
			}
		}
	}
	
	/**
	 * Send a INFO log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void i(String tag, String msg) {
		Log.i(tag, msg);
		if(printer != null) {
			printer.write(parseLog("I", tag, msg));
			printer.flush();
		}
	}

	/**
	 * Send a DEBUG log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void d(String tag, String msg) {
		if (Cons.DEBUG) {
			Log.d(tag, msg);
			if(printer != null) {
				printer.write(parseLog("D", tag, msg));
				printer.flush();
			}
		}
	}
	
	/**
	 * Send an ERROR log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		Log.e(tag, msg);
		if(printer != null) {
			printer.write(parseLog("E", tag, msg));
			printer.flush();
		}
	}

	/**
	 * Send an ERROR log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log.
	 */
	public static void e(String tag, String msg, Throwable tr) {
		Log.e(tag, msg, tr);
		if(printer != null) {
			printer.write(parseLog("E", tag, msg));
			printer.flush();
		}
	}
		
	/**
	 * Save log to file.	 
	 * @param context Application context.
	 */
	@SuppressLint("SimpleDateFormat")
	public static void openLogFile(final Context context) {
		
		Logger.context = context.getApplicationContext();
		
		if( isStorageReady() ) {
			createExternalStorageLogFile();
			dateFormat = new SimpleDateFormat("dd-MM HH:mm:ss.SSS");
		}		
		else {
			Logger.context = null;
		}
		
	}

	/**
	 * Close log file.
	 */
	public static void closeLogFile() {
		if(printer != null) {
			printer.flush();		
			printer.close();
			printer = null;
			dateFormat = null;
			context = null;
		}
	}
	
	private static boolean isStorageReady() {
		
		final String externalStorageState = Environment.getExternalStorageState();
		boolean isStorageReady = false;
		
		if ( Environment.MEDIA_MOUNTED.equals( externalStorageState ) ) {
		    // We can read and write the media
			isStorageReady = true;
		} 
		else {
			Log.i(Logger.class.getSimpleName(), "Storage not ready to save logs.");
		}
		
		return isStorageReady;
		
	}
	
	private static void createExternalStorageLogFile() {

		File file = hasExternalStorageLogFile();
		if( file != null) {
			try {
				printer = new PrintWriter( new FileWriter(file, true) );
			} catch (IOException e) {
				Log.e(Logger.class.getSimpleName(), e.toString());
			}
		}
	    
	}
	
	private static File hasExternalStorageLogFile() {
		
		int stringId = context.getApplicationInfo().labelRes;
	    String appName = context.getString(stringId);
	    if(appName == null) {
	    	appName = "R2BAppsBaseProject";
	    }
	    else {
	    	appName = appName.replaceAll("\\s+",""); // Replace whitespaces and non visible characteres	    	
	    }
	    	    
	    // Create a path where we will place our file on external storage
	    File sdCard = Environment.getExternalStorageDirectory();  
	    File root = new File (sdCard.getAbsolutePath() + "/" + appName);  
		if(!root.exists()) {
			root.mkdirs();
		}
		
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(root, appName + ".log");	   
	    if(!file.exists()) {
	    	try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e(Logger.class.getSimpleName(), e.toString());
			}
	    }
	    
	    return file;
	  
	}
	
	/**
	 * Print log with the format: Level\tTime\tPID\tTID\tApplication\tTag\tText\n
	 * @param level
	 * @param tag
	 * @param msg
	 * @return
	 */
	private static String parseLog(String level, String tag, String msg) {
		StringBuilder log = new StringBuilder();
		log.append(level).append("\t");
		log.append(dateFormat.format(new Date(System.currentTimeMillis()))).append("\t");
		log.append(android.os.Process.myPid()).append("\t");
		log.append(Thread.currentThread().getId()).append("\t");
		log.append(context.getPackageName()).append("\t");
		log.append(tag).append("\t");
		log.append(msg).append("\n");
		return log.toString();
	}
	
}
