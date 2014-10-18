/*
 * FileUtils
 * 
 * 0.1
 * 
 * 2014/07/16
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import r2b.apps.utils.logger.Logger;
import android.content.Context;
import android.os.Environment;

/**
 * File utility class.
 */
public final class FileUtils {
	
	/**
	 * Check if external storage is ready to read and write.
	 * @return True is ready, false otherwise.
	 */
	public static boolean isExternalStorageReady() {
		
		final String externalStorageState = Environment.getExternalStorageState();
		boolean isStorageReady = false;
		
		if ( Environment.MEDIA_MOUNTED.equals( externalStorageState ) ) {
		    // We can read and write the media
			isStorageReady = true;
		} 
		else {
			Logger.i(FileUtils.class.getSimpleName(), 
					"External storage not ready to save logs.");
		}
		
		return isStorageReady;
		
	}
	
	/**
	 * Open or create a file on external storage.
	 * @param context Application context
	 * @param dirName Directory name
	 * @param fileName File name (with extension)
	 * @return The created file, or the opened file if exist previously.
	 */
	public static File createExternalStorageFile(Context context, String dirName, String fileName) {
	    	    
	    // Create a path where it will place the file on external storage
	    File sdCard = Environment.getExternalStorageDirectory();  
	    File root = new File (sdCard.getAbsolutePath() + File.separator + dirName);  
		if(!root.exists()) {
			root.mkdirs();
		}
		
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(root, fileName);	   
	    if(!file.exists()) {
	    	try {
				file.createNewFile();
			} catch (IOException e) {
				Logger.e(FileUtils.class.getSimpleName(), e.toString());
			}
	    }
	    
	    return file;
	    
	}
	
	/**
	 * Open or create a file on internal storage.
	 * @param context Application context
	 * @param fileName File name (with extension)
	 * @return The created file, or the opened file if exist previously.
	 */
	public static File createInternalStorageFile(Context context, String fileName) {
		File file = new File(context.getFilesDir(), fileName);
		return file;
	}
	
	/**
	 * Copy src to dst.
	 * @param src Source file.
	 * @param dst Destination file.
	 */
	public static void copy(final File src, File dst) {		
		try {
		    FileInputStream inStream = new FileInputStream(src);
		    FileOutputStream outStream = new FileOutputStream(dst);
		    FileChannel inChannel = inStream.getChannel();
		    FileChannel outChannel = outStream.getChannel();
		    inChannel.transferTo(0, inChannel.size(), outChannel);
		    inStream.close();
		    outStream.close();
		}
		catch (IOException e) {
			Logger.e(FileUtils.class.getSimpleName(), e.toString());
		}
	}
	
	/**
	 * Get the file path without the file name.
	 * @param file The file.
	 * @return The file path.
	 */
	public static String getFilePath(final File file) {
		return getFilePath(file.getAbsolutePath());
	}
	
	/**
	 * Get the file path without the file name.
	 * @param absolutePath The absolute path of the file.
	 * @return The file path.
	 */
	public static String getFilePath(final String absolutePath) {
		String filePath = absolutePath.
			    substring(0, absolutePath.lastIndexOf(File.separator));
		
		return filePath;
	}
	
	/**
	 * Get the file name from absolute path.
	 * @param absolutePath The absolute path.
	 * @return The file name.
	 */
	public static String getFileName(String absolutePath) {
		return absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);
	}
	
	/**
	 * Removes files from absolute paths.
	 * @param absolutePath The array of absolute paths to remove.
	 */
	public static void removeFiles(final String[] absolutePaths) {
		
		if(absolutePaths != null) {
			for (String path : absolutePaths) {
				removeFile(path);
			}	
		}
	  
	}
	
	/**
	 * Removes a file from an absolute path.
	 * @param absolutePath The absolute path to remove.
	 */
	public static void removeFile(final String absolutePath) {
		
		if(absolutePath != null) {
		    File fileToDelete = new File(absolutePath);
		    if (fileToDelete != null && 
		    		fileToDelete.exists() && 
		    		fileToDelete.isFile()) {
		    	
				boolean deleted = fileToDelete.delete();
				
	        	if (!deleted) {
	        		Logger.i(FileUtils.class.getSimpleName(), 
	        				"The file '" + absolutePath + "' can not be deleted");
	        	}
	        	
			}
		}	  
		
	}
	
	/**
	 * Get a json array from asset file.
	 * @param context The application context.
	 * @param assetFile The asset file resource id.
	 * @return The JSONArray or null if error.
	 */
	public static JSONArray getJsonArrayFromAssetsFile(Context context, int assetFile) {
		JSONArray json = null;
		
		Object obj = getJsonFromAssetsFile(context, assetFile);
		if(obj != null) {
			json = (JSONArray) obj;
		}
		
		return json;
	}
	
	/**
	 * Get a json object from asset file.
	 * @param context The application context.
	 * @param assetFile The asset file resource id.
	 * @return The JSONObject or null if error.
	 */
	public static JSONObject getJsonObjectFromAssetsFile(Context context, int assetFile) {
		JSONObject json = null;
		
		Object obj = getJsonFromAssetsFile(context, assetFile);
		if(obj != null) {
			json = (JSONObject) obj;
		}
		
		return json;
	}
	
	/**
	 * Get a json entity from asset file.
	 * @param context The application context.
	 * @param assetFile The asset file resource id.
	 * @return The JSONArray/JSONObject or null if error.
	 */
	public synchronized static Object getJsonFromAssetsFile(Context context, int assetFile) {
		BufferedReader reader = null;
		Object json = null;
		
		try {
			InputStream inputStream = context.getResources().openRawResource(assetFile);
		    reader = new BufferedReader(
		        new InputStreamReader(inputStream, "UTF-8")); 
		    StringBuilder builder = new StringBuilder();

		    String mLine = reader.readLine();
		    String firstLine = mLine;
		    while (mLine != null) {
		    	mLine = reader.readLine();
		    	builder.append(mLine); 
		    }
		    
		    if(firstLine.charAt(0) == '[') {
		    	json = new JSONArray(builder.toString());
		    }
		    else {
		    	json = new JSONObject(builder.toString());
		    }
		    
		} catch (IOException e) {
    		Logger.e(FileUtils.class.getSimpleName(), e.toString());
		} catch (JSONException e) {
			Logger.e(FileUtils.class.getSimpleName(), e.toString());
		} finally {
		    if (reader != null) {
		         try {
		             reader.close();
		         } catch (IOException e) {
		        	 Logger.e(FileUtils.class.getSimpleName(), e.toString());
		         }
		    }
		}
		
		return json;
	}

}
