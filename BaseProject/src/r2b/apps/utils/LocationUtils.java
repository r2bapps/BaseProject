/*
 * LocationUtils
 * 
 * 0.1
 * 
 * 2014/09/18
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

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Locations and maps utility class.
 */
public final class LocationUtils {
	
	/**
	 * Distance between two locations.
	 * @param latA
	 * @param lngA
	 * @param latB
	 * @param lngB
	 * @return The distance.
	 */
	public static double distanceBetweenTwoLocations(double latA, double lngA, double latB, double lngB) {
		Location locationA = new Location("A");

	    locationA.setLatitude(latA);
	    locationA.setLongitude(lngA);

	    Location locationB = new Location("B");

	    locationB.setLatitude(latB);
	    locationB.setLongitude(lngB);

	    double distance = locationA.distanceTo(locationB);

	    return distance;
	}
	
	/**
	 * Check google play service installed.
	 * @param parent Activity parent.
	 * @return True installed, false otherwise.
	 */
	public static boolean checkGooglePlayServicesInstalled(final Activity parent) {
		final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(parent);
		boolean installed = false;
		if (resultCode == ConnectionResult.SUCCESS) {
			installed = true;
		}
		
		return installed;
	}
	
}
