/*
 * Utils
 * 
 * 0.2
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

import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import r2b.apps.utils.logger.Logger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Generic utility class.
 */
public final class Utils {

	/**
	 * Get the application name.
	 * @param context The application context.
	 * @return Application name or null if is not provided with its label.
	 */
	public static String getApplicationName(Context context) {
		
		int stringId = context.getApplicationInfo().labelRes;
	    String appName = context.getString(stringId);
	    
	    return appName;	    
	}
	
	/**
	 * Make a call if number is not null or empty.
	 * @param context The application context.
	 * @param number Number to call.
	 */
	public static void makeACall(Context context, String number) {
		if( !StringUtils.nullOrEmptyString(number) ) {
			String url = "tel:" + number;
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse(url));
			context.startActivity(intent);	
		}
	}
	
	/**
	 * Open the dialer with the number inserted, 
	 * if number is not null or empty.
	 * @param context The application context.
	 * @param number Number to call.
	 */
	public static void openDialerToCall(Context context, String number) {
		if( !StringUtils.nullOrEmptyString(number) ) {
			String url = "tel:" + number;
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
	
	/**
	 * Open the send sms app if number is not null or empty.
	 * @param context The application context.
	 * @param number Number to call.
	 * @param text Text to send if is not null or empty.
	 */
	public static void openToSendSms(Context context, String number, String text) {
		if( !StringUtils.nullOrEmptyString(number) ) {
			String url = "tel:" + number;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			if( !StringUtils.nullOrEmptyString(text) ) {
				intent.putExtra("sms_body", text);
			}
			intent.setType("vnd.android-dir/mms-sms"); 
			context.startActivity(intent);
		}
	}
	
	/**
	 * Open the send email app.
	 * @param context Aplication context.
	 * @param account Email account to send.
	 * @param text Text to send.
	 */
	public static void openToSendEmail(Context context, String account, String text){
		if( !StringUtils.nullOrEmptyString(account) &&
				!StringUtils.nullOrEmptyString(text) ) {
			Intent i = new Intent(Intent.ACTION_SEND);
		    i.setType("text/plain");
		    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    i.putExtra(Intent.EXTRA_EMAIL, new String[]{account});
		    context.startActivity(Intent.createChooser(i, text));
		}
	}
	
	/**
	 * Hide the keyboard
	 * @param context
	 * @param view
	 */
	public static void hideKeyBoard(Context context, View view){
		InputMethodManager imm = (InputMethodManager)context.
				getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/**
	 * Show the keyboard
	 * @param context
	 * @param view
	 */
	public static void showKeyBoard(Context context, View view){
		InputMethodManager imm = (InputMethodManager)context.
				getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}
	
	/**
	 * Load the GPS settings screen to activate/deactivate the GPS.
	 * @param activity The current activity.
	 * @param requestValue The request value for retrieve on activity for result.
	 */
	public static void openGPSSettingsScreen(Activity activity, int requestValue){
		Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		activity.startActivityForResult(intent, requestValue);
	}
	
	/**
	 * Load the contacts screen to select one and retrieve on activity for result.
	 * @param activity The current activity.
	 * @param requestValue The request value for retrieve on activity for result.
	 */
	public static void openContactsScreen(Activity activity, int requestValue){
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		activity.startActivityForResult(intent, requestValue);
	}
	
	/**
	 * Open a url in the app to view urls.
	 * @param context Application context.
	 * @param url The url.
	 */
	public static void openUrl(Context context, String url){
		if( !StringUtils.nullOrEmptyString(url) ) {
			context.startActivity(
					new Intent("android.intent.action.VIEW", Uri.parse(url)));
		}
	}
	
	/**
	 * Force a locale to the app.
	 * @param context Application context.
	 * @param locale Locale to force.
	 */
	public static void forceLocale(Context context, String locale){
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(locale);
		res.updateConfiguration(conf, dm);
	}
	
	/**
	 * Get the app version name.
	 * @param context Application context.
	 * @return App version anme or empty string if error.
	 */
	public static String getAppVersion(Context context) {
		String version = "";
        try {
			version = context.getPackageManager().
					getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Logger.e(Utils.class.getSimpleName(), e.toString());
		}
		
		return version;
	}
	
	/**
	 * Dip to pixels.
	 * @param context Application context.
	 * @param dipValue dip value.
	 * @return Pixels value.
	 */
	public static float dipToPixels(Context context, float dipValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}
	
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
	 * Get app version name from manifest.
	 * @param context App context.
	 * @return The manifest version name.
	 * @throws NameNotFoundException
	 */
	public static String getAppVersionName(Context context) throws NameNotFoundException {
		String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		return versionName;
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
	
	/**
	 * Get android id of the device.
	 * WARNING: Factory reset changes this id.
	 * @param context App context.
	 * @return The android id.
	 */
	public static String getAndroidId(Context context) {
		String id = Settings.Secure
        		.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		return id;
	}
	
}
