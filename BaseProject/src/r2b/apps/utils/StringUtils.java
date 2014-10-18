/*
 * StringUtils
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

import android.location.Location;

/**
 * String utility class.
 */
public final class StringUtils {
	
	/**
	 * Replace whitespaces and non visible characteres
	 * @param text
	 * @return
	 */
	public static String replaceAllWithespacesAndNonVisibleCharacteres(final String text) {
		String exit = text.replaceAll("\\s+","");
		return exit;
	}
	
	/**
	 * Check if string is null or empty.
	 * @param text Text to check.
	 * @return True null or empty, false otherwise.
	 */
	public static boolean nullOrEmptyString(String text) {
		boolean nullOrEmpty = false;
		if(text == null || text.length() == 0) {
			nullOrEmpty = true;
		}
		return nullOrEmpty;
	}
	
	/**
	 * Remove html tags on text.
	 * @param htmlText The html.
	 * @return Cleaned text.
	 */
	public static String removeHtmlTags(String htmlText) {
		return android.text.Html.fromHtml(htmlText).toString();
	}
	
	/**
	 * Get string format of location
	 * @param loc The location
	 * @return The location in ddº dd' dd,dd N, ddº dd' dd,dd E 
	 */
	public static String parseLocation(Location loc) {
		String cordinatesLat = "";

		String seconds = Location.convert(loc.getLatitude(), 
		Location.FORMAT_SECONDS);

        String[] splitLat = seconds.split(":"); 

        cordinatesLat += splitLat[0] + "º ";
        cordinatesLat += splitLat[1] + "' ";
        cordinatesLat += splitLat[2] + "'' ";
        if(loc.getLatitude() >= 0) { //N
            cordinatesLat += "N";
        }
        else { // S
            cordinatesLat += "S";
        }

        seconds = "";

        String cordinatesLng = "";          
        seconds = Location.convert(loc.getLongitude(), Location.FORMAT_SECONDS);

        String[] splitLng = seconds.split(":"); 

        cordinatesLng += splitLng[0] + "º ";
        cordinatesLng += splitLng[1] + "' ";
        cordinatesLng += splitLng[2] + "'' ";
        if(loc.getLatitude() >= 0) { //E
            cordinatesLng += "E";
        }
        else { // W
            cordinatesLng += "W";
        }

        String cordinates = cordinatesLat + ", " + cordinatesLng;
        
        return cordinates;
	}

}
