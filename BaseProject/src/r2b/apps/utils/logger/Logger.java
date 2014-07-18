/*
 * Logger
 * 
 * 0.3
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

package r2b.apps.utils.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import r2b.apps.utils.Cons;
import android.annotation.SuppressLint;
import android.content.Context;
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
	 * Application context.
	 */
	private static Context context;	
	/**
	 * Receivers to call.
	 */
	private static Receiver [] receivers = new Receiver[0];
	/**
	 * Initialized flag.
	 */
	private static boolean initialized;
	
	/**
	 * Logcat date format.
	 */
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM HH:mm:ss.SSS");

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
			
			for(Receiver receiver : receivers) {
				receiver.v(parseLog("V", tag, msg));
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
		
		for(Receiver receiver : receivers) {
			receiver.i(parseLog("I", tag, msg));
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
			
			for(Receiver receiver : receivers) {
				receiver.d(parseLog("D", tag, msg));
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
		
		for(Receiver receiver : receivers) {
			receiver.e(parseLog("E", tag, msg));
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
		
		for(Receiver receiver : receivers) {
			receiver.e(parseLog("E", tag, msg));
		}
	}
	
	public static void init(final Context context, Receiver [] receivers) {
		Logger.context = context.getApplicationContext();
		
		if(receivers != null) {
			Logger.receivers = new Receiver[receivers.length];
			
			for(int i = 0; i < receivers.length; i++) {
				Logger.receivers [i] = receivers [i];
			}
		}
		
		initialized = true;
		
	}
	
	public static void close() {
		if(initialized) {
			
			Thread worker = new Thread() {
				
				@Override
				public void run() {											
					for(Receiver receiver : receivers) {
						receiver.close();
					}
					
					Logger.receivers = null;
					Logger.context = null;
				}
			};
			
			worker.start();
			
			initialized = false;
		}
	}
	
	/**
	 * Print log with the format: Level\tTime\tPID\tTID\tApplication\tTag\tText\n
	 * @param level
	 * @param tag
	 * @param msg
	 * @return
	 */
	private synchronized static String parseLog(String level, String tag, String msg) {
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
