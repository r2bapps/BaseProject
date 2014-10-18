/*
 * ZipUtils
 * 
 * 0.2
 * 
 * 2014/07/18
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import r2b.apps.utils.logger.Logger;


/**
 * Compress utility class.
 */
public final class ZipUtils {
	
	/**
	 * Writer buffer size.
	 */
	private static final int BUFFER_SIZE = 16384; // 16KB
	 
	/**
	 * Compress a file.
	 * @param zipFile The file where store the file.
	 * @param file The absolute path of the file.
	 * @return Success true, false otherwise.
	 */
	public static boolean zip(File zipFile, final String file) {
		return zip(zipFile, file, false);
	}
	
	/**
	 * Compress an array of files.
	 * @param zipFile The file where store the file.
	 * @param files The absolute paths of the files.
	 * @return Success true, false otherwise.
	 */
	public static boolean zip(File zipFile, final String[]  files) {
		return zip(zipFile, files, false);
	}
	
	/**
	 * 
	 * Compress a file and can delete original file.
	 * @param zipFile The file where store the file.
	 * @param file The absolute path of the file.
	 * @param delete True to delete the original file, false not delete.
	 * @return Success true, false otherwise and not replace the original file.
	 */
	public static boolean zip(File zipFile, final String file, boolean delete) {
	
		final String[] files = new String[1];
		files[0] = file;
		
		return zip(zipFile, files, delete);

	}
	
	/**
	 * 
	 * Compress an array of files and can delete original files.
	 * @param zipFile The file where store the file.
	 * @param file The absolute path of the file.
	 * @param delete True to delete the original file, false not delete.
	 * @return Success true, false otherwise and not replace the original file.
	 */
	public static boolean zip(File zipFile, final String[] files, boolean delete) {
		boolean success = false;
		
		success = write(zipFile, files);
		
		if (success && delete) {	
			FileUtils.removeFiles(files);	
		}
		
		return success;
	}
	
	/**
	 * Compress an array of files on a single file container.
	 * @param zipFile The file where store the file.
	 * @param files The file to compress.
	 * @return Success true, false otherwise.
	 */
	private static boolean write(File zipFile, final String[] files) {
		
		boolean success = false;
		
		ZipOutputStream out = null;
		
        try {
        	FileInputStream fi = null;
            BufferedInputStream origin = null;
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
            byte data[] = new byte[BUFFER_SIZE];
 
            for (int i = 0; i < files.length; i++) {

            	Logger.i(ZipUtils.class.getSimpleName(), "Adding: " + files[i]);        	
            	
            	// Read the file
            	File file = new File(files[i]);
                fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
 
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                int count;
 
                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
                
                origin.close();                
                fi.close();
            }
 
            out.finish();
            
            success = true;
            
            Logger.i(ZipUtils.class.getSimpleName(), "The file '" 
            		+ zipFile.getName() + "' was compressed successfully");

        	
        } catch (Exception e) {

        	Logger.e(ZipUtils.class.getSimpleName(), e.toString());
        	
        } finally {
        	
        	if (out != null) {
        		try {
					out.close();
				} catch (IOException e) {
					Logger.e(ZipUtils.class.getSimpleName(), e.toString());
				}
        	}
        	
        }
        
        return success;
        
    }
	
}
