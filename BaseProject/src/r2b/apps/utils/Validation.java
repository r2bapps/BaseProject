/*
 * Validation
 * 
 * 0.1
 * 
 * 2014/07/24
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Validation utility class.
 */
public final class Validation {
	

	private static final String DECIMAL = "[-+]?[0-9]*\\.?[0-9]+";
	private static final String SYMBOLS = "[_\\W]";
	private static final String LOWER_UPPER_DIGIT_SYMBOL = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*(_|[^\\w])).+$";
	private static final String DATE_yyyy_mm_dd = 
			"^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$";
	
	/**
	 * Credit cards
	 */
	private static final String VISA = "^4[0-9]{12}(?:[0-9]{3})?$";
	private static final String MASTERDCARD = "^5[1-5][0-9]{14}$";
	private static final String AMEX = "^3[47][0-9]{13}$";
	private static final String DINERS = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
	
	
	public static boolean email(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
			valid =  android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
		}
		
		return valid;
	}
	
	public static boolean phone(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
			valid =  android.util.Patterns.PHONE.matcher(input).matches();
		}
		
		return valid;
	}
	
	public static boolean ip(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
			valid =  android.util.Patterns.IP_ADDRESS.matcher(input).matches();
		}
		
		return valid;
	}
	
	public static boolean url(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
			valid =  android.util.Patterns.WEB_URL.matcher(input).matches();
		}
		
		return valid;
	}
	
	/**
	 * Check integer with a min and max length
	 * @param input
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean integer(String input, int min, int max) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
		      Pattern r = Pattern.compile("[0-9]{"+min+","+max+"}");
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	/**
	 * Check text with a min and max length
	 * @param input
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean text(String input, int min, int max) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) && 
				input.length() >= min && 
				input.length() <= max) {
		      valid = true;
		}
		
		return valid;
	}
	
	/**
	 * Check text with at least one digit
	 * @param input
	 * @return
	 */
	public static boolean containsAtLeastOneDigit(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input)) {
		      Pattern r = Pattern.compile("\\d");
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	/**
	 * Check text with at least one symbol
	 * @param input
	 * @return
	 */
	public static boolean containsAtLeastOneSymbol(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input)) {
		      Pattern r = Pattern.compile(SYMBOLS);
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	/**
	 * Check text with at least one lower case, 
	 * one upper case, one digit and one symbol.
	 * 
	 * @param input
	 * @return
	 */
	public static boolean containsAtLeastOneLowerUpperDigitSymbol(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input)) {
		      Pattern r = Pattern.compile(LOWER_UPPER_DIGIT_SYMBOL);
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	public static boolean decimalNumber(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
		      Pattern r = Pattern.compile(DECIMAL);
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	/**
	 * Check date in format yyyy-mm-dd or yyyy/mm/dd
	 * @param input
	 * @return
	 */
	public static boolean date(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
		      Pattern r = Pattern.compile(DATE_yyyy_mm_dd);
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	public static boolean visa(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
		      Pattern r = Pattern.compile(VISA);
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	public static boolean mastercard(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
		      Pattern r = Pattern.compile(MASTERDCARD);
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	public static boolean amex(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
		      Pattern r = Pattern.compile(AMEX);
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
	
	public static boolean diners(String input) {	
		boolean valid = false;
		
		if ( !StringUtils.nullOrEmptyString(input) ) {
		      Pattern r = Pattern.compile(DINERS);
		      Matcher m = r.matcher(input);
		      valid = m.matches();
		}
		
		return valid;
	}
    
}
