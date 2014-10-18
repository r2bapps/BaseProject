/*
 * SecurePreferences
 * 
 * 0.1.1
 * 
 * 2014/06/26
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

package r2b.apps.utils.cipher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import r2b.apps.utils.logger.Logger;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Wrapper class for Android's {@link SharedPreferences} interface, which adds a
 * layer of encryption to the persistent storage and retrieval of sensitive
 * key-value pairs of primitive data types.
 * <p>
 * This class provides important - but nevertheless imperfect - protection
 * against simple attacks by casual snooper. It is crucial to remember that
 * even encrypted data may still be susceptible to attacks, especially on rooted
 * or stolen devices!
 * <p>
 */
public class SecurePreferences implements SharedPreferences {
	
	private static SecurePreferences instance;
	private static SharedPreferences prefs;	
	
	private static final class EncryptedEditor implements SharedPreferences.Editor {

		private SharedPreferences.Editor e;
		
		public EncryptedEditor() {
			e = prefs.edit();
		}
		
		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void apply() {
			e.apply();
		}

		@Override
		public Editor clear() {
			e.clear();
			return this;
		}

		@Override
		public boolean commit() {
			return e.commit();
		}

		@Override
		public Editor putBoolean(String key, boolean value) {
			return putString(key, Boolean.toString(value));
		}

		@Override
		public Editor putFloat(String key, float value) {
			return putString(key, Float.toString(value));
		}

		@Override
		public Editor putInt(String key, int value) {
			return putString(key, Integer.toString(value));
		}

		@Override
		public Editor putLong(String key, long value) {
			return putString(key, Long.toString(value));
		}

		@Override
		public Editor putString(String key, String value) {
			final String encryptKey = AESCipher.encrypt(key);
			final String encryptValue = AESCipher.encrypt(value);
			e.putString(encryptKey, encryptValue);
			
			// XXX LOGGER
			Logger.v(this.getClass().getSimpleName(), 
					"Saving string (" + key + "," + value + ") to (" + encryptKey + ", " + encryptValue + ")");
			
			return this;
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public Editor putStringSet(String key, Set<String> values) {
			final Set<String> encryptedValues = new HashSet<String>(values.size());
			
			for (String value : values) {
				encryptedValues.add(AESCipher.encrypt(value));
			}
			
			final String encryptKey = AESCipher.encrypt(key);
			
			e.putStringSet(encryptKey, encryptedValues);
			
			// XXX LOGGER
			Logger.v(this.getClass().getSimpleName(), 
					"Saving string set (" + key + "," + values.toString() + ") "
							+ "to (" + encryptKey + ", " + encryptedValues.toString() + ")");
			
			return this;
		}

		@Override
		public Editor remove(String key) {
			e.remove(AESCipher.encrypt(key));
			return this;
		}
		
	};
		
	private SecurePreferences (Context context, String name) {
		prefs = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
		
		// XXX LOGGER
		Logger.i(this.getClass().getSimpleName(), "Init on private mode.");
	}
	
	/**
	 * Singleton builder.
	 * @param context Application context.
	 * @param name Preferences name.
	 * @return Shared preferences.
	 */
	public static SharedPreferences getSecurePreferences(Context context, String name) {
		instance = new SecurePreferences(context, name);		
		return instance;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String key) {
		return prefs.contains(AESCipher.encrypt(key));
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#edit()
	 */
	@Override
	public Editor edit() {
		return new EncryptedEditor();
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getAll()
	 */
	@Override
	public Map<String, ?> getAll() {
		final Map<String, ?> encryptedMap = prefs.getAll();
		final Map<String, String> decryptedMap = new HashMap<String, String>(encryptedMap.size());
		
		for (Entry<String, ?> entry : encryptedMap.entrySet()) {
			decryptedMap.put(AESCipher.decrypt(entry.getKey()),
					AESCipher.decrypt(String.valueOf(entry.getValue())));
		}
		
		return decryptedMap;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getBoolean(String key, boolean defValue) {
		boolean exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = Boolean.parseBoolean(AESCipher.decrypt(auxValue));	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getFloat(java.lang.String, float)
	 */
	@Override
	public float getFloat(String key, float defValue) {
		float exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = Float.parseFloat(AESCipher.decrypt(auxValue));	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getInt(java.lang.String, int)
	 */
	@Override
	public int getInt(String key, int defValue) {
		int exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = Integer.parseInt(AESCipher.decrypt(auxValue));	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getLong(java.lang.String, long)
	 */
	@Override
	public long getLong(String key, long defValue) {
		long exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = Long.parseLong(AESCipher.decrypt(auxValue));	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(String key, String defValue) {
		String exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = AESCipher.decrypt(auxValue);	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getStringSet(java.lang.String, java.util.Set)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Set<String> getStringSet(String key, Set<String> defaultValues) {
		Set<String> exit = defaultValues;
		final Set<String> encryptedSet = prefs.getStringSet(AESCipher.encrypt(key), null);
		
		if (encryptedSet != null) {
			Set<String> decryptedSet = new HashSet<String>(encryptedSet.size());
			
			for (String encryptedValue : encryptedSet) {
				decryptedSet.add(AESCipher.decrypt(encryptedValue));
			}
			
			exit = decryptedSet;
		}		
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	@Override
	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		prefs.registerOnSharedPreferenceChangeListener(listener);
		
		// XXX LOGGER
		Logger.v(this.getClass().getSimpleName(), 
				"Register preferences change listener: " 
						+ listener == null ? "null" : listener.getClass().getSimpleName());
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#unregisterOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	@Override
	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
		
		// XXX LOGGER
		Logger.v(this.getClass().getSimpleName(), 
				"Unregister preferences change listener: " 
						+ listener == null ? "null" : listener.getClass().getSimpleName());
	}

}
